<?xml version='1.0'?>

<!--
   Copyright 2008 JBoss, a division of Red Hat
   License: LGPL
   Author: Mark Newton <mark.newton@jboss.org>
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"  xmlns:date="http://exslt.org/dates-and-times" exclude-result-prefixes="date">

   <xsl:import href="collapsing-navigation.xsl"/>
   <xsl:param name="generate.toc" select="'book toc'"/>
   <xsl:param name="toc.section.depth" select="5"/>
	<!--
From: xhtml/docbook.xsl
Reason: Remove inline style for draft mode
Version: 1.72.0
-->
	
<xsl:template name="head.content">
	<xsl:param name="node" select="."/>
	<xsl:param name="title">
		<xsl:apply-templates select="$node" mode="object.title.markup.textonly"/>
	</xsl:param>

	<title xmlns="http://www.w3.org/1999/xhtml" >
		<xsl:copy-of select="$title"/>
	</title>

	<xsl:if test="$html.stylesheet != ''">
		<xsl:call-template name="output.html.stylesheets">
			<xsl:with-param name="stylesheets" select="normalize-space($html.stylesheet)"/>
		</xsl:call-template>
	</xsl:if>

	<xsl:if test="$link.mailto.url != ''">
		<link rev="made" href="{$link.mailto.url}"/>
	</xsl:if>

	<xsl:if test="$html.base != ''">
		<base href="{$html.base}"/>
	</xsl:if>

	<meta xmlns="http://www.w3.org/1999/xhtml" name="generator" content="DocBook {$DistroTitle} V{$VERSION}"/>

	<xsl:if test="$generate.meta.abstract != 0">
		<xsl:variable name="info" select="(articleinfo |bookinfo |prefaceinfo |chapterinfo |appendixinfo |sectioninfo |sect1info |sect2info |sect3info |sect4info |sect5info |referenceinfo |refentryinfo |partinfo |info |docinfo)[1]"/>
		<xsl:if test="$info and $info/abstract">
			<meta xmlns="http://www.w3.org/1999/xhtml" name="description">
				<xsl:attribute name="content">
					<xsl:for-each select="$info/abstract[1]/*">
						<xsl:value-of select="normalize-space(.)"/>
						<xsl:if test="position() &lt; last()">
							<xsl:text> </xsl:text>
						</xsl:if>
					</xsl:for-each>
				</xsl:attribute>
			</meta>
		</xsl:if>
	</xsl:if>
	
	<link rel="shortcut icon"  type="image/vnd.microsoft.icon" href="images/favicon.ico" />

	<xsl:apply-templates select="." mode="head.keywords.content"/>
			<script type="text/javascript" src="script/prototype-1.6.0.2.js"><xsl:comment>If you see this message, your web browser doesn't support JavaScript or JavaScript is disabled.</xsl:comment></script>
		<script type="text/javascript" src="script/effects.js"><xsl:comment>If you see this message, your web browser doesn't support JavaScript or JavaScript is disabled.</xsl:comment></script>
		<script type="text/javascript" src="script/scriptaculous.js"><xsl:comment>If you see this message, your web browser doesn't support JavaScript or JavaScript is disabled.</xsl:comment></script>

</xsl:template>

<xsl:template match="abstract" mode="titlepage.mode">
	<div id="timestamp">
		<xsl:text>Last published: </xsl:text>
		<xsl:call-template name="datetime.format">
			<xsl:with-param name="date" select="date:date-time()"/>
			<xsl:with-param name="format" select="'B d, Y'"/>
		</xsl:call-template>
	</div>
	<div>
	    <xsl:apply-templates select="." mode="class.attribute"/>
	    <xsl:apply-templates mode="titlepage.mode"/>
	  </div>
</xsl:template>
</xsl:stylesheet>
