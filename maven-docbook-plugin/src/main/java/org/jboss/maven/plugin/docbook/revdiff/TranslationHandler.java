package org.jboss.maven.plugin.docbook.revdiff;

import java.util.Map;
import java.util.Iterator;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Locator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Handles actually discerning diffs between the translations based on the
 * revision numbers of the content elements.
 *
 * @author Christian Bauer
 * @author Steve Ebersole
 */
public class TranslationHandler extends DefaultHandler {
	private final Map catalog;
	private final Diff diff;
	private Locator docLocator;

	public TranslationHandler(Map catalog, Diff diff) {
		this.catalog = catalog;
		this.diff = diff;
	}

	public void setDocumentLocator(Locator locator) {
		this.docLocator = locator;
	}

	public void startElement(
			String namespaceURI,
			String localName,
			String qualifiedName,
			Attributes atts) throws SAXException {

		String identifier = atts.getValue( "id" );
		if ( identifier != null ) {
			int revision = 0;
			if ( atts.getValue( "revision" ) != null ) {
				revision = new Integer( atts.getValue( "revision" ) ).intValue();
			}

			ContentItemDescriptor translationState = new ContentItemDescriptor(
					docLocator.getSystemId(),
					qualifiedName,
					revision,
					docLocator.getLineNumber(),
					docLocator.getColumnNumber()
			);

			// Locate the corresponding ContentItem from the master
			//
			// NOTE: we use remove to keep track of the fact that the transation
			// has used that element; that way, afterwards, we know all the
			// "unused" ones which would indicate elements recently added to
			// the master...
			ContentItem contentItem = ( ContentItem ) catalog.remove( identifier );
			if ( contentItem == null ) {
				// we have encountered an element in the translation which
				// is not present in the master.  This *should indicate that we
				// have a section that was removed from the master, but not yet
				// removed from the translation...
				if ( revision != ContentItemDescriptor.REVISION_IGNORE ) {
					contentItem = new ContentItem( identifier );
					contentItem.setTranslationDescriptor( translationState );
					diff.addOnlyInTranslation( contentItem );
				}
			}
			else {
				// the element is present in both; check the revisions
				if ( revision != contentItem.getMasterDescriptor().getRevision() && revision != ContentItemDescriptor.REVISION_IGNORE ) {
					contentItem.setTranslationDescriptor( translationState );
					diff.addDiffRevision( contentItem );
				}
			}
		}
	}

	public void finish() {
		// anything left in the catalog at this point is stuff that is present
		// in the master but absent from the translation...
		Iterator itr = catalog.entrySet().iterator();
		while ( itr.hasNext() ) {
			final ContentItem contentItem = ( ContentItem ) ( ( Map.Entry ) itr.next() ).getValue();
			itr.remove();
			diff.addOnlyInMaster( contentItem );
		}
	}
}
