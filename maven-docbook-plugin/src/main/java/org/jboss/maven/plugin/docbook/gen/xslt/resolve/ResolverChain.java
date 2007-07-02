package org.jboss.maven.plugin.docbook.gen.xslt.resolve;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.transform.URIResolver;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;

/**
 * Allows chaining a series of {@link URIResolver resolvers} together.
 * <p/>
 * "Precedence" of the resolvers is determined by the order in which
 * they are {@link #addResolver added}.
 *
 * @author Steve Ebersole
 */
public class ResolverChain implements URIResolver {
	private List resolvers = new ArrayList();

	public ResolverChain() {
	}

	public ResolverChain(URIResolver resolver) {
		this();
		addResolver( resolver );
	}

	/**
	 * Adds a resolver to the chain.
	 *
	 * @param resolver The resolver to add.
	 */
	public void addResolver(URIResolver resolver) {
		resolvers.add( resolver );
	}

	/**
	 * Here we iterate over all the chained resolvers and delegate to them
	 * until we find one which can handle the resolve request (if any).
	 *
	 * {@inheritDoc}
	 */
	public Source resolve(String href, String base) throws TransformerException {
		Source result = null;
		Iterator itr = resolvers.iterator();
		while ( itr.hasNext() ) {
			final URIResolver resolver = ( URIResolver ) itr.next();
			result = resolver.resolve( href, base );
			if ( result != null ) {
				break;
			}
		}
		return result;
	}
}
