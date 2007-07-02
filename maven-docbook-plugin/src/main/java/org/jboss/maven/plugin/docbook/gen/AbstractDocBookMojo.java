package org.jboss.maven.plugin.docbook.gen;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.jboss.maven.plugin.docbook.gen.render.Formatting;
import org.jboss.maven.plugin.docbook.gen.render.RenderingException;
import org.jboss.maven.plugin.docbook.gen.util.FormatType;
import org.jboss.maven.plugin.docbook.gen.xslt.XSLTException;

/**
 * Basic support for the various DocBook mojos in this package.  Mainly, we are
 * defining common configuration attributes of the packaging.
 *
 * @author Steve Ebersole
 */
public abstract class AbstractDocBookMojo extends AbstractMojo {
    /**
	 * INTERNAL : The project being built
	 *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;

	/**
	 * The name of the document (relative to sourceDirectory) which is the
	 * document to be rendered.
	 *
	 * @parameter
	*  @required
	 */
	protected String sourceDocumentName;

	/**
	 * The directory where the sources are located.
	 *
	 * @parameter expression="${basedir}/src/main/docbook"
	 */
	protected File sourceDirectory;

	/**
	 * The directory where the output will be written.
	 *
	 * @parameter expression="${basedir}/target/docbook"
	 */
	protected File targetDirectory;

	/**
	 * The formats in which to perform rendering.
	 *
     * @parameter
	*  @required
	 */
	protected Format[] formats;

	/**
	 * Configurable options
	 *
     * @parameter
	 */
	protected Options options;

	public Formatting[] getFormattings() {
		Formatting[] formattings = new Formatting[ formats.length ];
		for ( int i = 0; i < formats.length; i++ ) {
			formattings[i] = new Formatting( FormatType.parse( formats[i].getFormatName() ), formats[i] );
		}
		return formattings;
	}

	public final void execute() throws MojoExecutionException, MojoFailureException {
		try {
			process( getFormattings() );
		}
		catch ( XSLTException e ) {
			throw new MojoExecutionException( "XSLT problem", e );
		}
		catch ( RenderingException e ) {
			throw new MojoExecutionException( "Rendering problem", e );
		}
	}

	protected abstract void process(Formatting[] formattings) throws RenderingException, XSLTException;
}
