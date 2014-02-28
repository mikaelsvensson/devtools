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
    <xsl:output method="xml" indent="yes"/>

    <xsl:param name="format"/>

    <xsl:template match="/">
        <document xmlns="http://maven.apache.org/XDOC/2.0"
                  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                  xsi:schemaLocation="http://maven.apache.org/XDOC/2.0 http://maven.apache.org/xsd/xdoc-2.0.xsd">

            <properties>
                <title>Page Title</title>
                <author email="user@company.com">John Doe</author>
            </properties>

            <!-- Optional HEAD element, which is copied as is into the XHTML <head> element -->
            <head>
                <link type="text/css" rel="stylesheet" href="css/sh_ide-codewarrior.min.css"/>

                <script type="text/javascript" src="js/sh_main.min.js"></script>
                <script type="text/javascript" src="js/sh_java.min.js"></script>
                <script type="text/javascript" src="js/sh_xml.min.js"></script>
                <script type="text/javascript" src="js/init_sh.js"></script>
            </head>

            <body>

                <!-- The body of the document contains a number of sections -->
                <section name="Samples For Document Creator '{$format}'">

                    <p>This page shows the generated XML documents for different Java files. All samples show the output
                        created by the document createor called &quot;<xsl:value-of select="$format"/>&quot;.
                    </p>

                    <!-- in addition to XHTML, any number of subsections can be within a section -->
                    <xsl:apply-templates select="//file[output/@format = $format]">
                        <xsl:sort select="@name"/>
                    </xsl:apply-templates>

                </section>

            </body>

        </document>
    </xsl:template>

    <xsl:template match="file">
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