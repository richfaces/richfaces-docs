<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		        version="1.0"
		        xmlns="http://www.w3.org/TR/xhtml1/transitional"
		        xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:jbh="java:org.jboss.highlight.renderer.FORenderer"
		        exclude-result-prefixes="jbh">

<xsl:import href="classpath:/xslt/org/jboss/pdf.xsl" />
<xsl:attribute-set name="book.titlepage.recto.style">
	<xsl:attribute name="font-family">
		<xsl:value-of select="$title.fontset"/>
	</xsl:attribute>
	<xsl:attribute name="color"><xsl:value-of select="$titlepage.color"/></xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="font-size">12pt</xsl:attribute>
	<xsl:attribute name="text-align">center</xsl:attribute>
</xsl:attribute-set>

   <!-- avoid page sequence  to generate blank pages after even page numbers -->
   
   <xsl:template name="force.page.count">
      <xsl:param name="element" select="local-name(.)"/>
      <xsl:param name="master-reference" select="''"/>
      <xsl:text>no-force</xsl:text>
   </xsl:template>
   
   <!-- adding corpauthor entry to the titlepage -->
   
   <xsl:template name="book.titlepage.recto">
      <xsl:choose>
         <xsl:when test="bookinfo/title">
            <xsl:apply-templates mode="book.titlepage.recto.auto.mode"
               select="bookinfo/title" />
         </xsl:when>
         
         <xsl:when test="info/title">
            <xsl:apply-templates mode="book.titlepage.recto.auto.mode"
               select="info/title" />
         </xsl:when>
         <xsl:when test="title">
            <xsl:apply-templates mode="book.titlepage.recto.auto.mode"
               select="title" />
         </xsl:when>
      </xsl:choose>
      
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode"
         select="bookinfo/issuenum" />
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode"
         select="info/issuenum" />
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode"
         select="issuenum" />
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/corpauthor"/>
      
      <xsl:choose>
         <xsl:when test="bookinfo/subtitle">
            <xsl:apply-templates mode="book.titlepage.recto.auto.mode"
               select="bookinfo/subtitle" />
         </xsl:when>
         <xsl:when test="info/subtitle">
            <xsl:apply-templates mode="book.titlepage.recto.auto.mode"
               select="info/subtitle" />
         </xsl:when>
         <xsl:when test="subtitle">
            <xsl:apply-templates mode="book.titlepage.recto.auto.mode"
               select="subtitle" />
         </xsl:when>
      </xsl:choose>
      
      <fo:block xsl:use-attribute-sets="book.titlepage.recto.style"
         font-size="14pt" space-before="15.552pt">
         <xsl:apply-templates mode="book.titlepage.recto.auto.mode"
            select="bookinfo/releaseinfo" />
      </fo:block>
      
      <fo:block text-align="center" space-before="15.552pt">
         <xsl:call-template name="person.name.list">
            <xsl:with-param name="person.list" select="bookinfo/authorgroup/author|bookinfo/authorgroup/corpauthor" />
            <xsl:with-param name="person.type" select="'author'"/>
         </xsl:call-template>
      </fo:block>
      
      <fo:block text-align="center" space-before="15.552pt">
         <xsl:call-template name="person.name.list">
            <xsl:with-param name="person.list" select="bookinfo/authorgroup/editor" />
            <xsl:with-param name="person.type" select="'editor'"/>
         </xsl:call-template>
      </fo:block>
      
      <fo:block text-align="center" space-before="15.552pt">
         <xsl:call-template name="person.name.list">
            <xsl:with-param name="person.list" select="bookinfo/authorgroup/othercredit" />
            <xsl:with-param name="person.type" select="'othercredit'"/>
         </xsl:call-template>
      </fo:block>
      
   </xsl:template>
   
   <xsl:template match="corpauthor" mode="book.titlepage.recto.mode">
      <fo:block>
         <xsl:apply-templates mode="book.titlepage.recto.mode"/>
      </fo:block>
   </xsl:template>
   
   <!-- overwriting the font-size of the subtitle on the titlepage -->
   
   <xsl:template match="subtitle" mode="book.titlepage.recto.auto.mode">
      <fo:block xmlns:fo="http://www.w3.org/1999/XSL/Format" xsl:use-attribute-sets="book.titlepage.recto.style" text-align="center" font-size="18pt" space-before="2pt" font-family="{$title.fontset}">
         <xsl:apply-templates select="." mode="book.titlepage.recto.mode"/>
      </fo:block>
   </xsl:template>
</xsl:stylesheet>
