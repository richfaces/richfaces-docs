<?xml version="1.0" encoding="UTF-8"?>
<xsl:transform xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
               xmlns:javaee="http://java.sun.com/JSP/TagLibraryDescriptor"
               version="2.0" exclude-result-prefixes="javaee">
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
    <xsl:param name="lang" />
    
    <xsl:template match="javaee:taglib | taglib">
        <xsl:variable name="excluded-tag-names">header2,header3,header4,header5,header6</xsl:variable>
        
       <xsl:for-each select="javaee:tag | tag">
	       <xsl:sort select="name" />
		   <xsl:sort select="javaee:name" />
	   <!--xsl:value-of select="./name/text()" /-->

		   <xsl:if test="not(contains($excluded-tag-names, javaee:name))">
				<xsl:call-template name="tag" />
		   </xsl:if>
		   <xsl:if test="not(contains($excluded-tag-names, ./name/text()))">
				<xsl:call-template name="tag" />
		   </xsl:if>
       </xsl:for-each>
    </xsl:template>

	<xsl:template name="tag">
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
       
       <section id="tag_name">
        	<xsl:attribute name="id"><xsl:value-of select="$tag_name"/></xsl:attribute>
	   <xsl:processing-instruction name="dbhtml">
	   		<xsl:text>filename="</xsl:text><xsl:value-of select="$tag_name" /><xsl:text>.html" </xsl:text>
	   </xsl:processing-instruction>
            <title>&lt;<xsl:value-of select="concat('rich:', $tag_name)" />&gt;</title>
			<xsl:for-each select="document(concat($lang, '/included/',$tag_name, '.desc.xml'))/*">
                <xsl:copy-of select="./*"/>
            </xsl:for-each>
			<table>
                <title>rich:<xsl:value-of select="javaee:name"/><xsl:value-of select="name"/> attributes</title>
                <tgroup cols="2">
                    <thead>
                        <row>
                            <entry>Attribute Name</entry>
                            <entry>Description</entry>
                        </row>
                    </thead>                
                <tbody>
                    <xsl:for-each select="javaee:attribute | attribute">
                        <xsl:sort select="name" />
                        <xsl:sort select="javaee:name" />
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
 			<xsl:for-each select="document(concat($lang, '/included/',$tag_name, '.xml'))/*">
                <xsl:copy-of select="./*"/>
            </xsl:for-each>
        </section>
	</xsl:template>
</xsl:transform>