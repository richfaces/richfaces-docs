package org.jboss.maven.plugin.docbook.gen.render;

import java.io.File;
import java.io.IOException;

import org.jboss.maven.plugin.docbook.gen.xslt.XSLTException;

/**
 * Responsible for rendering a given source document in a particular format.
 *
 * @author Steve Ebersole
 */
public interface Renderer {
	public File prepareDirectory() throws RenderingException;
	public void render(File source) throws RenderingException, XSLTException;
}
