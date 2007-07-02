package org.jboss.maven.plugin.docbook.gen.xslt.resolve;

import java.net.URL;
import javax.xml.transform.URIResolver;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;

/**
 * Responsible for resolving relative references from jar base urls.
 *
 * @author Steve Ebersole
 */
public class RelativeJarUriResolver implements URIResolver {
	public Source resolve(String href, String base) throws TransformerException {
		// href need to be relative
		if ( href.indexOf( "://" ) > 0 || href.startsWith( "/" ) ) {
			return null;
		}

		// base would need to start with jar:
		if ( null == base || (!base.startsWith( "jar:" )) ) {
			return null;
		}

		String fullHref = base.substring( 4, base.lastIndexOf( '/' ) + 1 )
				+ href;
		try {
			URL url = new URL( fullHref );
			return new StreamSource( url.openStream(), url.toExternalForm() );
		}
		catch ( Throwable t ) {
			return null;
		}
	}
}
