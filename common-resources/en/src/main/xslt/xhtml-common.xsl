<?xml version='1.0'?>

<!--
   Copyright 2008 JBoss, a division of Red Hat
   License: LGPL
   Author: Mark Newton <mark.newton@jboss.org>
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"  xmlns:date="http://exslt.org/dates-and-times" exclude-result-prefixes="date">

   <xsl:import href="collapsing-navigation.xsl"/>
   <!--xsl:param name="generate.toc" select="'book toc'"/-->
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
<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
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
		<!--script type="text/javascript" src="script/prototype-1.6.0.2.js"><xsl:comment>If you see this message, your web browser doesn't support JavaScript or JavaScript is disabled.</xsl:comment></script>
		<script type="text/javascript" src="script/effects.js"><xsl:comment>If you see this message, your web browser doesn't support JavaScript or JavaScript is disabled.</xsl:comment></script>
		<script type="text/javascript" src="script/scriptaculous.js"><xsl:comment>If you see this message, your web browser doesn't support JavaScript or JavaScript is disabled.</xsl:comment></script-->

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

 <xsl:template name="feedback">
	<div id="feedback-maincontainer" style="display:none">
		<div id="feedback-header">
			Send your remarks, comments or wishes to doc team
		</div>
		<a href="#" onclick="feedbackClose();" id="feedback-close">
			<img src="images/close.png" class="feedback-images" />
		</a>
		<!--div id="feedback-state"><xsl:text> </xsl:text></div-->
		<iframe id='feedback-iFrame' name="feedback-iFrame"><xsl:text> </xsl:text></iframe>
		<form id="feedback-mailform" method="post" action="http://192.168.0.194:8090/feedbackRF/FeedbackRF" target="feedback-iFrame">
			<div class="feedback-textbox-div">
			  Subject:<input type="text" id="subject" name="subject" title="Enter the subject of your message" class="feedback-textbox" />
			</div>
			<div class="feedback-textbox-div">
			  <span style="vertical-align: top;">Message:</span>
			  <textarea name="emailContent" title="Type here the text of your message" id="feedback-emailContent"><xsl:text> </xsl:text></textarea>
			</div>
			<div class="feedback-textbox-div">
			  Your name:<input type="text" id="senderName" name="senderName" title="Enter your name" class="feedback-textbox" />
			</div>
			<div class="feedback-textbox-div">
			  Your email:<input type="text" id="from" name="from" title="Enter your email address" class="feedback-textbox" />
			</div>
			<span class="feedback-button-container">
				<input type="submit" value="Send Message" name="submit" class="feedback-formbutton" title="Send Message" />
			</span>
			<span class="feedback-button-container">
				<input type="reset" value="Clear All Fields" class="feedback-formbutton" title="Clear All Fields" />
			</span>
	    </form>
	</div>
	<div id="feedback-wrapper">
		<a id="feedback-link" onclick="feedbackAppear();">
			<img src="images/feedback_logo.png" class="feedback-images" width="100px"/>
		</a>
	</div>							
 </xsl:template>
</xsl:stylesheet>
