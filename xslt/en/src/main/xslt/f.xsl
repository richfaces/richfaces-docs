<?xml version="1.0" encoding="UTF-8"?>
<xsl:transform xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:javaee="http://java.sun.com/xml/ns/javaee"
	version="1.0" exclude-result-prefixes="javaee">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"
		omit-xml-declaration="yes" />
	<xsl:param name="lang" />
	<xsl:param name="title" />
	<xsl:param name="separator" />
	<xsl:param name="prefix" />

	<xsl:template match="javaee:taglib | taglib">
		<xsl:variable name="excluded-tag-names">
			header2,header3,header4,header5,header6
		</xsl:variable>

		<xsl:for-each select="javaee:tag | tag">

			<!--xsl:value-of select="./name/text()" /-->

			<xsl:if
				test="not(contains($excluded-tag-names, javaee:name))">
				<xsl:call-template name="tag" />
			</xsl:if>
			<xsl:if
				test="not(contains($excluded-tag-names, ./name/text()))">
				<xsl:call-template name="tag" />
			</xsl:if>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="tag">
		<section role="NotInToc">
			<xsl:variable name="tag_name">
				<xsl:choose>
					<xsl:when test="javaee:name">
						<xsl:value-of select="javaee:name" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="./name/text()" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<!--
			<xsl:variable name="prefix">
				<xsl:choose>
					<xsl:when test="/javaee:taglib/javaee:short-name">
						<xsl:value-of select="/javaee:taglib/javaee:short-name" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="/taglib/short-name/text()" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>			
			-->
			<xsl:attribute name="id"><xsl:value-of select="$tag_name" />
			</xsl:attribute>
			<title>
				&lt;
				<xsl:value-of select="concat($prefix,':', $tag_name)" />
				&gt;
			</title>
			<xsl:for-each
				select="document(concat($lang, $separator,'included',$separator,$tag_name, '.desc.xml'))/*">
				<xsl:copy-of select="./*" />
			</xsl:for-each>
			<table>
				<title>
					<xsl:value-of select="$prefix" />
					:
					<xsl:value-of select="javaee:name" />
					<xsl:value-of select="name" />
					attributes
				</title>
				<tgroup cols="2">
					<thead>
						<row>
							<entry>Attribute Name</entry>
							<entry>Description</entry>
						</row>
					</thead>
					<tbody>
						<xsl:for-each select="javaee:attribute | attribute">
							<xsl:sort select="javaee:name|name" />
							<row>
								<entry>
									<xsl:value-of select="javaee:name"/>
									<xsl:value-of select="name"/>
								</entry>
								<entry>
									<xsl:value-of select="javaee:description" disable-output-escaping="yes"/>
									<xsl:value-of select="description" disable-output-escaping="yes"/>
								</entry>
							</row>
						</xsl:for-each>
					</tbody>
				</tgroup>
			</table>
			<xsl:for-each
				select="document(concat($lang, $separator,'included',$separator,$tag_name, '.xml'))/*">
				<xsl:copy-of select="./*" />
			</xsl:for-each>
		</section>
	</xsl:template>
</xsl:transform>