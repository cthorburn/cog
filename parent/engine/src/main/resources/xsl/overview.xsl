<?xml version="1.0" encoding="UTF-8" ?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0" xmlns:svg="http://www.w3.org/2000/svg"  xmlns:xlink="http://www.w3.org/1999/xlink">
	
	<xsl:output method="html" indent="yes" doctype-public="html"/>
	
	<xsl:template match="@font-family[parent::svg:text]">
		<xsl:attribute name="font-family">
			<xsl:value-of select="'Arial,sans serif'"/>
		</xsl:attribute>
	</xsl:template>
	
	<xsl:template match="@font-size[parent::svg:text]">
		<xsl:attribute name="font-size">
			<xsl:value-of select="'10'"/>
		</xsl:attribute>
	</xsl:template>
	
	<xsl:template match="/">
		<html>
			<head>
				<script type="text/javascript" src="js/trabajo/graph.js"></script>
			</head>
			<body>
				<xsl:apply-templates/>
			</body>
		</html>
	</xsl:template>
	
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="comment()"/>
	
</xsl:stylesheet>


