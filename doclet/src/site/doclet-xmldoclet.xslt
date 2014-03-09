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
    <xsl:import href="../../../common/src/site/shared.xslt"/>
    <xsl:output method="html" indent="yes" name="html"/>

    <xsl:template match="class" mode="doclet-xmldoclet">
        <xsl:param name="docletFileName"/>

        <xsl:variable name="docletName" select="@name"/>

        <section name="Document Creators">

            <xsl:call-template name="documentation">
                <xsl:with-param name="element" select="//class[@qualified-name='info.mikaelsvensson.devtools.doclet.shared.DocumentCreator']/comment"/>
            </xsl:call-template>

            <table>
                <tbody>
            <xsl:for-each
                        select="//class[@abstract='false' and starts-with(@qualified-name, 'info.mikaelsvensson.') and ends-with(@qualified-name, 'DocumentCreator')]">
                <xsl:sort select="@name"/>
                <xsl:variable name="document-creator-id" select=".//field[annotations/annotation/type/@qualified-name='info.mikaelsvensson.devtools.doclet.xml.FormatName']/@constant-value"/>

                <xsl:variable name="documentCreatorFileName" select="concat(replace($docletFileName, '.xml','-'), $document-creator-id, '.xml')"/>

                <tr>
                    <td>
                        <p>
                            <xsl:value-of select="$document-creator-id"/>
                        </p>
                    </td>
                    <td>
                        <xsl:if test="comment/p[1]">
                            <p>
                                <xsl:call-template name="documentation">
                                    <xsl:with-param name="element" select="comment/p[1]"/>
                                </xsl:call-template>
                            </p>
                        </xsl:if>
                        <p>
                            <a href="{replace($documentCreatorFileName, '.xml', '.html')}">More information, options and samples</a>
                        </p>
                    </td>
                </tr>

                <xsl:result-document href="{$documentCreatorFileName}">
                    <document xmlns="http://maven.apache.org/XDOC/2.0"
                              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                              xsi:schemaLocation="http://maven.apache.org/XDOC/2.0 http://maven.apache.org/xsd/xdoc-2.0.xsd">

                        <xsl:call-template name="head">
                            <xsl:with-param name="pageTitle">Doclet
                                <xsl:value-of select="@name"/>
                            </xsl:with-param>
                        </xsl:call-template>

                        <body>
                            <section name="Document Creator {$document-creator-id}">

                                <p>
                                    <a href="{replace($docletFileName, '.xml', '.html')}">Back to overview of <xsl:value-of select="$docletName"/></a>
                                </p>

                                <xsl:call-template name="documentation">
                                    <xsl:with-param name="element" select="comment"/>
                                </xsl:call-template>

                                <p>This is how you would send parameter values to the document generator from a Maven POM file:</p>

                                <source>
                                    <xsl:text><![CDATA[<plugin>]]>&#x0a;</xsl:text>
                                    <xsl:text><![CDATA[  <groupId>org.apache.maven.plugins</groupId>]]>&#x0a;</xsl:text>
                                    <xsl:text><![CDATA[  <artifactId>maven-javadoc-plugin</artifactId>]]>&#x0a;</xsl:text>
                                    <xsl:text><![CDATA[  ...]]>&#x0a;</xsl:text>
                                    <xsl:text><![CDATA[  <configuration>]]>&#x0a;</xsl:text>
                                    <xsl:text><![CDATA[    ...]]>&#x0a;</xsl:text>
                                    <xsl:text><![CDATA[    <doclet>info.mikaelsvensson.devtools.doclet.xml.XmlDoclet</doclet>]]>&#x0a;</xsl:text>
                                    <xsl:text><![CDATA[    ...]]>&#x0a;</xsl:text>
                                    <xsl:text><![CDATA[    <additionalparams>]]>&#x0a;</xsl:text>
                                    <xsl:text><![CDATA[      -format.name ]]></xsl:text><xsl:value-of select="$document-creator-id"/><xsl:text>&#x0a;</xsl:text>
                                    <xsl:for-each
                                            select=".//field[annotations/annotation/type/@qualified-name='info.mikaelsvensson.devtools.doclet.xml.FormatProperty']">
                                        <xsl:variable name="defaultValue"
                                                      select=".//annotation[type/@qualified-name='info.mikaelsvensson.devtools.doclet.xml.FormatProperty']/element-values/element-value[@element-name='defaultValue']"/>
                                        <xsl:choose>
                                            <xsl:when test="$defaultValue">
                                                <xsl:value-of select="concat('      -format.property.', @constant-value, ' ', $defaultValue, '&#x0a;')"/>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:value-of select="concat('      -format.property.', @constant-value, ' parameter_value', '&#x0a;')"/>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                    </xsl:for-each>
                                    <xsl:text><![CDATA[    </additionalparams>]]>&#x0a;</xsl:text>
                                    <xsl:text><![CDATA[...]]></xsl:text>
                                </source>

                                <subsection name="Document Creator Format Options">

                                    <xsl:call-template name="classFormatPropertiesTable">
                                        <xsl:with-param name="element" select="."/>
                                    </xsl:call-template>

                                </subsection>

                                <xsl:variable name="samples" select="document('../../xmldoclet-samples-data.xml')/files/file[output/@format=$document-creator-id]"/>

                                <xsl:if test="count($samples) > 0">

                                    <subsection name="Samples">
                                        <p>There are <xsl:value-of select="count($samples)"/> samples for this document creator:</p>
                                        <ul>
                                            <xsl:for-each select="$samples">
                                                <xsl:sort select="@name"/>
                                                <li><a href="#sample-{@name}"><xsl:value-of select="@name"/></a></li>
                                            </xsl:for-each>
                                        </ul>

                                        <xsl:apply-templates select="$samples">
                                            <xsl:with-param name="format" select="$document-creator-id"/>
                                            <xsl:sort select="@name"/>
                                        </xsl:apply-templates>
                                    </subsection>
                                </xsl:if>
                            </section>
                        </body>
                    </document>
                </xsl:result-document>
            </xsl:for-each>
                </tbody>
            </table>
        </section>

    </xsl:template>

    <xsl:template match="file">
        <xsl:param name="format"/>
        <a name="sample-{@name}"/>
        <subsection name="{@name}">
            <xsl:apply-templates select="input | output[@format = $format]"/>
        </subsection>
    </xsl:template>

    <xsl:template match="input">
        <p>Source:</p>
        <pre class="sh_java">
            <xsl:value-of select="."/>
        </pre>
    </xsl:template>

    <xsl:template match="output">
        <p>Output:</p>
        <pre class="sh_xml">
            <xsl:value-of select="."/>
        </pre>
    </xsl:template>

</xsl:stylesheet>