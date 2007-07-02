package org.jboss.maven.plugin.docbook.gen.util;

/**
 * An enumeration of the various types of formatting supported in this plugin.
 * <p/>
 * DocBook does define some other output formatting support (like HTML Help),
 * but those are not covered nor supported by this plugin.
 * <p/>
 * Really, instead of a discrete enumeration, these should act as templates
 * based on the docbook standard formats.  The attributes here should be exposed
 * to the user for configuration, thesse attributes values acting as defaults
 * based on the selected format-name.
 *
 * @author Steve Ebersole
 */
public class FormatType {
	public static final FormatType ECLIPSE =
			new HtmlBasedFormat( "eclipse", "/eclipse/eclipse.xsl" );

	public static final FormatType HTML =
			new HtmlBasedFormat( "html", "/html/chunk.xsl" );

	public static final FormatType HTML_SINGLE =
			new HtmlBasedFormat( "html_single", "/html/docbook.xsl", false );

	public static final FormatType HTMLHELP =
			new HtmlBasedFormat( "htmlhelp", "/htmlhelp/htmlhelp.xsl" );

	public static final FormatType JAVAHELP =
			new HtmlBasedFormat( "javahelp", "/javahelp/javahelp.xsl" );

	public static final FormatType MAN =
			new HtmlBasedFormat( "man", "/manpages/docbook.xsl", false );

	public static final FormatType PDF =
			new FormatType( "pdf", "pdf", "/fo/docbook.xsl", true, false, false );

	public static final FormatType WEBSITE =
			new HtmlBasedFormat( "website", "/website/website.xsl", false );

// I'd rather not support this...
//	public static final FormatType WORDML =
//			new FormatType( "wordml", "doc", "/wordml/wordml.xsl", ?, ?, ? );

	public static final FormatType XHTML =
			new FormatType( "xhtml", "xhtml", "/xhtml/docbook.xsl", false, true, false );

	private final String name;
	private final String standardFileExtension;
	private final String stylesheetResource;
	private final boolean imagePathSettingRequired;
	private final boolean imageCopyingRequired;
	private final boolean doingChunking;

	public FormatType(
			String name,
			String standardFileExtension,
			String stylesheetResource,
			boolean imagePathSettingRequired,
			boolean imageCopyingRequired,
			boolean doingChunking) {
		this.name = name;
		this.standardFileExtension = standardFileExtension;
		this.stylesheetResource = stylesheetResource;
		this.imagePathSettingRequired = imagePathSettingRequired;
		this.imageCopyingRequired = imageCopyingRequired;
		this.doingChunking = doingChunking;
	}

	public String getName() {
		return name;
	}

	public String getStandardFileExtension() {
		return standardFileExtension;
	}

	public String getStylesheetResource() {
		return stylesheetResource;
	}

	public boolean isImagePathSettingRequired() {
		return imagePathSettingRequired;
	}

	public boolean isImageCopyingRequired() {
		return imageCopyingRequired;
	}

	public boolean isDoingChunking() {
		return doingChunking;
	}

	public static FormatType parse(String name) {
		if ( ECLIPSE.name.equals( name ) ) {
			return ECLIPSE;
		}
		else if ( HTML.name.equals( name ) ) {
			return HTML;
		}
		else if ( HTML_SINGLE.name.equals( name ) ) {
			return HTML_SINGLE;
		}
		else if ( HTMLHELP.name.equals( name ) ) {
			return HTMLHELP;
		}
		else if ( JAVAHELP.name.equals( name ) ) {
			return JAVAHELP;
		}
		else if ( MAN.name.equals( name ) ) {
			return MAN;
		}
		else if ( PDF.name.equals( name ) ) {
			return PDF;
		}
		else if ( WEBSITE.name.equals( name ) ) {
			return WEBSITE;
		}
//		else if ( WORDML.name.equals( name ) ) {
//			return WORDML;
//		}
		else if ( XHTML.name.equals( name ) ) {
			return XHTML;
		}
		else {
			return null;
		}
	}


	// convenience for html based formats to simplify ctors ~~~~~~~~~~~~~~~~~~~

	private static class HtmlBasedFormat extends FormatType {
		private HtmlBasedFormat(String name, String stylesheetResource) {
			this( name, stylesheetResource, true );
		}
		private HtmlBasedFormat(String name, String stylesheetResource, boolean doingChunking) {
			super( name, "html", stylesheetResource, false, true, doingChunking );
		}
	}
}
