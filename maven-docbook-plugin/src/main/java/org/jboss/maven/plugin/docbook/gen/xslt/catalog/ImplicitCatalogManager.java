package org.jboss.maven.plugin.docbook.gen.xslt.catalog;

import java.util.Enumeration;
import java.util.ArrayList;
import java.net.URL;
import java.io.IOException;

/**
 * CatalogManager which resolves its catalogs internally via  classpath
 * resource lookups.  Its looks for resources named '/catalog.xml' on the
 * classpath.
 *
 * @author Steve Ebersole
 */
public class ImplicitCatalogManager extends AbstractCatalogManager {
	public ImplicitCatalogManager() {
		super( resolveCatalogNames() );
	}

	private static String[] resolveCatalogNames() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if ( classLoader == null ) {
			classLoader = ImplicitCatalogManager.class.getClassLoader();
		}
		ArrayList names = new ArrayList();
        try {
            Enumeration enumeration = classLoader.getResources( "/catalog.xml" );
            while ( enumeration.hasMoreElements() ) {
				final URL resource = ( URL ) enumeration.nextElement();
				final String resourcePath = resource.toExternalForm();
				if ( resourcePath != null ) {
					names.add( resourcePath );
				}
            }
        }
		catch ( IOException ignore ) {
			// intentionally empty
		}
		return ( String[] ) names.toArray( new String[ names.size() ] );
	}
}
