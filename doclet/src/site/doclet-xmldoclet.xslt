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
    <xsl:output method="html" indent="yes" name="html"/>

    <xsl:template match="class" mode="doclet-xmldoclet">

        <section name="Document Creators">

            <xsl:call-template name="documentation">
                <xsl:with-param name="element" select="//class[@qualified-name='info.mikaelsvensson.devtools.doclet.shared.DocumentCreator']/comment"/>
            </xsl:call-template>

            <xsl:for-each
                        select="//class[@abstract='false' and starts-with(@qualified-name, 'info.mikaelsvensson.') and ends-with(@qualified-name, 'DocumentCreator')]">
                <xsl:sort select="@name"/>
                    <subsection name="{@name}">

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
                            <xsl:text><![CDATA[      -format.name ]]></xsl:text><xsl:value-of select="@qualified-name"/><xsl:text>&#x0a;</xsl:text>
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

                        <br/>
                        Parameters:
                        <table>
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Description</th>
                                    <th>Default Value</th>
                                </tr>
                            </thead>
                            <tbody>
                                <xsl:for-each
                                        select=".//field[annotations/annotation/type/@qualified-name='info.mikaelsvensson.devtools.doclet.xml.FormatProperty']">
                                    <tr>
                                        <td>
                                            <xsl:value-of select="@constant-value"/>
                                        </td>
                                        <td>
                                            <xsl:call-template name="documentation">
                                                <xsl:with-param name="element" select="comment"/>
                                            </xsl:call-template>
                                        </td>
                                        <td>
                                            <xsl:value-of
                                                    select=".//annotation[type/@qualified-name='info.mikaelsvensson.devtools.doclet.xml.FormatProperty']/element-values/element-value[@element-name='defaultValue']"/>
                                        </td>
                                    </tr>
                                </xsl:for-each>
                            </tbody>
                        </table>
                    </subsection>
                </xsl:for-each>
        </section>

    </xsl:template>

</xsl:stylesheet>