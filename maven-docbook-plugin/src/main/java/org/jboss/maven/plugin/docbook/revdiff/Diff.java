package org.jboss.maven.plugin.docbook.revdiff;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Represents the difference between two sources.
 *
 * @author Steve Ebersole
 */
public class Diff {
	private Set elementsOnlyInMaster = new HashSet();
	private Set elementsOnlyInTranslation = new HashSet();
	private Set elementsDiffRevision = new HashSet();

	public void addOnlyInMaster(ContentItem element) {
		elementsOnlyInMaster.add( element );
	}

	public void addOnlyInTranslation(ContentItem element) {
		elementsOnlyInTranslation.add( element );
	}

	public void addDiffRevision(ContentItem element) {
		elementsDiffRevision.add( element );
	}

	public Iterator getElementsOnlyInMaster() {
		return elementsOnlyInMaster.iterator();
	}

	public Iterator getElementsOnlyInTranslation() {
		return elementsOnlyInTranslation.iterator();
	}
	public Iterator getElementsDiffRevision() {
		return elementsDiffRevision.iterator();
	}
}
