package org.jboss.maven.plugin.docbook.gen.xslt;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import com.icl.saxon.Controller;
import org.apache.xml.resolver.tools.CatalogResolver;
import org.jboss.maven.plugin.docbook.gen.render.Formatting;
import org.jboss.maven.plugin.docbook.gen.util.FormatType;
import org.jboss.maven.plugin.docbook.gen.util.NoOpWriter;
import org.jboss.maven.plugin.docbook.gen.util.ResourceHelper;
import org.jboss.maven.plugin.docbook.gen.util.TransformerType;
import org.jboss.maven.plugin.docbook.gen.xslt.resolve.CurrentVersionResolver;
import org.jboss.maven.plugin.docbook.gen.xslt.resolve.ExplicitUrnResolver;
import org.jboss.maven.plugin.docbook.gen.xslt.resolve.RelativeJarUriResolver;
import org.jboss.maven.plugin.docbook.gen.xslt.resolve.ResolverChain;
import org.jboss.maven.plugin.docbook.gen.xslt.resolve.VersionResolver;

/**
 * A factory for {@link javax.xml.transform.Transformer} instances, configurable
 * to return either SAXON or XALAN based transformers.
 *
 * @author Steve Ebersole
 */
public class TransformerFactory {
	private final TransformerType transformerType;
	private final Properties transformerParameters;
	private final CatalogResolver catalogResolver;
	private final String docbookVersion;

	public TransformerFactory(
			TransformerType transformerType,
			Properties transformerParameters,
			CatalogResolver catalogResolver, String docbookVersion) {
		this.transformerType = transformerType;
		this.transformerParameters = transformerParameters;
		this.catalogResolver = catalogResolver;
		this.docbookVersion = docbookVersion;
	}

	public Transformer buildTransformer(Formatting formatting, URL customStylesheet) throws XSLTException {
		URIResolver uriResolver = buildUriResolver( formatting.getStandardDocBookSpec() );

		javax.xml.transform.TransformerFactory transformerFactory = buildTransformerFactory();
		transformerFactory.setURIResolver( uriResolver );

		URL xsltStylesheet = customStylesheet == null
				? ResourceHelper.requireResource( formatting.getStylesheetResource() )
				: customStylesheet;

		Transformer transformer;
		try {
			Source source = new StreamSource( xsltStylesheet.openStream(), xsltStylesheet.toExternalForm() );
			transformer = transformerFactory.newTransformer( source );
		}
		catch ( IOException e ) {
			throw new XSLTException( "problem opening stylesheet", e );
		}
		catch ( TransformerConfigurationException e ) {
			throw new XSLTException( "unable to build transformer", e );
		}

		transformer.setURIResolver( uriResolver );
		applyParameters( transformer );

		if ( transformer instanceof Controller ) {
			Controller controller = ( Controller ) transformer;
			try {
				controller.makeMessageEmitter();
				controller.getMessageEmitter().setWriter( new NoOpWriter() );
			}
			catch ( TransformerException te ) {
				// intentionally empty
			}
		}
		return transformer;
	}

	private javax.xml.transform.TransformerFactory buildTransformerFactory() {
		if ( transformerType == TransformerType.XALAN ) {
			return new com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl();
		}
		else {
			// saxon as default...
			return new com.icl.saxon.TransformerFactoryImpl();
		}
	}

	private void applyParameters(Transformer transformer) {
		if ( transformerParameters == null ) {
			return;
		}
		Iterator itr = transformerParameters.entrySet().iterator();
		while ( itr.hasNext() ) {
			final Map.Entry entry = ( Map.Entry ) itr.next();
			transformer.setParameter( ( String ) entry.getKey(), entry.getValue() );
		}
	}

	public URIResolver buildUriResolver(FormatType formatType) throws XSLTException {
		ResolverChain resolverChain = new ResolverChain();
		if ( formatType != null ) {
			resolverChain.addResolver( new ExplicitUrnResolver( formatType ) );
		}
		resolverChain.addResolver( new CurrentVersionResolver() );
		if ( docbookVersion != null ) {
			resolverChain.addResolver( new VersionResolver( docbookVersion ) );
		}
		resolverChain.addResolver( new RelativeJarUriResolver() );
		resolverChain.addResolver( catalogResolver );
		return resolverChain;
	}
}
