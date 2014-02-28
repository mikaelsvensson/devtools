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

    <xsl:template name="head">
        <xsl:param name="pageTitle"/>
        <properties>
            <title><xsl:value-of select="$pageTitle"/></title>
            <!--<author email="user@company.com">John Doe</author>-->
        </properties>

        <!-- Optional HEAD element, which is copied as is into the XHTML <head> element -->
        <head>
<!--
            <link type="text/css" rel="stylesheet" href="css/sh_ide-codewarrior.min.css"/>
            <script type="text/javascript" src="js/sh_main.min.js"></script>
            <script type="text/javascript" src="js/sh_java.min.js"></script>
            <script type="text/javascript" src="js/sh_xml.min.js"></script>
            <script type="text/javascript" src="js/init_sh.js"></script>
            <script type="text/javascript">
                $(document).ready(function () {
                $("pre.embedded-java-file").addClass("sh_java");
                $("pre.embedded-xml-file").addClass("sh_xml");
                sh_highlightDocument();
                });
            </script>
-->
        </head>
    </xsl:template>

    <xsl:template name="documentation">
        <xsl:param name="element" />
        <xsl:copy-of select="$element/child::node()"/>
    </xsl:template>

</xsl:stylesheet>