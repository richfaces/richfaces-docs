package org.jboss.maven.plugin.docbook.gen.xslt.resolve;

import javax.xml.transform.URIResolver;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;

/**
 * Basic support for URIResolvers which map a URN unto a single replacement
 * {@link Source}.
 *
 * @author Steve Ebersole
 */
public class BasicUrnResolver implements URIResolver {
	private final String urn;
	private final Source source;

	public BasicUrnResolver(String urn, Source source) {
		this.urn = urn;
		this.source = source;
	}

	public Source resolve(String href, String base) throws TransformerException {
		return urn.equals( href ) ? source : null;
	}

	public String toString() {
		return super.toString() + " [URN:" + urn + "]";
	}
}
