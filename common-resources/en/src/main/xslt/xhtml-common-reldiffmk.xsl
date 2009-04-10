<?xml version='1.0'?>

<!--
   Copyright 2008 JBoss, a division of Red Hat
   License: LGPL
   Author: Mark Newton <mark.newton@jboss.org>
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"  xmlns:diffmk="http://diffmk.sf.net/ns/diff" xmlns:date="http://exslt.org/dates-and-times" exclude-result-prefixes="date">

   <xsl:import href="collapsing-navigation.xsl"/>
   <xsl:import href="xhtml-common.xsl"/>
   <xsl:param name="generate.toc" select="'book toc'"/>
   <xsl:param name="toc.section.depth" select="5"/>

<!--
From: xhtml/docbook.xsl
Reason: Remove inline style for draft mode
Version: 1.72.0
-->
	

 <!--                 Overriding toc.line                          -->
  <xsl:template name="toc.line"> 
    <xsl:param name="toc-context" select="."/> 
    <xsl:param name="depth" select="1"/> 
    <xsl:param name="depth.from.context" select="8"/> 
    
    <span> 
      <xsl:attribute name="class"><xsl:value-of select="local-name(.)"/></xsl:attribute> 
      
      <!-- * if $autotoc.label.in.hyperlink is zero, then output the label --> 
      <!-- * before the hyperlinked title (as the DSSSL stylesheet does) --> 
      <xsl:if test="$autotoc.label.in.hyperlink = 0"> 
        <xsl:variable name="label"> 
          <xsl:apply-templates select="." mode="label.markup"/> 
        </xsl:variable> 
        <xsl:copy-of select="$label"/> 
        <xsl:if test="$label != ''"> 
          <xsl:value-of select="$autotoc.label.separator"/> 
        </xsl:if> 
      </xsl:if> 
      
      <a> 
        <xsl:attribute name="href"> 
          <xsl:call-template name="href.target"> 
            <xsl:with-param name="context" select="$toc-context"/> 
            <xsl:with-param name="toc-context" select="$toc-context"/> 
          </xsl:call-template> 
        </xsl:attribute> 
        
        <xsl:choose> 
          <xsl:when test="@role='new' or @role='updated'"> 
            <xsl:attribute name="class"> 
              <xsl:value-of select="@role"/> 
            </xsl:attribute> 
          </xsl:when> 
	<!-- For mkdiff compatibility-->
	<xsl:when test="@revisionflag='added' or @revisionflag='changed'"> 
            <xsl:attribute name="class"> 
              <xsl:value-of select="@revisionflag"/> 
            </xsl:attribute>
        </xsl:when>
	<xsl:when test="@diffmk:change='added' or @diffmk:change='changed'"> 
            <xsl:attribute name="class"> 
              <xsl:value-of select="@diffmk:change"/> 
            </xsl:attribute>
        </xsl:when>
        </xsl:choose> 
        
        <!-- * if $autotoc.label.in.hyperlink is non-zero, then output the label --> 
        <!-- * as part of the hyperlinked title --> 
        <xsl:if test="not($autotoc.label.in.hyperlink = 0)"> 
          <xsl:variable name="label"> 
            <xsl:apply-templates select="." mode="label.markup"/> 
          </xsl:variable> 
          <xsl:copy-of select="$label"/> 
          <xsl:if test="$label != ''"> 
            <xsl:value-of select="$autotoc.label.separator"/> 
          </xsl:if> 
        </xsl:if> 
        
        <xsl:apply-templates select="." mode="titleabbrev.markup"/> 
      </a> 
    </span> 
  </xsl:template> 

  <!-- XHTML  -->
  
  <xsl:template match="//diffmk:wrapper">
	<xsl:choose>
		<xsl:when test="@diffmk:change='deleted'">
				<xsl:text> </xsl:text>
		 </xsl:when>
		<xsl:when test="parent::node()[local-name()='title']">
				<xsl:value-of select="."/>
		 </xsl:when>
		 <xsl:otherwise>
			<span class="diffmkwrapper">
				<xsl:value-of select="."/> 
			</span>
		</xsl:otherwise>
	</xsl:choose>
  </xsl:template>
  
  <!-- THERE ARE 2 TEMPLATES FOR ADDING 'ADDED', 'CHANGED' ICONS TO ALL OF THE TITLES-->

