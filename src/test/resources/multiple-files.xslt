<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" indent="yes" name="html"/>
    <xsl:param name="outputFile"/>
    <xsl:param name="color"/>
    <xsl:param name="title"/>

    <xsl:template name="head">
        <xsl:param name="pageTitle"/>
        <head>
            <title>
                <xsl:value-of select="$pageTitle"/>
            </title>
        </head>
    </xsl:template>

    <xsl:template match="/">
        <html>
            <xsl:call-template name="head">
                <xsl:with-param name="pageTitle" select="$title" />
            </xsl:call-template>
            <body>
                <xsl:apply-templates select="//package"/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="package">
        <xsl:variable name="fileName" select="concat(@name, '.html')"/>
        <p style="color: {$color};">
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
        <p>
            <xsl:value-of select="@name"/>
        </p>
    </xsl:template>
</xsl:stylesheet>