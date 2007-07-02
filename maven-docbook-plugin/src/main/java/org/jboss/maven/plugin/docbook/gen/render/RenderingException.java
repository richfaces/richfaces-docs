package org.jboss.maven.plugin.docbook.gen.render;

/**
 * {@inheritDoc}
 *
 * @author Steve Ebersole
 */
public class RenderingException extends Exception {

	public RenderingException(String message) {
		super( message );
	}

	public RenderingException(String message, Throwable cause) {
		super( message, cause );
	}
}
