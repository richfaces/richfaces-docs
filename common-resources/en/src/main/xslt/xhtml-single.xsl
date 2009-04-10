<?xml version='1.0'?>

<!--
   Copyright 2008 JBoss, a division of Red Hat
   License: LGPL
   Author: Mark Newton <mark.newton@jboss.org>
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:import href="classpath:/xslt/org/jboss/xhtml-single.xsl"/>
<xsl:import href="xhtml-common.xsl"/>
<xsl:param name="html.stylesheet" select="'css/html.css'"/>

<xsl:template name="book.titlepage.recto">
	<xsl:call-template name="book.titlepage.recto.singlePage">
		 <xsl:with-param name="nightly" select="1" />
	</xsl:call-template>
</xsl:template>

</xsl:stylesheet>