<xsl:template name="component.title">
  <xsl:param name="node" select="."/>

  <xsl:variable name="level">
    <xsl:choose>
      <xsl:when test="ancestor::section">
        <xsl:value-of select="count(ancestor::section)+1"/>
      </xsl:when>
      <xsl:when test="ancestor::sect5">6</xsl:when>
      <xsl:when test="ancestor::sect4">5</xsl:when>
      <xsl:when test="ancestor::sect3">4</xsl:when>
      <xsl:when test="ancestor::sect2">3</xsl:when>
      <xsl:when test="ancestor::sect1">2</xsl:when>
      <xsl:otherwise>1</xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <!-- Let's handle the case where a component (bibliography, for example)
       occurs inside a section; will we need parameters for this? -->
	
  <xsl:element name="h{$level+1}">
		<xsl:attribute name="class">
			title
		</xsl:attribute>
    <xsl:if test="$generate.id.attributes = 0">
      <xsl:call-template name="anchor">
	<xsl:with-param name="node" select="$node"/>
	<xsl:with-param name="conditional" select="0"/>
      </xsl:call-template>
    </xsl:if>
      <xsl:apply-templates select="$node" mode="object.title.markup">
      <xsl:with-param name="allow-anchors" select="1"/>
    </xsl:apply-templates>
  </xsl:element>
  
  	<xsl:choose> 
		  <xsl:when test="../@role='new' or ../@revisionflag='added' or ../@diffmk:change='added'"> 
			<img src="images/new.png" alt="new" class="img_marker" />
		  </xsl:when> 
		<!-- For mkdiff compatibility-->
		<xsl:when test="../@role='updated' or ../@revisionflag='changed' or ../@diffmk:change='changed'"> 
			<img src="images/updated.png" alt="updated" class="img_marker" />
		</xsl:when>
	</xsl:choose> 
</xsl:template>

<!-- ==================================================================== -->

<xsl:template name="section.heading">
  <xsl:param name="section" select="."/>
  <xsl:param name="level" select="1"/>
  <xsl:param name="allow-anchors" select="1"/>
  <xsl:param name="title"/>
  <xsl:param name="class" select="'title'"/>

  <xsl:variable name="id">
    <xsl:choose>
      <!-- if title is in an *info wrapper, get the grandparent -->
      <xsl:when test="contains(local-name(..), 'info')">
        <xsl:call-template name="object.id">
          <xsl:with-param name="object" select="../.."/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="object.id">
          <xsl:with-param name="object" select=".."/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <!-- HTML H level is one higher than section level -->
  <xsl:variable name="hlevel">
    <xsl:choose>
      <!-- highest valid HTML H level is H6; so anything nested deeper
           than 5 levels down just becomes H6 -->
      <xsl:when test="$level &gt; 5">6</xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$level + 1"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:variable>
  
  <xsl:element name="h{$hlevel}">
    <xsl:attribute name="class">
	<xsl:value-of select="$class"/>
    </xsl:attribute>
    <xsl:if test="$css.decoration != '0'">
      <xsl:if test="$hlevel&lt;3">
        <xsl:attribute name="style">clear: both</xsl:attribute>
      </xsl:if>
    </xsl:if>
    <xsl:if test="$allow-anchors != 0 and $generate.id.attributes = 0">
      <xsl:call-template name="anchor">
        <xsl:with-param name="node" select="$section"/>
        <xsl:with-param name="conditional" select="0"/>
      </xsl:call-template>
    </xsl:if>
    <xsl:if test="$generate.id.attributes != 0 and not(local-name(.) = 'appendix')">
      <xsl:attribute name="id"><xsl:value-of select="$id"/></xsl:attribute>
    </xsl:if>
    <xsl:copy-of select="$title"/>
  </xsl:element>
 	<xsl:choose> 
		  <xsl:when test="../@role='new' or ../@revisionflag='added' or ../@diffmk:change='added'"> 
			<img src="images/new.png" alt="new" class="img_marker" />
		  </xsl:when> 
		<!-- For mkdiff compatibility-->
		<xsl:when test="../@role='updated' or ../@revisionflag='changed' or ../@diffmk:change='changed'"> 
			<img src="images/updated.png" alt="updated" class="img_marker" />
		</xsl:when>
	</xsl:choose>
</xsl:template>

<!-- ==================================================================== -->

</xsl:stylesheet>
