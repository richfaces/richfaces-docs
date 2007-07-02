package org.jboss.maven.plugin.docbook.revdiff;

/**
 * Indicates problems generating the diff report
 *
 * @author Steve Ebersole
 */
public class GenerationException extends Exception {
	public GenerationException(String message) {
		super( message );
	}

	public GenerationException(String message, Throwable cause) {
		super( message, cause );
	}
}
