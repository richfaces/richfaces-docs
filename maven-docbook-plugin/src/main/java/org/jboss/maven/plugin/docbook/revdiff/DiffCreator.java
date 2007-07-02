package org.jboss.maven.plugin.docbook.revdiff;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.apache.maven.plugin.logging.Log;

/**
 * Responsible for creating a diff description.
 *
 * @author Steve Ebersole
 */
public class DiffCreator {

	private final XMLReader parser;
	private final Log log;

	public DiffCreator(boolean xincludeSupport, Log log) throws GenerationException {
		this.parser = generateParser( xincludeSupport );
		this.log = log;
	}

	public synchronized Diff findDiff(File master, File translation) throws GenerationException {
		Map catalog = new HashMap();

		// Parse master file
		BaselineHandler baselineHandler = new BaselineHandler( catalog );
		parser.setContentHandler( baselineHandler );
		try {
			parser.parse( master.getAbsolutePath() );
		}
		catch( IOException e ) {
			throw new GenerationException( "unable to locate specified master [" + master.getAbsolutePath() + "]", e );
		}
		catch ( SAXException e ) {
			throw new GenerationException( "error parsing master [" + master.getAbsolutePath() + "]", e );
		}

		log.debug( "encountered [" + catalog.size() + "] master content elements" );

		Diff diff = new Diff();

		// Parse translation
		TranslationHandler translationHandler = new TranslationHandler( catalog, diff );
		parser.setContentHandler( translationHandler );
		try {
			parser.parse( translation.getAbsolutePath() );
		}
		catch ( IOException e ) {
			throw new GenerationException( "unable to locate specified translation [" + translation.getAbsolutePath() + "]", e );
		}
		catch ( SAXException e ) {
			throw new GenerationException( "error parsing translation [" + translation.getAbsolutePath() + "]", e );
		}

		// as a final step, allow translation handler to finish up
		translationHandler.finish();

		return diff;
	}

	private XMLReader generateParser(boolean xincludeSupport) throws GenerationException {
		try {
			XMLReader parser = createParserFactory( xincludeSupport ).newSAXParser().getXMLReader();
//			SAXParser parser = new SAXParser();
			// Disable validation against DTD
			parser.setFeature( "http://xml.org/sax/features/validation", false );
			// Disable DTD loading in Xerces
			parser.setFeature( "http://apache.org/xml/features/nonvalidating/load-external-dtd", false );
			return parser;
		}
		catch ( SAXNotSupportedException e ) {
			throw new GenerationException( "unable to generate SAX Parser", e );
		}
		catch ( SAXNotRecognizedException e ) {
			throw new GenerationException( "unable to generate SAX Parser", e );
		}
		catch ( ParserConfigurationException e ) {
			throw new GenerationException( "unable to generate SAX Parser", e );
		}
		catch ( SAXException e ) {
			throw new GenerationException( "unable to generate SAX Parser", e );
		}
	}

	private SAXParserFactory createParserFactory(boolean xincludeSupport) {
        SAXParserFactory factory = new SAXParserFactoryImpl();
        factory.setXIncludeAware( xincludeSupport );
        return factory;
    }
}
