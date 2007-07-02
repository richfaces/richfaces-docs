package org.jboss.maven.plugin.docbook.revdiff;

import java.util.Locale;
import java.util.List;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.reporting.MavenReportException;

/**
 * {@inheritDoc}
 *
 * @author Steve Ebersole
 */
public class IndexReportGenerator {
	private final ResourceBundle bundle;
	private final Sink sink;
	private final Log log;

	public static class TranslationReportDescriptor {
		private final Locale translation;
		private final String reportPath;

		public TranslationReportDescriptor(Locale translation, String reportPath) {
			this.translation = translation;
			this.reportPath = reportPath;
		}
	}

	public IndexReportGenerator(ResourceBundle bundle, Sink sink, Log log) {
		this.bundle = bundle;
		this.sink = sink;
		this.log = log;
	}

	public void generate(List reportDescriptors) throws MavenReportException {
		log.debug( "writing index report via provided sink" );
		sink.head();
		sink.text( bundle.getString( "report.name" ) );
        sink.head_();

        sink.body();
		sink.sectionTitle1();
        sink.anchor( bundle.getString( "report.header" ) );
        sink.anchor_();
        sink.text( bundle.getString( "report.header" ) );
        sink.sectionTitle1_();

		sink.table();
		Iterator itr = reportDescriptors.iterator();
		while ( itr.hasNext() ) {
			final TranslationReportDescriptor descriptor = ( TranslationReportDescriptor ) itr.next();
			sink.tableRow();
			sink.tableCell();
			sink.link( descriptor.reportPath );
			sink.text( descriptor.translation.getDisplayName( Locale.ENGLISH ) + " (" + descriptor.translation.toString() + ")" );
			sink.link_();
			sink.tableCell_();
			sink.tableRow_();
		}
		sink.table_();

		sink.body_();
        sink.flush();
        sink.close();
		log.debug( "index report sink flushed and closed" );
	}
}
