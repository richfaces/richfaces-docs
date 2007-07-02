package org.jboss.maven.plugin.docbook.revdiff;

import java.util.Map;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Locator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * An XML parse handler used to create a catalog of baseline elements.
 * <p/>
 * Typically this would be used against the master translation to create
 * a baseline against which particular translations would be checked.
 *
 * @author Christian Bauer
 * @author Steve Ebersole
 */
public class BaselineHandler extends DefaultHandler {
	private final Map catalog;
	private Locator docLocator;

	public void setDocumentLocator(Locator locator) {
		this.docLocator = locator;
	}

	public BaselineHandler(Map catalog) {
		this.catalog = catalog;
	}

	public void startElement(
			String namespaceURI,
			String localName,
			String qualifiedName,
			Attributes atts) throws SAXException {

		// Only add ModuleElements that have an identifier
		String identifier = atts.getValue( "id" );
		if ( identifier != null ) {
			// The default revision is 0
			int revision = 0;
			if ( atts.getValue( "revision" ) != null ) {
				revision = new Integer( atts.getValue( "revision" ) ).intValue();
			}

			// Generate new ContentItem and new original state
			ContentItem contentItem = new ContentItem( identifier );
			ContentItemDescriptor descriptor = new ContentItemDescriptor(
					docLocator.getSystemId(),
					qualifiedName,
					revision,
					docLocator.getLineNumber(),
					docLocator.getColumnNumber()
			);
			contentItem.setMasterDescriptor( descriptor );
			catalog.put( identifier, contentItem );
		}
	}
}
