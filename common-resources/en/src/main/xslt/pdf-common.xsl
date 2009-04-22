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

   <!--avoid page sequence  to generate blank pages after even page numbers -->
   
   <xsl:template name="force.page.count">
      <xsl:param name="element" select="local-name(.)"/>
      <xsl:param name="master-reference" select="''"/>
      <xsl:text>no-force</xsl:text>
   </xsl:template>
   
</xsl:stylesheet>
