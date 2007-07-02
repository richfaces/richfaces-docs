package org.jboss.maven.plugin.docbook.gen.xslt.catalog;


/**
 * Utilizes explicit, user-supplied catalog names to build a
 * CatalogManager.
 *
 * @author Steve Ebersole
 */
public class ExplicitCatalogManager extends AbstractCatalogManager {
	public ExplicitCatalogManager(String[] catalogNames) {
		super( catalogNames );
	}
}
