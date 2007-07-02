package org.jboss.maven.plugin.docbook.gen.xslt.resolve;

/**
 * Map hrefs starting with <tt>http://docbook.sourceforge.net/release/xsl/current/</tt>
 * to classpath resource lookups.
 *
 * @author Steve Ebersole
 */
public class CurrentVersionResolver extends VersionResolver {

	public CurrentVersionResolver() {
		super( "current" );
	}
}
