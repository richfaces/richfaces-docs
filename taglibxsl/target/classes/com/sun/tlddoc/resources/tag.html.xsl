<?xml version="1.0" encoding="UTF-8" ?>

<!--
  - <license>
  - Copyright (c) 2003-2004, Sun Microsystems, Inc.
  - All rights reserved.
  - 
  - Redistribution and use in source and binary forms, with or without 
  - modification, are permitted provided that the following conditions are met:
  - 
  -     * Redistributions of source code must retain the above copyright 
  -       notice, this list of conditions and the following disclaimer.
  -     * Redistributions in binary form must reproduce the above copyright 
  -       notice, this list of conditions and the following disclaimer in the
  -       documentation and/or other materials provided with the distribution.
  -     * Neither the name of Sun Microsystems, Inc. nor the names of its 
  -       contributors may be used to endorse or promote products derived from
  -       this software without specific prior written permission.
  - 
  - THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
  - "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
  - TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A 
  - PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
  - CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
  - EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
  - ROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
  - PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
  - LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
  - NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
  - SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
  - </license>
  -->

<!--
    Document   : tag.html.xsl
    Created on : December 18, 2002, 5:22 PM
    Author     : mroth
    Description:
        Creates the tag detail page (right frame), listing the known
        information for a given tag in a tag library.
-->

