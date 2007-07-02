package org.jboss.maven.plugin.docbook.gen.xslt.catalog;

import org.apache.xml.resolver.CatalogManager;

/**
 * Basic support for our notion of CatalogManagers.
 *
 * @author Steve Ebersole
 */
public abstract class AbstractCatalogManager extends CatalogManager {
	public AbstractCatalogManager(String[] catalogNames) {
		super();
		setIgnoreMissingProperties( true );
		if ( catalogNames != null && catalogNames.length != 0 ) {
			StringBuffer buffer = new StringBuffer();
			boolean first = true;
			for ( int i = 0; i < catalogNames.length; i++ ) {
				if ( catalogNames[i] != null ) {
					if ( first ) {
						first = false;
					}
					else {
						buffer.append( ';' );
					}
				}
				buffer.append( catalogNames[i] );
			}
			setCatalogFiles( buffer.toString() );
		}
	}
}
