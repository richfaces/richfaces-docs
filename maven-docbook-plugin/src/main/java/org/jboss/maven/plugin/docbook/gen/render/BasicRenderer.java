package org.jboss.maven.plugin.docbook.gen.render;


import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.xml.transform.Transformer;
import javax.xml.transform.Source;
import javax.xml.transform.Result;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jboss.maven.plugin.docbook.gen.xslt.XSLTException;
import org.jboss.maven.plugin.docbook.gen.util.ResourceHelper;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.FileUtils;
import org.xml.sax.EntityResolver;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl;

/**
 * {@inheritDoc}
 *
 * @author Steve Ebersole
 */
public class BasicRenderer implements Renderer {
	public static final String DTD_VALIDATION_FEATURE = "http://xml.org/sax/features/validation";
	public static final String DTD_LOADING_FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";

	protected final RendererFactory factory;
	protected final Formatting formatting;

	public BasicRenderer(RendererFactory factory, Formatting formatting) {
		this.factory = factory;
		this.formatting = formatting;
	}

	public File prepareDirectory() throws RenderingException {
		File target = new File( factory.getTargetDirectory(), formatting.getFormatName() );
		if ( ! target.exists() ) {
			FileUtils.mkdir( target.getAbsolutePath() );
		}
		return target;
	}

	public final void render(File source) throws RenderingException, XSLTException {
		getLog().debug( "starting formatting [" + formatting.getFormatName() + "]" );

		File target = prepareTarget( prepareDirectory(), source );

		Transformer transformer = buildTransformer( target );
		Source transformationSource = buildSource( source );
		Result transformationResult = buildResult( target );
		try {
			transformer.transform( transformationSource, transformationResult );
		}
		catch ( TransformerException e ) {
			throw new RenderingException( "unable to perform transformation", e );
		}
		finally {
			releaseResult( transformationResult );
		}
		
	}

	private File prepareTarget(File directory, File source) throws RenderingException {
		String targetFileName = deduceTargetFileName( source );
		getLog().debug( "preparing target file [" + targetFileName + "]" );
		File target = new File( directory, targetFileName );
		if ( target.exists() ) {
			if ( !target.delete() ) {
				getLog().warn( "unable to clean up previous output file [" + target.getAbsolutePath() + "]" );
			}
		}
		if ( !target.exists() ) {
			try {
				target.createNewFile();
			}
			catch ( IOException e ) {
				throw new RenderingException( "unable to create output file [" + target.getAbsolutePath() + "]", e );
			}
		}
		return target;
	}

	private String deduceTargetFileName(File source) {
		return formatting.getNamingStrategy().deduceTargetFileName( source );
	}

	protected Transformer buildTransformer(File targetFile)
			throws RenderingException, XSLTException {
		final URL transformationStylesheet = resolveTransformationStylesheet();
		Transformer transformer = factory.getTransformerFactory()
				.buildTransformer( formatting, transformationStylesheet );
		if ( formatting.isImagePathSettingRequired() ) {
			String imgSrcPath = factory.getTargetDirectory().getAbsolutePath() + "/";
			getLog().debug( "setting 'img.src.path' [" + imgSrcPath + "]" );
			transformer.setParameter( "img.src.path", imgSrcPath );
		}
		if ( factory.getOptions().isUseRelativeImageUris() ) {
			getLog().debug( "enforcing retention of relative image URIs" );
			transformer.setParameter( "keep.relative.image.uris", "0" );
		}
		if ( formatting.isDoingChunking() ) {
			getLog().debug( "Chunking output." );
			String rootFilename = targetFile.getName();
			rootFilename = rootFilename.substring( 0, rootFilename.lastIndexOf( '.' ) );
			transformer.setParameter( "root.filename", rootFilename );
			transformer.setParameter( "base.dir", targetFile.getParent() + File.separator );
                        transformer.setParameter( "manifest.in.base.dir", "1" );
		}
		return transformer;
	}

	protected final URL resolveTransformationStylesheet() throws RenderingException {
		return ResourceHelper.requireResource( formatting.getStylesheetResource() );
//		return format.getStylesheetResource() != null
//				? ResourceHelper.requireResource( format.getStylesheetResource() )
//				: ResourceHelper.requireResource( format.getFormatType().getStylesheetResource() );
	}

	private Source buildSource(File sourceFile) throws RenderingException {
		try {
			EntityResolver resolver = factory.getCatalogResolver();
			SAXParserFactory factory = createParserFactory();
			XMLReader reader = factory.newSAXParser().getXMLReader();
			reader.setEntityResolver( resolver );

			// Disable DTD loading and validation
			reader.setFeature( DTD_LOADING_FEATURE, false );
			reader.setFeature( DTD_VALIDATION_FEATURE, false );

			return new SAXSource( reader, new InputSource( sourceFile.getAbsolutePath() ) );
		}
		catch ( ParserConfigurationException e ) {
			throw new RenderingException( "unable to build SAX Parser", e );
		}
		catch ( SAXException e ) {
			throw new RenderingException( "unable to build SAX Parser", e );
		}
	}

	protected final SAXParserFactory createParserFactory() {
        SAXParserFactory parserFactory = new SAXParserFactoryImpl();
        parserFactory.setXIncludeAware( factory.getOptions().isXincludeSupported() );
        return parserFactory;
    }

	protected Result buildResult(File targetFile) throws RenderingException {
		return new StreamResult( targetFile );
	}

	protected void releaseResult(Result transformationResult) {
		// typically nothing to do...
	}

	protected Log getLog() {
		return factory.getLog();
	}
}
