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
    <xsl:output method="html" indent="yes" name="html"/>

    <xsl:template match="class" mode="doclet-xmldoclet">
        <p>Document Creators:
            <ul>
                <xsl:for-each
                        select="//class[@abstract='false' and starts-with(@qualified-name, 'info.mikaelsvensson.') and ends-with(@qualified-name, 'DocumentCreator')]">
                    <li>
                        <xsl:value-of select="@name"/>
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
                                        select=".//field[annotations/annotation/type/@qualified-name='info.mikaelsvensson.docutil.xml.FormatProperty']">
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
                                                    select=".//annotation[type/@qualified-name='info.mikaelsvensson.docutil.xml.FormatProperty']/element-values/element-value[@element-name='defaultValue']"/>
                                        </td>
                                    </tr>
                                </xsl:for-each>
                            </tbody>
                        </table>
                    </li>
                </xsl:for-each>
            </ul>
        </p>

    </xsl:template>

</xsl:stylesheet>