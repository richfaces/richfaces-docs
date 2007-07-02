package org.jboss.maven.plugin.docbook.gen;

import java.io.File;

import org.codehaus.plexus.util.FileUtils;
import org.jboss.maven.plugin.docbook.gen.render.Formatting;
import org.jboss.maven.plugin.docbook.gen.render.RendererFactory;
import org.jboss.maven.plugin.docbook.gen.render.RenderingException;
import org.jboss.maven.plugin.docbook.gen.xslt.XSLTException;

/**
 * A DocBook plugin based on the excellent docbkx-maven-plugin, but which
 * specifically handles language translations in a more transparent way.
 *
 * @goal generate
 * @phase compile
 *
 * @author Steve Ebersole
 */
public class GenerationMojo extends AbstractDocBookMojo {

	protected void process(Formatting[] formattings) throws XSLTException, RenderingException {
		if ( !sourceDirectory.exists() ) {
			getLog().info( "sourceDirectory [" + sourceDirectory.getAbsolutePath() + "] did not exist" );
			return;
		}
		File source = new File( sourceDirectory, sourceDocumentName );
		if ( !source.exists() ) {
			getLog().info( "source [" + source.getAbsolutePath() + "] did not exist" );
			return;
		}

		if ( !targetDirectory.exists() ) {
			FileUtils.mkdir( targetDirectory.getAbsolutePath() );
		}

		RendererFactory rendererFactory = new RendererFactory( options, source, targetDirectory, project, getLog() );

		for ( int i = 0; i < formattings.length; i++ ) {
			rendererFactory.buildRenderer( formattings[i] ).render( source );
		}
	}

}
