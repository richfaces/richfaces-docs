package org.jboss.maven.plugin.docbook.gen.render;

import java.io.File;

import org.jboss.maven.plugin.docbook.gen.Format;
import org.jboss.maven.plugin.docbook.gen.Options;
import org.jboss.maven.plugin.docbook.gen.util.TransformerType;
import org.jboss.maven.plugin.docbook.gen.util.FormatType;
import org.jboss.maven.plugin.docbook.gen.xslt.XSLTException;
import org.jboss.maven.plugin.docbook.gen.xslt.TransformerFactory;
import org.jboss.maven.plugin.docbook.gen.xslt.catalog.ImplicitCatalogManager;
import org.jboss.maven.plugin.docbook.gen.xslt.catalog.ExplicitCatalogManager;
import org.apache.xml.resolver.CatalogManager;
import org.apache.xml.resolver.tools.CatalogResolver;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

/**
 * Builds a renderer for the given format
 *
 * @author Steve Ebersole
 */
public class RendererFactory {
	private final Options options;
	private final File source;
	private final File targetDirectory;
	private final MavenProject project;
	private Log log;

	private CatalogResolver catalogResolver;
	private TransformerFactory transformerFactory;

	public RendererFactory(Options options, File source, File targetDirectory, MavenProject project, Log log) {
		this.options = options;
		this.source = source;
		this.targetDirectory = targetDirectory;
		this.project = project;
		this.log = log;
	}


	public Options getOptions() {
		return options;
	}

	public File getSource() {
		return source;
	}

	public File getTargetDirectory() {
		return targetDirectory;
	}

	public Log getLog() {
		return log;
	}

	public CatalogResolver getCatalogResolver() {
		if ( catalogResolver == null ) {
			CatalogManager catalogManager;
			if ( options.getCatalogs() == null || options.getCatalogs().length == 0 ) {
				catalogManager = new ImplicitCatalogManager();
			}
			else {
				catalogManager = new ExplicitCatalogManager( options.getCatalogs() );
			}
			catalogResolver = new CatalogResolver( catalogManager );
		}
		return catalogResolver;
	}

	public TransformerFactory getTransformerFactory() {
		if ( transformerFactory == null ) {
			transformerFactory = new TransformerFactory(
					TransformerType.parse( options.getXmlTransformerType() ),
					options.getTransformerParameters(),
					getCatalogResolver(),
					options.getDocbookVersion()
			);
		}
		return transformerFactory;
	}

	public Renderer buildRenderer(Formatting formatting) throws XSLTException {
		// todo : these usages of 'FormatType' are ok as well
		if ( formatting.getFormatName().equals( FormatType.PDF.getName() ) ) {
			return new PdfRenderer( this, formatting );
		}
		else if ( formatting.getFormatName().equals( FormatType.HTML.getName() )
				|| formatting.getFormatName().equals( FormatType.HTML_SINGLE.getName() ) ) {
			return new HtmlRenderer( this, formatting );
		}
		else {
			return new BasicRenderer( this, formatting );
		}
	}


	/**
	 * @return the project
	 */
	public MavenProject getProject() {
	    return project;
	}
}