<xsl:stylesheet version="1.0"
    xmlns:javaee="http://java.sun.com/xml/ns/javaee" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
    
    <xsl:param name="tlddoc-shortName">default</xsl:param>
    <xsl:param name="tlddoc-tagName">default</xsl:param>

    <!-- template rule matching source root element -->
    <xsl:template match="/">
      <xsl:apply-templates select="javaee:tlds/javaee:taglib"/>
    </xsl:template>
    
    <xsl:template match="javaee:taglib">
      <xsl:if test="javaee:short-name=$tlddoc-shortName">
        <xsl:apply-templates select="javaee:tag|javaee:tag-file"/>
      </xsl:if>
    </xsl:template>
    
    <xsl:template match="javaee:tag|javaee:tag-file">
      <xsl:if test="javaee:name=$tlddoc-tagName">
        <xsl:variable name="tldname">
          <xsl:choose>
            <xsl:when test="../javaee:display-name!=''">
              <xsl:value-of select="../javaee:display-name"/>
            </xsl:when>
            <xsl:when test="../javaee:short-name!=''">
              <xsl:value-of select="../javaee:short-name"/>
            </xsl:when>
            <xsl:otherwise>
              Unnamed TLD
            </xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <xsl:variable name="title">
          <xsl:value-of select="javaee:name"/>
          (<xsl:value-of select="/javaee:tlds/javaee:config/javaee:window-title"/>)
        </xsl:variable>
            
            <!-- Tag Information -->
            <table class="doctable"  cellpadding="3" cellspacing="0" width="100%">
              <tr  class="TableHeadingColor">
                <td colspan="2">
                  <font size="+2">
                    <b>Tag Information</b>
                  </font>
                </td>
              </tr>
              <tr>
                <td>Tag Class</td>
                <td>
                  <xsl:choose>
                    <xsl:when test="javaee:tag-class!=''">
                      <xsl:value-of select="javaee:tag-class"/>
                    </xsl:when>
                    <xsl:otherwise>
                      <i>None</i>
                    </xsl:otherwise>
                  </xsl:choose>
                </td>
              </tr>
              <tr>
                <td>TagExtraInfo Class</td>
                <td>
                  <xsl:choose>
                    <xsl:when test="javaee:tei-class!=''">
                      <xsl:value-of select="javaee:tei-class"/>
                    </xsl:when>
                    <xsl:otherwise>
                      <i>None</i>
                    </xsl:otherwise>
                  </xsl:choose>
                </td>
              </tr>
              <tr>
                <td>Body Content</td>
                <td>
                  <xsl:choose>
                    <xsl:when test="javaee:body-content!=''">
                      <xsl:value-of select="javaee:body-content"/>
                    </xsl:when>
                    <xsl:otherwise>
                      <i>None</i>
                    </xsl:otherwise>
                  </xsl:choose>
                </td>
              </tr>
              <tr>
                <td>Display Name</td>
                <td>
                  <xsl:choose>
                    <xsl:when test="javaee:display-name!=''">
                      <xsl:value-of select="javaee:display-name"/>
                    </xsl:when>
                    <xsl:otherwise>
                      <i>None</i>
                    </xsl:otherwise>
                  </xsl:choose>
                </td>
              </tr>
            </table>
            
            <!-- Attribute Information -->
            <table class="doctable"  cellpadding="3" cellspacing="0" width="100%">
              <tr  class="TableHeadingColor">
                <td colspan="5">
                  <font size="+2">
                    <b>Attributes</b>
                  </font>
                </td>
              </tr>
              <xsl:choose>
                <xsl:when test="count(javaee:attribute)>0">
                  <tr>
                    <td><b>Name</b></td>
                    <td><b>Required</b></td>
                    <td><b>Request-time</b></td>                    
                    <td><b>Type</b></td>
                    <td><b>Description</b></td>
                  </tr>
                  <xsl:apply-templates select="javaee:attribute"/>
                </xsl:when>
                <xsl:otherwise>
                  <td colspan="5"><i>No Attributes Defined.</i></td>
                </xsl:otherwise>
              </xsl:choose>
            </table>

            <!-- Variable Information -->
            <table border="1" cellpadding="3" cellspacing="0" width="100%">
              <tr bgcolor="#CCCCFF" class="TableHeadingColor">
                <td colspan="5">
                  <font size="+2">
                    <b>Variables</b>
                  </font>
                </td>
              </tr>
              <xsl:choose>
                <xsl:when test="count(javaee:variable)>0">
                  <tr>
                    <td><b>Name</b></td>
                    <td><b>Type</b></td>
                    <td><b>Declare</b></td>
                    <td><b>Scope</b></td>
                    <td><b>Description</b></td>
                  </tr>
                  <xsl:apply-templates select="javaee:variable"/>
                </xsl:when>
                <xsl:otherwise>
                  <td colspan="2"><i>No Variables Defined.</i></td>
                </xsl:otherwise>
              </xsl:choose>
            </table>        
      </xsl:if>
    </xsl:template>

    <xsl:template match="javaee:attribute">
      <tr valign="top">
        <td><xsl:apply-templates select="javaee:name"/></td>
        <td>
          <xsl:choose>
            <xsl:when test="javaee:required!=''">
              <xsl:value-of select="javaee:required"/>
            </xsl:when>
            <xsl:otherwise>false</xsl:otherwise>
          </xsl:choose>
        </td>
        <td>
          <xsl:choose>
            <xsl:when test="javaee:rtexprvalue!=''">
              <xsl:value-of select="javaee:rtexprvalue"/>
            </xsl:when>
            <xsl:otherwise>false</xsl:otherwise>
          </xsl:choose>
        </td>        
        <td>
          <xsl:choose>
            <xsl:when test="javaee:deferred-value">
                <xsl:choose>
                    <xsl:when test="javaee:deferred-value/javaee:type">
                        <code>javax.el.ValueExpression</code>
                        <br/>(<i>must evaluate to </i><code><xsl:value-of
                                select="javaee:deferred-value/javaee:type"/></code>)
                    </xsl:when>
                    <xsl:otherwise>
                        <code>javax.el.ValueExpression</code>
                        <br/>(<i>must evaluate to </i><code>java.lang.Object</code>)
                    </xsl:otherwise>
                </xsl:choose>                                
            </xsl:when>
            <xsl:when test="javaee:deferred-method">
                <xsl:choose>
                    <xsl:when test="javaee:deferred-method/javaee:method-signature">
                        <code>javax.el.MethodExpression</code>
                        <br/>(<i>signature must match </i><code><xsl:value-of
                                select="javaee:deferred-method/javaee:method-signature"/></code>)
                    </xsl:when>
                    <xsl:otherwise>
                        <code>javax.el.MethodExpression</code>
                        <br/>(<i>signature must match </i><code>void methodname()</code>)
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:when test="javaee:type!=''">
              <code><xsl:value-of select="javaee:type"/></code>
            </xsl:when>
            <xsl:otherwise>
                <code>java.lang.String</code>                
            </xsl:otherwise>
          </xsl:choose>
        </td>
        <td>
          <xsl:choose>
            <xsl:when test="javaee:description!=''">
              <xsl:value-of select="javaee:description" disable-output-escaping="yes"/>
            </xsl:when>
            <xsl:otherwise><i>No Description</i></xsl:otherwise>
          </xsl:choose>
        </td>
      </tr>
    </xsl:template>
    
    <xsl:template match="javaee:variable">
      <tr>
        <td>
          <xsl:choose>
            <xsl:when test="javaee:name-given!=''">
              <xsl:value-of select="javaee:name-given"/>
            </xsl:when>
            <xsl:when test="javaee:name-from-attribute!=''">
              <i>From attribute '<xsl:value-of select="javaee:name-from-attribute"/>'</i>
            </xsl:when>
            <xsl:otherwise>
              <i>Unknown</i>
            </xsl:otherwise>
          </xsl:choose>
        </td>
        <td>
          <xsl:choose>
            <xsl:when test="javaee:variable-class!=''">
              <code><xsl:value-of select="javaee:variable-class"/></code>
            </xsl:when>
            <xsl:otherwise><code>java.lang.String</code></xsl:otherwise>
          </xsl:choose>
        </td>
        <td>
          <xsl:choose>
            <xsl:when test="javaee:declare!=''">
              <xsl:value-of select="javaee:declare"/>
            </xsl:when>
            <xsl:otherwise>true</xsl:otherwise>
          </xsl:choose>
        </td>
        <td>
          <xsl:choose>
            <xsl:when test="javaee:scope!=''">
              <xsl:value-of select="javaee:scope"/>
            </xsl:when>
            <xsl:otherwise>NESTED</xsl:otherwise>
          </xsl:choose>
        </td>
        <td>
          <xsl:choose>
            <xsl:when test="javaee:description!=''">
              <xsl:value-of select="javaee:description" disable-output-escaping="yes"/>
            </xsl:when>
            <xsl:otherwise><i>No Description</i></xsl:otherwise>
          </xsl:choose>
        </td>
      </tr>
    </xsl:template>
    
</xsl:stylesheet> 
