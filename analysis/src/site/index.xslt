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

    <xsl:import href="analyzer.xslt"/>

    <xsl:output method="xml" indent="yes" name="xml"/>

    <xsl:param name="outputFile"/>

    <xsl:template match="/">
        <document xmlns="http://maven.apache.org/XDOC/2.0"
                  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                  xsi:schemaLocation="http://maven.apache.org/XDOC/2.0 http://maven.apache.org/xsd/xdoc-2.0.xsd">

            <xsl:call-template name="head">
                <xsl:with-param name="pageTitle" select="Doclets"/>
            </xsl:call-template>

            <body>

                <section name="Analysis Utilities">

                    <p>
                        There are many graphical tools for analyzing log files, and they often have a lot of features
                        and options. However, graphical tools are only good as long as you are there, in person, to
                        analyze the logs. What if you want to perform unattended log analysis?
                    </p>

                    <p>
                        Here you will find some simple command-line tools for analyzing certain types of log files.
                        Each utility is designed to a particular log and will analyze it from a certain point-of-view,
                        e.g. counting the number of exceptions in the log or measuring the average response time.
                    </p>

                    <p>
                        On other words: These analyzers are not for everyone, but very useful to those with the
                        targeted needs! Read more about each analyzer to see if is will produce the type of report
                        you are interested in.
                    </p>

                    <xsl:for-each select="//class[annotations/annotation/type/@qualified-name = 'info.mikaelsvensson.devtools.analysis.shared.CliHelp']">
                        <xsl:sort select="@name"/>
                        <xsl:variable name="fileName" select="concat(replace($outputFile, '.xml','-'), @name, '.xml')"/>
                        <p>
                            <strong>
                                <a href="{replace($fileName, '.xml', '.html')}">
                                    <xsl:value-of select="@name"/>
                                </a>
                            </strong>
                        </p>
                        <xsl:variable name="cliHelp" select="annotations/annotation[type/@qualified-name = 'info.mikaelsvensson.devtools.analysis.shared.CliHelp']/element-values/element-value[@element-name = 'text']"/>
                        <xsl:choose>
                            <xsl:when test="comment/p[1]">
                                <p>
                                    <xsl:call-template name="documentation">
                                        <xsl:with-param name="element" select="comment/p[1]"/>
                                    </xsl:call-template>
                                </p>
                            </xsl:when>
                            <xsl:when test="$cliHelp">
                                <p>
                                    <xsl:value-of select="$cliHelp"/>
                                </p>
                            </xsl:when>
                        </xsl:choose>

                        <xsl:apply-templates select="." mode="analyzer">
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