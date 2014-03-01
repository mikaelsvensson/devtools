<?xml version="1.0" encoding="UTF-8"?>
<!--
  ~ Copyright (c) 2012, Mikael Svensson
  ~ All rights reserved.
  ~
  ~ Redistribution and use in source and binary forms, with or without
  ~ modification, are permitted provided that the following conditions are met:
  ~     * Redistributions of source code must retain the above copyright
  ~       notice, this list of conditions and the following disclaimer.
  ~     * Redistributions in binary form must reproduce the above copyright
  ~       notice, this list of conditions and the following disclaimer in the
  ~       documentation and/or other materials provided with the distribution.
  ~     * Neither the name of the copyright holder nor the names of the
  ~       contributors of this software may be used to endorse or promote products
  ~       derived from this software without specific prior written permission.
  ~
  ~ THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
  ~ ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
  ~ WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
  ~ DISCLAIMED. IN NO EVENT SHALL MIKAEL SVENSSON BE LIABLE FOR ANY
  ~ DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
  ~ (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
  ~ LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
  ~ ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
  ~ (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
  ~ SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
  -->

<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:import href="doclet-xmldoclet.xslt"/>
    <xsl:output method="html" indent="yes" name="html"/>

    <xsl:template match="class" mode="doclet">
        <xsl:param name="fileName"/>
        <xsl:result-document href="{$fileName}">
            <document xmlns="http://maven.apache.org/XDOC/2.0"
                      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                      xsi:schemaLocation="http://maven.apache.org/XDOC/2.0 http://maven.apache.org/xsd/xdoc-2.0.xsd">

                <xsl:call-template name="head">
                    <xsl:with-param name="pageTitle">Doclet
                        <xsl:value-of select="@name"/>
                    </xsl:with-param>
                </xsl:call-template>

                <body>

                    <section name="Doclet {@name}">

                        <xsl:call-template name="documentation">
                            <xsl:with-param name="element" select="comment"/>
                        </xsl:call-template>

                        <section name="Required Javadoc Configuration">
                            <p>Make sure Javadoc is started with these options:</p>
                            <ul>
                                <li>
                                    <code>doclet</code>
                                    set to
                                    <code>
                                        <xsl:value-of select="@qualified-name"/>
                                    </code>
                                </li>
                                <li>
                                    <code>docletpath</code>
                                    set to the path of
                                    <code>doclet.jar</code>.
                                </li>
                            </ul>
                            <p>For more information about how to make Javadoc use this Doclet, check out the Getting
                                Started pages:
                            </p>
                            <ul>
                                <li>
                                    <a href="using-doclets-from-console.html">Using the Doclets from Console</a>
                                </li>
                                <li>
                                    <a href="using-doclets-in-maven.html">Using the Doclets in Maven</a>
                                </li>
                            </ul>
                        </section>

                        <section name="Doclet Configuration Options">
                            <p>These options are used only by the
                                <xsl:value-of select="@name"/> doclet (they are specified in the same way as other
                                Javadoc options).
                            </p>

                            <xsl:call-template name="classFormatPropertiesTable">
                                <xsl:with-param name="element" select="."/>
                            </xsl:call-template>
                        </section>

                        <xsl:choose>
                            <xsl:when test="@qualified-name='info.mikaelsvensson.devtools.doclet.xml.XmlDoclet'">
                                <xsl:apply-templates select="." mode="doclet-xmldoclet">
                                    <xsl:with-param name="docletFileName" select="$fileName"/>
                                </xsl:apply-templates>
                            </xsl:when>
                        </xsl:choose>
                    </section>
                </body>
            </document>
        </xsl:result-document>
    </xsl:template>

</xsl:stylesheet>