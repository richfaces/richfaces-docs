package org.jboss.maven.plugin.docbook.revdiff;

/**
 * Describes a particular piece of content, including descriptors
 * from both the master and a particular translation
 *
 * @author Christian Bauer
 */
public class ContentItem {
    private final String identifier;
    private ContentItemDescriptor masterDescriptor;
    private ContentItemDescriptor translationDescriptor;

    public ContentItem(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ContentItemDescriptor getMasterDescriptor() {
        return masterDescriptor;
    }

    public void setMasterDescriptor(ContentItemDescriptor masterDescriptor) {
        this.masterDescriptor = masterDescriptor;
    }

    public ContentItemDescriptor getTranslationDescriptor() {
        return translationDescriptor;
    }

    public void setTranslationDescriptor(ContentItemDescriptor translationDescriptor) {
        this.translationDescriptor = translationDescriptor;
    }


	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		ContentItem that = ( ContentItem ) o;

		if ( !identifier.equals( that.identifier ) ) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		return identifier.hashCode();
	}
}
