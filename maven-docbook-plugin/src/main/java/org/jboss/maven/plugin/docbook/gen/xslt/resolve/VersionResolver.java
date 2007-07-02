package org.jboss.maven.plugin.docbook.gen.xslt.resolve;

import java.net.URL;
import java.io.IOException;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import org.jboss.maven.plugin.docbook.gen.util.ResourceHelper;

/**
 * {@inheritDoc}
 *
 * @author Steve Ebersole
 */
public class VersionResolver implements URIResolver {
	public static final String BASE_HREF = "http://docbook.sourceforge.net/release/xsl/";

	private final String version;
	private final String versionHref;

	public VersionResolver(String version) {
		this.version = version;
		this.versionHref = BASE_HREF + version;
	}

	public Source resolve(String href, String base) throws TransformerException {
		if ( href.startsWith( versionHref ) ) {
			return resolve( href );
		}
		else if ( null != base && base.startsWith( versionHref ) ) {
			return resolve( base + "/" + href );
		}
		return null;
	}

	private Source resolve(String href) {
		String resource = href.substring( versionHref.length() );
		try {
			URL resourceURL = ResourceHelper.requireResource( resource );
			return new StreamSource( resourceURL.openStream(), resourceURL.toExternalForm() );
		}
		catch ( IllegalArgumentException e ) {
			return null;
		}
		catch ( IOException e ) {
			return null;
		}
	}

	public String toString() {
		return super.toString() + " [version=" + version + "]";
	}
}
