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
				<div id="overlay">
					<xsl:text> </xsl:text>
				</div>
<!-- FEEDBACK -->
	<div id="feedback-maincontainer" style="display:none">
		<div id="feedback-header">
			Send your remarks, comments or wishes to doc team
		</div>
		<a href="#" onclick="$('feedback-maincontainer').hide(); return false;" id="feedback-close">
			<img src="images/close.png" class="feedback-images" />
		</a>
		<div id="feedback-state"><xsl:text> </xsl:text></div>
    		<form id="feedback-mailform">
			<div class="feedback-textbox-div">
			  Subject:<input type="text" id="subject" name="subject" title="Enter the subject of your message" class="feedback-textbox" />
			</div>
			<div class="feedback-textbox-div">
			  <span style="vertical-align: top;">Message:</span>
			  <textarea name="message" title="Type here the text of your message" id="message"><xsl:text> </xsl:text></textarea>
			</div>
			<div class="feedback-textbox-div">
			  Your name:<input type="text" id="name" name="name" title="Enter your name" class="feedback-textbox" />
			</div>
			<div class="feedback-textbox-div">
			  Your email:<input type="text" id="email" name="email" title="Enter your email address" class="feedback-textbox" />
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
		<a id="feedback-link" href="#" onclick="$('feedback-maincontainer').appear(); return false;">
			<img src="images/feedback_logo.png" class="feedback-images" width="100px"/>
		</a>
	</div>							
 <!-- FEEDBACK ENDS -->
				<p xmlns="http://www.w3.org/1999/xhtml">
					<xsl:attribute name="id">
						<xsl:text>title</xsl:text>
					</xsl:attribute>
					<a>
						<xsl:attribute name="href">
							<xsl:value-of select="$siteHref" />
						</xsl:attribute>
						<xsl:attribute name="class">
							<xsl:text>site_href</xsl:text>
						</xsl:attribute>
						<strong>
						        <xsl:value-of select="$siteLinkText"/>	
						</strong>
					</a>
					<a>
						<xsl:attribute name="href">
							<xsl:value-of select="$docHref" />
						</xsl:attribute>
						<xsl:attribute name="class">
							<xsl:text>doc_href</xsl:text>
						</xsl:attribute>
						<strong>
						        <xsl:value-of select="$docLinkText"/>	
						</strong>
					</a>
				</p>
  <xsl:choose>
    <xsl:when test="bookinfo/title">
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/title"/>
    </xsl:when>
    <xsl:when test="info/title">
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/title"/>
    </xsl:when>
    <xsl:when test="title">
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="title"/>
    </xsl:when>
  </xsl:choose>

  <xsl:choose>
    <xsl:when test="bookinfo/subtitle">
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/subtitle"/>
    </xsl:when>
    <xsl:when test="info/subtitle">
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/subtitle"/>
    </xsl:when>
    <xsl:when test="subtitle">
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="subtitle"/>
    </xsl:when>
  </xsl:choose>

  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/corpauthor"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/corpauthor"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/authorgroup"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/authorgroup"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/author"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/author"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/othercredit"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/othercredit"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/releaseinfo"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/releaseinfo"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/copyright"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/copyright"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/legalnotice"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/legalnotice"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/pubdate"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/pubdate"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/revision"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/revision"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/revhistory"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/revhistory"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/abstract"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/abstract"/>
</xsl:template>

</xsl:stylesheet>
