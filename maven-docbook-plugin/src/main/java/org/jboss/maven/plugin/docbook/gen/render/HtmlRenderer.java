package org.jboss.maven.plugin.docbook.gen.render;

import java.io.File;
import javax.xml.transform.Transformer;

import org.jboss.maven.plugin.docbook.gen.xslt.XSLTException;

/**
 * Special handling for html processing
 *
 * @author Steve Ebersole
 */
public class HtmlRenderer extends BasicRenderer {
	public HtmlRenderer(RendererFactory factory, Formatting formatting) {
		super( factory, formatting );
	}

	protected Transformer buildTransformer(File targetFile)
			throws RenderingException, XSLTException {
		Transformer transformer = super.buildTransformer( targetFile );
		return transformer;
	}
}
