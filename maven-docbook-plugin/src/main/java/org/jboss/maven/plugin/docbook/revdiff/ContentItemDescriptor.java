package org.jboss.maven.plugin.docbook.revdiff;

/**
 * {@inheritDoc}
 *
 * @author Christian Bauer
 */
public class ContentItemDescriptor {
    public static int REVISION_IGNORE = -1;

    private final String sourceSystemId;
    private final String elementName;
    private final int revision;
    private final int row;
    private final int column;

	public ContentItemDescriptor(String sourceSystemId, String elementName, int revision, int row, int column) {
		this.sourceSystemId = sourceSystemId;
		this.elementName = elementName;
		this.revision = revision;
		this.row = row;
		this.column = column;
	}

	public int getRevision() {
        return revision;
    }

    public String getElementName() {
        return elementName;
    }

    public String getSourceSystemId() {
        return sourceSystemId;
    }

    public int getRow() {
        return row;
    }

	public int getColumn() {
		return column;
	}
}
