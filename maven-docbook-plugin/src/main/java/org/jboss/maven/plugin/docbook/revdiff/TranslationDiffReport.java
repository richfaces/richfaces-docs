package org.jboss.maven.plugin.docbook.revdiff;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.codehaus.plexus.util.DirectoryScanner;

/**
 * A plugin for generating a "translation diff" report across different
 * translations of the same document.  This is useful for the translators to
 * know what changes exist between their translation and the master.
 *
 * @goal diff
 * 
 * @author Christian Bauer
 * @author Steve Ebersole
 */
public class TranslationDiffReport extends AbstractMavenReport {

	public static final String NAME = "translation-diff-report";

	/**
	 * The directory containing the translated DocBook sources.
	 *
	 * @parameter expression="${basedir}/src/main/docbook/translations"
     * @required
	 */
	private File translationsDirectory;

	/**
     * Directory where reports will go.
     *
     * @parameter expression="${project.reporting.outputDirectory}"
     * @required
     * @readonly
     */
    private File reportingDirectory;

	/**
	 * Which translation is considered the master.
	 *
	 * @parameter default-value="en"
	 */
	private String masterTranslation;

	/**
	 * (non master) translations to be included in the report.
	 *
	 * @parameter
	 */
	private String[] translationIncludes;

	/**
	 * Patternsets of sources (relative to translationsDirectory) to include
	 * in reporting.
	 *
	 * @parameter
	 */
	private String[] sourceIncludes;

    /**
     * A boolean, indicating if XInclude should be supported.
     *
     * @parameter default-value="false"
     */
    private boolean xincludeSupported;

	/**
     * Whether to build an aggregated report at the root, or build individual reports.
     *
     * @parameter expression="${aggregate}" default-value="false"
     */
    protected boolean aggregate;

	/**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

	private File reportOutputDirectory;

	/**
	 * @parameter expression="${component.org.apache.maven.doxia.siterenderer.Renderer}"
     * @required
     * @readonly
     */
    private Renderer siteRenderer;

	protected Renderer getSiteRenderer() {
		return siteRenderer;
	}

	protected MavenProject getProject() {
		return project;
	}

	public String getCategoryName() {
		return CATEGORY_PROJECT_REPORTS;
	}

	protected String getOutputDirectory() {
		return getReportOutputDirectory().getAbsolutePath();
	}

	public String getOutputName() {
		return "index";
	}

	public String getName(Locale locale) {
		return getBundle( locale ).getString( "report.name" );
	}

	public String getDescription(Locale locale) {
		return getBundle( locale ).getString( "report.description" );
	}

	private ResourceBundle getBundle(Locale locale) {
		return ResourceBundle.getBundle( NAME, locale, this.getClass().getClassLoader() );
	}

	public void setReportOutputDirectory(File dir) {
		if ( NAME.equals( dir.getName() ) ) {
			this.reportOutputDirectory = dir;
		}
		else {
			this.reportOutputDirectory = new File( dir, NAME );
		}
	}

	public File getReportOutputDirectory() {
		if ( reportOutputDirectory == null ) {
			reportOutputDirectory = new File( reportingDirectory, NAME );
		}
		return reportOutputDirectory;
	}

	public boolean isExternalReport() {
		return false;
	}

	public boolean canGenerateReport() {
		return true;
	}

	protected void executeReport(Locale locale) throws MavenReportException {
		getLog().debug( "starting docbook:diff goal execution [masterTranslation=" + masterTranslation + "]" );

		if ( ! translationsDirectory.exists() ) {
			getLog().debug( "translations directory [" + translationsDirectory.getAbsolutePath() + "] did not exist" );
			return;
		}

		File masterTranslationDirectory = new File( translationsDirectory, masterTranslation );
		if ( ! masterTranslationDirectory.exists() ) {
			getLog().info( "master translation directory [" + masterTranslationDirectory.getAbsolutePath() + "] did not exist" );
			return;
		}

		if ( translationIncludes == null || translationIncludes.length == 0 ) {
			// use all translations...
			ArrayList includes = new ArrayList();
			File[] subdirs = translationsDirectory.listFiles();
			for ( int i = 0; i < subdirs.length; i++ ) {
				includes.add( subdirs[i].getName() );
			}
			translationIncludes = ( String[] ) includes.toArray( new String[ includes.size() ] );
		}

		if ( sourceIncludes == null || sourceIncludes.length == 0 ) {
			sourceIncludes = new String[] { "*.xml" };
		}

		List reports = new ArrayList();
		for ( int i = 0; i < translationIncludes.length; i++ ) {
			if ( masterTranslation.equals( translationIncludes[i] ) ) {
				continue;
			}
			final File translationDirectory = new File( translationsDirectory, translationIncludes[i] );
			if ( ! translationDirectory.exists() ) {
				getLog().info( "translation directory [" + translationDirectory.getAbsolutePath() + "] did not exist" );
				continue;
			}

			if ( generateTranslationReport( masterTranslationDirectory, translationDirectory, translationIncludes[i] ) ) {
				reports.add(
						new IndexReportGenerator.TranslationReportDescriptor(
								new Locale( translationIncludes[i] ),
								NAME + "/" + buildTranslationReportName( translationIncludes[i] )
						)
				);
			}
		}

		new IndexReportGenerator( getBundle( locale ), getSink(), getLog() ).generate( reports );
	}

	private void prepReportFile(File reportFile) throws MavenReportException {
		if ( reportFile.exists() ) {
			reportFile.delete();
		}
		if ( !reportFile.exists() ) {
			try {
				reportFile.createNewFile();
			}
			catch ( IOException e ) {
				throw new MavenReportException( "unable to prep report file [" + reportFile.getAbsolutePath() + "]" );
			}
		}
	}

	private String buildTranslationReportName(String translationName) {
		return "report-" + translationName + ".html";
	}

	private boolean generateTranslationReport(
			File masterTranslationDirectory,
			File translationDirectory,
			String translationName) throws MavenReportException {
		getLog().debug( "starting docbook:diff processing [translation=" + translationName + "]" );

		DirectoryScanner scanner = new DirectoryScanner();
		scanner.setBasedir( masterTranslationDirectory );
		scanner.setIncludes( sourceIncludes );
		scanner.scan();
		String[] masterSources = scanner.getIncludedFiles();

		scanner = new DirectoryScanner();
		scanner.setBasedir( translationDirectory );
		scanner.setIncludes( sourceIncludes );
		scanner.scan();
		String[] translationSources = scanner.getIncludedFiles();

		if ( ! Arrays.equals( masterSources, translationSources ) ) {
			getLog().warn( "includes returned different master and translation sets" );
			return false;
		}

		Locale locale = new Locale( translationName );
		File output = new File( getReportOutputDirectory(), buildTranslationReportName( translationName ) );
		prepReportFile( output );

		try {
			DiffCreator diffCreator = new DiffCreator( xincludeSupported, getLog() );
			TranslationReportGenerator generator = new TranslationReportGenerator( translationsDirectory, getBundle( locale ), getLog() );

			for ( int i = 0; i < masterSources.length; i++ ) {
				getLog().debug( "processing source [" + masterSources[i] + "]" );
				final File master = new File( masterTranslationDirectory, masterSources[i] );
				final File translation = new File( translationDirectory, translationSources[i] );
				final Diff diff = diffCreator.findDiff( master, translation );
				generator.generate( diff, output, locale.toString() );
			}
		}
		catch ( GenerationException e ) {
			throw new MavenReportException( "unable to generate report", e );
		}
		return true;
	}

}
