package org.jboss.maven.plugin.docbook.gen.xslt;

/**
 * Indicates problems either building XSLT transformers or performing
 * transformations.
 *
 * @author Steve Ebersole
 */
public class XSLTException extends Exception {

	public XSLTException(String message) {
		super( message );
	}

	public XSLTException(String message, Throwable cause) {
		super( message, cause );
	}
}
