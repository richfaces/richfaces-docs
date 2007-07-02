package org.jboss.maven.plugin.docbook.gen.xslt.resolve;

import java.net.URL;
import java.io.IOException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.jboss.maven.plugin.docbook.gen.util.FormatType;
import org.jboss.maven.plugin.docbook.gen.util.ResourceHelper;
import org.jboss.maven.plugin.docbook.gen.xslt.XSLTException;

/**
 * Resolves an explicit <tt>urn:docbook:stylesheet</tt> URN against the standard
 * DocBook stylesheets.
 *
 * @author Steve Ebersole
 */
public class ExplicitUrnResolver extends BasicUrnResolver {
	private final FormatType formatType;

	public ExplicitUrnResolver(FormatType type) throws XSLTException {
		super( "urn:docbook:stylesheet", createSource( type ) );
		this.formatType = type;
	}

	private static Source createSource(FormatType type) throws XSLTException {
		URL stylesheet = ResourceHelper.requireResource( type.getStylesheetResource() );
		try {
			return new StreamSource( stylesheet.openStream(), stylesheet.toExternalForm() );
		}
		catch ( IOException e ) {
			throw new XSLTException( "could not locate DocBook stylesheet [" + type.getName() + "]", e );
		}
	}

	public String toString() {
		return super.toString() + " [" + formatType.getName() + "]";
	}
}
