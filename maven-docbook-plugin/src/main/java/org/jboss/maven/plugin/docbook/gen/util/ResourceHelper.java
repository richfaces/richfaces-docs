package org.jboss.maven.plugin.docbook.gen.util;

import java.net.URL;

/**
 * Simple helpers for locating and handling classpath resource lookups.
 *
 * @author Steve Ebersole
 */
public class ResourceHelper {
	public static URL requireResource(String name) {
		URL resource = locateResource( name );
		if ( resource == null ) {
			throw new IllegalArgumentException( "could not locate resource [" + name + "]" );
		}
		return resource;
	}

	public static URL locateResource(String name) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if ( loader == null ) {
			loader = ResourceHelper.class.getClassLoader();
		}
		return loader.getResource( name );
	}
}
