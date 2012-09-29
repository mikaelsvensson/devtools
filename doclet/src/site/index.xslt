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
    <xsl:param name="color"/>
    <xsl:param name="title"/>


    <xsl:template match="/">
        <html>
            <xsl:call-template name="head">
                <xsl:with-param name="pageTitle" select="$title"/>
            </xsl:call-template>
            <body>
                <p>Doclets:
                    <ul>
                        <xsl:for-each select="//class[tags/tag/@name = '@doclet']">
                            <xsl:sort select="@name"/>
                            <xsl:variable name="fileName" select="concat('doclet-', @name, '.html')"/>
                            <li>
                                <a href="{$fileName}">
                                    <xsl:value-of select="@name"/>
                                </a>
                            </li>
                            <xsl:apply-templates select="." mode="doclet">
                                <xsl:with-param name="fileName" select="$fileName"/>
                            </xsl:apply-templates>
                        </xsl:for-each>
                    </ul>
                </p>
                <p>Inline Tags:
                    <ul>
                        <xsl:for-each
                                select="//class[@abstract='false' and ends-with(@qualified-name, 'InlineTagHandler')]">
                            <li>
                                <xsl:value-of select="@name"/>
                            </li>
                        </xsl:for-each>
                    </ul>
                </p>
                <xsl:apply-templates select="documentation/java/packages/package"/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="package">
        <xsl:variable name="fileName" select="concat(@name, '.html')"/>
        <p style="color: {$color};">
            Package
            <a href="{$fileName}">
                <xsl:value-of select="@name"/>
            </a>
        </p>
        <xsl:result-document href="{$fileName}" format="html">
            <html>
                <xsl:call-template name="head">
                    <xsl:with-param name="pageTitle">Package
                        <xsl:value-of select="@name"/>
                    </xsl:with-param>
                </xsl:call-template>
                <body>
                    <xsl:apply-templates/>
                    <a href="{$outputFile}">Back</a>
                </body>
            </html>
        </xsl:result-document>
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