package org.jboss.maven.plugin.docbook.gen.render;

import java.io.File;

import org.codehaus.plexus.util.FileUtils;
import org.jboss.maven.plugin.docbook.gen.Format;
import org.jboss.maven.plugin.docbook.gen.util.FormatType;

/**
 * Defined formatting information.  Basically stuff to either pass into the xsl
 * transformer or stuff needed to prepare for transformation.
 *
 * @author Steve Ebersole
 */
public class Formatting {
	// todo : the usage of 'FormatType' here are ok.

	private final String formatName;
	private final String stylesheetResource;
	private final boolean imagePathSettingRequired;
	private final boolean imageCopyingRequired;
	private final boolean doingChunking;
	private final FormatType standardDocBookSpec;
	private final TargetNamingStrategy namingStrategy;

	public Formatting(FormatType standardDocBookSpec, Format userSpec) {
		// todo : there may not be matching standard docbook formatter...
		if ( ! standardDocBookSpec.getName().equals( userSpec.getFormatName() ) ) {
			throw new IllegalArgumentException( "formatting type mismatch" );
		}

		this.standardDocBookSpec = standardDocBookSpec;
		this.formatName = standardDocBookSpec.getName();

		this.stylesheetResource = userSpec.getStylesheetResource() == null
				? standardDocBookSpec.getStylesheetResource()
				: userSpec.getStylesheetResource();

		this.imageCopyingRequired = userSpec.getImageCopyingRequired() == null
				? standardDocBookSpec.isImageCopyingRequired()
				: userSpec.getImageCopyingRequired().booleanValue();

		this.imagePathSettingRequired = userSpec.getImagePathSettingRequired() == null
				? standardDocBookSpec.isImagePathSettingRequired()
				: userSpec.getImagePathSettingRequired().booleanValue();

		this.doingChunking =userSpec.getDoingChunking() == null
				? standardDocBookSpec.isDoingChunking()
				: userSpec.getDoingChunking().booleanValue();

		this.namingStrategy = new TargetNamingStrategy( standardDocBookSpec, userSpec );
	}

	public String getFormatName() {
		return formatName;
	}

	public FormatType getStandardDocBookSpec() {
		return standardDocBookSpec;
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

	public TargetNamingStrategy getNamingStrategy() {
		return namingStrategy;
	}

	public static class TargetNamingStrategy {
		private String targetFileExtension;
		private String finalName;

		public TargetNamingStrategy(FormatType standardDocBookSpec, Format userSpec) {
			if ( userSpec.getFinalName() != null ) {
				this.targetFileExtension = null;
				this.finalName = userSpec.getFinalName();
			}
			else {
				this.targetFileExtension = userSpec.getTargetFileExtension() == null
						? standardDocBookSpec.getStandardFileExtension()
						: userSpec.getTargetFileExtension();
				this.finalName = null;
			}
		}

		public String deduceTargetFileName(File source) {
			return finalName == null
				? FileUtils.basename( source.getAbsolutePath() ) + targetFileExtension
				: finalName;
		}
	}
}
