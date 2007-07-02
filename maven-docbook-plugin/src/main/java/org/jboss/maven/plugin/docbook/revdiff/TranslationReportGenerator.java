package org.jboss.maven.plugin.docbook.revdiff;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.FileUtils;

/**
 * Generate a diff report.
 *
 * @author Christian Bauer
 * @author Steve Ebersole
 */
public class TranslationReportGenerator {
	// todo : use templating...
	private final String base;
	private final ResourceBundle bundle;
	private final Log log;

	public TranslationReportGenerator(File basedir, ResourceBundle bundle, Log log) {
		try {
			this.base = basedir.toURL().toExternalForm();
		}
		catch ( MalformedURLException e ) {
			throw new IllegalArgumentException( "basedir was a malformed url" );
		}
		log.debug( "constructing docbook diff report generator [base=" + base + "]" );
		this.bundle = bundle;
		this.log = log;
	}

	public void generate(Diff diff, File output, String translation) throws GenerationException {
		if ( output.exists() ) {
			log.debug( "cleaning up previous translation-diff report output [" + output.getAbsolutePath() + "]" );
			output.delete();
		}

		File outputDirectory = output.getParentFile();
		if ( !outputDirectory.exists() ) {
			String outputDirectoryPath = outputDirectory.getAbsolutePath();
			log.debug( "creating translation-diff report directory [" + outputDirectoryPath + "]" );
			FileUtils.mkdir( outputDirectoryPath );
		}

		try {
			output.createNewFile();
		}
		catch ( IOException e ) {
			throw new GenerationException( "unable to create output file [" + output.getAbsolutePath() + "]", e );
		}

		try {
			FileWriter writer = new FileWriter( output );
			writer.write( pageHead( translation ) );

			writer.write( "<table cellpadding=4>" );

			writer.write( generateNewInMaster( diff ) );
			writer.write( "<tr><td>&nbsp;</td></tr>" );
			writer.write( generateDiffRevision( diff ) );
			writer.write( "<tr><td>&nbsp;</td></tr>" );
			writer.write( generateNewInTranslation( diff ) );

			writer.write( "</table>" );
			writer.write( pageFoot() );
			writer.close();
		}
		catch ( IOException e ) {
			throw new GenerationException( "error writing report [" + output.getAbsolutePath() + "]", e );
		}
	}

	private String pageHead(String translation) {
		StringBuffer buf = new StringBuffer( 256 );
		buf.append( "<html><head><title>" ).append( bundle.getString( "report.name" ) ).append( "</title></head><body><div align=\"center\">" );
		buf.append( "<h3>" ).append( bundle.getString( "report.header") ).append( " - " ).append( translation ).append( "</h3>" );
		return buf.toString();
	}

	private String pageFoot() {
		StringBuffer buf = new StringBuffer( 256 );
		buf.append( "</div></body></html>" );
		return buf.toString();
	}

	private String generateNewInMaster(Diff diff) {
		StringBuffer buf = new StringBuffer();
		Iterator itr = diff.getElementsOnlyInMaster();
		while ( itr.hasNext() ) {
			final ContentItem contentItem = ( ContentItem ) itr.next();
			buf.append( "<tr><td bgcolor=#eeeeee>ID: " ).append( contentItem.getIdentifier() ).append( "</td></tr>" );
			buf.append( "<tr><td>" );
			buf.append( "<b>" ).append( formatReference( contentItem.getMasterDescriptor() ) ).append( "</b>" );
			buf.append( "</td></tr>" );
		}

		String result = "";
		if ( buf.length() > 0 ) {
			result = "<tr bgcolor=#cccccc><td><b>New in master:</b></td></tr>";
		}
		return result + buf.toString();
	}

	private String generateDiffRevision(Diff diff) {
		StringBuffer buf = new StringBuffer();
		Iterator itr = diff.getElementsDiffRevision();
		while ( itr.hasNext() ) {
			final ContentItem contentItem = ( ContentItem ) itr.next();
			buf.append( "<tr><td bgcolor=#eeeeee>ID: " ).append( contentItem.getIdentifier() ).append( "</td></tr>" );
			if ( !contentItem.getMasterDescriptor().getElementName().equals( contentItem.getTranslationDescriptor().getElementName() ) ) {
				buf.append( "<tr><td bgcolor=#ee6666>" );
				buf.append( "Element names differ: " )
						.append( contentItem.getMasterDescriptor().getElementName() )
						.append( "/" )
						.append( contentItem.getTranslationDescriptor().getElementName() );
				buf.append( "</td></tr>" );
			}
			buf.append( "<tr><td>" );
			buf.append( "Master: <b>" ).append( formatReference( contentItem.getMasterDescriptor() ) ).append( "</b>" );
			buf.append( "</td></tr>" );
			buf.append( "<tr><td>" );
			buf.append( "Translation: <b>" ).append( formatReference( contentItem.getTranslationDescriptor() ) ).append( "</b>" );
			buf.append( "</td></tr>" );
			buf.append( "<tr><td>" );
			buf.append( "Master Revision: " ).append( contentItem.getMasterDescriptor().getRevision() );
			buf.append( "</td></tr>" );
			buf.append( "<tr><td>" );
			buf.append( "Translation Revision: " ).append( contentItem.getTranslationDescriptor().getRevision() );
			buf.append( "</td></tr>" );
		}

		String result = "";
		if ( buf.length() > 0 ) {
			result = "<tr bgcolor=#cccccc><td><b>Translation needs update:</b></td></tr>";
		}
		return result + buf.toString();
	}

	private String generateNewInTranslation(Diff diff) {
		StringBuffer buf = new StringBuffer();
		Iterator itr = diff.getElementsOnlyInTranslation();
		while ( itr.hasNext() ) {
			final ContentItem contentItem = ( ContentItem ) itr.next();
			buf.append( "<tr><td bgcolor=#eeeeee>ID: " ).append( contentItem.getIdentifier() ).append( "</td></tr>" );
			buf.append( "<tr><td>" );
			buf.append( "<b>" ).append( formatReference( contentItem.getTranslationDescriptor() ) ).append( "</b>" );
			buf.append( "</td></tr>" );
		}
		String result = "";
		if ( buf.length() > 0 ) {
			result = "<tr bgcolor=#cccccc><td><b>Only present in translation:</b></td></tr>";
		}
		return result + buf.toString();
	}

	private String formatReference(ContentItemDescriptor descriptor) {
		return makeSystemIdRelative( descriptor.getSourceSystemId() ) +
				":" + descriptor.getElementName() +
				" (" + descriptor.getRow() + "," + descriptor.getColumn() + ")";
	}

	private String makeSystemIdRelative(String systemId) {
		String systemIdUrl = toURL( systemId ).toExternalForm();
		log.debug( "checking source [" + systemIdUrl + "] against base [" + base + "]" );
		if ( systemIdUrl.startsWith( base ) ) {
			return systemIdUrl.substring( base.length() );
		}
		else {
			return systemIdUrl;
		}
	}

	private URL toURL(String systemId) {
		try {
			return new URL( systemId );
		}
		catch ( MalformedURLException e ) {
			throw new UnsupportedOperationException( "unable to parse systemId [" + systemId + "] as URL" );
		}
	}
}
