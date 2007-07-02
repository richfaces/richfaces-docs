package org.jboss.maven.plugin.docbook.gen.util;

/**
 * Enumeration of supported XSLT transformers.
 *
 * @author Steve Ebersole
 */
public class TransformerType {
	public static final TransformerType SAXON = new TransformerType( "saxon", false );
	public static final TransformerType XALAN = new TransformerType( "xalan", true );

	private final String name;
	private final boolean supportsReset;

	private TransformerType(String name, boolean supportsReset) {
		this.name = name;
		this.supportsReset = supportsReset;
	}

	public String getName() {
		return name;
	}

	public boolean supportsReset() {
		return supportsReset;
	}

	public static TransformerType parse(String name) {
		if ( XALAN.name.equals( name ) ) {
			return XALAN;
		}
		else {
			// default
			return SAXON;
		}
	}
}
