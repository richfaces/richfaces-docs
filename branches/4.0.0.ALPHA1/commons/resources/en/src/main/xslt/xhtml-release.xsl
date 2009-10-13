<?xml version='1.0'?>

<!--
   Copyright 2008 JBoss, a division of Red Hat
   License: LGPL
   Author: Mark Newton <mark.newton@jboss.org>
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="classpath:/xslt/org/jboss/xhtml.xsl"/>
	<xsl:import href="xhtml-common-reldiffmk.xsl"/>	  
	<xsl:param name="chunk.fast" select="1"/>
	<xsl:param name="html.stylesheet" select="'css/html-release.css'"/>
	<xsl:variable name="nightly" select="0" />
</xsl:stylesheet>
