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

    <xsl:import href="shared.xslt"/>

    <xsl:import href="doclet.xslt"/>

    <xsl:output method="html" indent="yes" name="html"/>
    <xsl:param name="outputFile"/>

    <xsl:template match="/">
        <document xmlns="http://maven.apache.org/XDOC/2.0"
                  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                  xsi:schemaLocation="http://maven.apache.org/XDOC/2.0 http://maven.apache.org/xsd/xdoc-2.0.xsd">

            <xsl:call-template name="head">
                <xsl:with-param name="pageTitle" select="Doclets"/>
            </xsl:call-template>

            <body>

                <section name="Doclet Overview">

                    <p>
                        The
                        <code>javadoc</code>
                        tool excels at extracting documentation and other source code metadata from Java classes. The
                        default output is, unfortunately, restricted to a myriad of HTML files. Luckily, the
                        <code>javadoc</code>
                        tool supports
                        a "plugin mechanism" called "doclets". A doclet receives metadata, such as class members and
                        source code comments,
                        and produces output documents. The default doclet produces said myriad of HTML files.
                    </p>

                    <p>
                        The
                        <em>doclet</em>
                        module includes replacement doclets for the default one. By using these alternative doclets you
                        can
                        harness the wealth of data which
                        <code>javadoc</code>
                        extracts from the source code without limiting yourself to the
                        default HTML output.
                    </p>

                    <xsl:for-each select="//class[tags/tag/@name = '@doclet']">
                        <xsl:sort select="@name"/>
                        <xsl:variable name="fileName" select="concat(replace($outputFile, '.xml','-'), @name, '.xml')"/>
                        <p>
                            <strong>
                                <a href="{replace($fileName, '.xml', '.html')}">
                                    <xsl:value-of select="@name"/>
                                </a>
                                <xsl:if test="tags/tag[@name='@doclet-tagline']">
                                    -
                                    <xsl:value-of select="tags/tag[@name='@doclet-tagline']/@text"/>
                                </xsl:if>
                            </strong>
                        </p>
                        <p>
                            <xsl:call-template name="documentation">
                                <xsl:with-param name="element" select="comment/p[1]"/>
                            </xsl:call-template>
                        </p>
                        <xsl:apply-templates select="." mode="doclet">
                            <xsl:with-param name="fileName" select="$fileName"/>
                        </xsl:apply-templates>
                    </xsl:for-each>

                </section>

            </body>

        </document>
    </xsl:template>

    <xsl:template match="class">
        <h1>
            <xsl:value-of select="@name"/>
        </h1>
        <xsl:call-template name="documentation">
            <xsl:with-param name="element" select="comment"/>
        </xsl:call-template>

        <xsl:apply-templates select="methods/method"/>

        <xsl:apply-templates select="fields/field"/>
    </xsl:template>

    <xsl:template match="method">
        <p>Method
            <xsl:value-of select="@name"/>
        </p>
        <xsl:call-template name="documentation">
            <xsl:with-param name="element" select="comment"/>
        </xsl:call-template>
    </xsl:template>
    <xsl:template match="field">
        <p>Field
            <xsl:value-of select="@name"/>
        </p>
        <xsl:call-template name="documentation">
            <xsl:with-param name="element" select="comment"/>
        </xsl:call-template>
    </xsl:template>
</xsl:stylesheet>