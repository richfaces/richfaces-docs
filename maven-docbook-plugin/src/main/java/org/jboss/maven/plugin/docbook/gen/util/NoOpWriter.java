package org.jboss.maven.plugin.docbook.gen.util;

import java.io.Writer;

/**
 * A writer which does no writing :)
 *
 * @author Steve Ebersole
 */
public class NoOpWriter extends Writer {

	public void write(char cbuf[], int off, int len) {
	}

	public void flush() {
	}

	public void close() {
	}
}
