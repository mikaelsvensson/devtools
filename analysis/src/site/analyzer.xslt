<?xml version="1.0" encoding="UTF-8"?>
<!--
  ~ Copyright 2014 Mikael Svensson
  ~
  ~    Licensed under the Apache License, Version 2.0 (the "License");
  ~    you may not use this file except in compliance with the License.
  ~    You may obtain a copy of the License at
  ~
  ~        http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~    Unless required by applicable law or agreed to in writing, software
  ~    distributed under the License is distributed on an "AS IS" BASIS,
  ~    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~    See the License for the specific language governing permissions and
  ~    limitations under the License.
  -->

<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:import href="../../../common/src/site/shared.xslt"/>

    <xsl:output method="xml" indent="yes" name="xml"/>

    <xsl:template match="class" mode="analyzer">
        <xsl:param name="fileName"/>
        <xsl:result-document href="{$fileName}">
            <document xmlns="http://maven.apache.org/XDOC/2.0"
                      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                      xsi:schemaLocation="http://maven.apache.org/XDOC/2.0 http://maven.apache.org/xsd/xdoc-2.0.xsd">

                <xsl:call-template name="head">
                    <xsl:with-param name="pageTitle">Log Analyzer
                        <xsl:value-of select="@name"/>
                    </xsl:with-param>
                </xsl:call-template>

                <body>

                    <section name="{@name}">

                        <xsl:variable name="cliHelp"
                                      select="annotations/annotation[type/@qualified-name = 'info.mikaelsvensson.devtools.analysis.shared.CliHelp']/element-values/element-value[@element-name = 'text']"/>
                        <xsl:choose>
                            <xsl:when test="comment">
                                <p>
                                    <xsl:call-template name="documentation">
                                        <xsl:with-param name="element" select="comment"/>
                                    </xsl:call-template>
                                </p>
                            </xsl:when>
                            <xsl:when test="$cliHelp">
                                <p>
                                    <xsl:value-of select="$cliHelp"/>
                                </p>
                            </xsl:when>
                        </xsl:choose>

                        <subsection name="Command-Line Arguments">
                            <dl>
                                <xsl:apply-templates select="." mode="cliOptionsList"/>
                            </dl>
                            <p>Full command-line:</p>
                            <pre>
                                <xsl:text>java </xsl:text>
                                <xsl:value-of select="@qualified-name"/>
                                <xsl:text> -cp [class path]</xsl:text>
                                <xsl:apply-templates select="." mode="fullCommandLine"/>
                            </pre>
                        </subsection>

                        <xsl:variable name="extDoc" select="document(concat('analyzer-', @name, '.xml'))/document"/>
                        <xsl:copy-of select="$extDoc"/>

                    </section>
                </body>
            </document>
        </xsl:result-document>
    </xsl:template>

    <xsl:template match="class" mode="cliOptionsList">
        <xsl:for-each
                select="annotations//single-annotation[type/@qualified-name='info.mikaelsvensson.devtools.analysis.shared.CliOptionConfig']/element-values">
            <dt>

                <code>
                    <xsl:value-of select="concat('-', element-value[@element-name='name'])"/>
                </code>
                <xsl:if test="element-value[@element-name='longName']">
                    or
                    <code>
                        <xsl:value-of select="concat('--', element-value[@element-name='longName'])"/>
                    </code>
                </xsl:if>
            </dt>
            <dd>
                <p>
                    <xsl:value-of select="element-value[@element-name='description']"/>
                </p>
                <xsl:if test="element-value[@element-name='required'] = 'true'">
                    <p>
                        This argument is&#x20;<strong>required</strong>.
                    </p>
                </xsl:if>
            </dd>
        </xsl:for-each>

        <xsl:variable name="superClassName" select="./superclass/@qualified-name"/>

        <xsl:apply-templates select="root()//class[@qualified-name = $superClassName]"
                             mode="cliOptionsList"/>
    </xsl:template>

    <xsl:template match="class" mode="fullCommandLine">
        <xsl:for-each
                select="annotations//single-annotation[type/@qualified-name='info.mikaelsvensson.devtools.analysis.shared.CliOptionConfig']/element-values">
            <xsl:text>&#x0A;    </xsl:text>
            <xsl:if test="not(element-value[@element-name='required']) or element-value[@element-name='required']/text() = 'false'">
                <xsl:text>[</xsl:text>
            </xsl:if>
            <xsl:choose>
                <xsl:when test="element-value[@element-name='longName']">
                    <xsl:value-of select="concat('--', element-value[@element-name='longName'])"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="concat('-', element-value[@element-name='name'])"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:if test="element-value[@element-name='numArgs'] != 0">
                <xsl:text> &lt;</xsl:text>
                <xsl:choose>
                    <xsl:when test="element-value[@element-name='argsDescription']">
                        <xsl:value-of select="element-value[@element-name='argsDescription']"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>arg</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:text>&gt;</xsl:text>
            </xsl:if>
            <xsl:if test="not(element-value[@element-name='required']) or element-value[@element-name='required']/text() = 'false'">
                <xsl:text>]</xsl:text>
            </xsl:if>
        </xsl:for-each>

        <xsl:variable name="superClassName" select="./superclass/@qualified-name"/>

        <xsl:apply-templates select="root()//class[@qualified-name = $superClassName]"
                             mode="fullCommandLine"/>
    </xsl:template>

</xsl:stylesheet>