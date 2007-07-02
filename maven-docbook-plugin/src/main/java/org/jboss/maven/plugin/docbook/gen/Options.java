package org.jboss.maven.plugin.docbook.gen;

import java.util.Properties;

/**
 * A (detachable) representation of the user configuration.
 *
 * @author Steve Ebersole
 */
public class Options {
    private boolean xincludeSupported;
	private String[] catalogs;
	private String xmlTransformerType;
	private Properties transformerParameters;
	private boolean useRelativeImageUris = true;
	// TODO : try as hard as I possibly can to remove this :(
	// 		this is gettable either from (1) VERSION (fm:project/fm:Version) or
	// 		(2) the dependencies making up the execution classpath
	//
	//		In #1, it would require had parsing :(, and #2 I've yet to actually
	//		find a wsay to do this...
	private String docbookVersion;

	public Options() {
	}

	public Options(
			boolean xincludeSupported,
			String[] catalogs,
			String xmlTransformerType,
			Properties transformerParameters,
			boolean useRelativeImageUris,
			String docBookVersion) {
		this.xincludeSupported = xincludeSupported;
		this.catalogs = catalogs;
		this.xmlTransformerType = xmlTransformerType;
		this.transformerParameters = transformerParameters;
		this.useRelativeImageUris = useRelativeImageUris;
		this.docbookVersion = docBookVersion;
	}

	public boolean isXincludeSupported() {
		return xincludeSupported;
	}

	public String[] getCatalogs() {
		return catalogs;
	}

	public String getXmlTransformerType() {
		return xmlTransformerType;
	}

	public Properties getTransformerParameters() {
		return transformerParameters;
	}

	public boolean isUseRelativeImageUris() {
		return useRelativeImageUris;
	}

	public String getDocbookVersion() {
		return docbookVersion;
	}
}
