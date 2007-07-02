package org.jboss.maven.plugin.docbook.gen;

/**
 * Represents a user format specification.
 *
 * @author Steve Ebersole
 */
public class Format {
	private String formatName;

	private String targetFileExtension;
	private String finalName;
	private String stylesheetResource;
	private Boolean imagePathSettingRequired;
	private Boolean imageCopyingRequired;
	private Boolean doingChunking;

	public Format() {
	}

	public Format(
			String formatName,
			String targetFileExtension,
			String finalName,
			String stylesheetResource,
			Boolean imagePathSettingRequired,
			Boolean imageCopyingRequired,
			Boolean doingChunking) {
		this.formatName = formatName;
		this.targetFileExtension = targetFileExtension;
		this.finalName = finalName;
		this.stylesheetResource = stylesheetResource;
		this.imagePathSettingRequired = imagePathSettingRequired;
		this.imageCopyingRequired = imageCopyingRequired;
		this.doingChunking = doingChunking;
	}

	public String getFormatName() {
		return formatName;
	}

	public String getTargetFileExtension() {
		return targetFileExtension;
	}

	public String getFinalName() {
		return finalName;
	}

	public String getStylesheetResource() {
		return stylesheetResource;
	}

	public Boolean getImagePathSettingRequired() {
		return imagePathSettingRequired;
	}

	public Boolean getImageCopyingRequired() {
		return imageCopyingRequired;
	}

	public Boolean getDoingChunking() {
		return doingChunking;
	}
}
