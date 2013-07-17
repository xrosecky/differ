<xsl:stylesheet
  version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:jhove="http://hul.harvard.edu/ois/xml/ns/jhove"
  xmlns:mix="http://www.loc.gov/mix/v20"
  exclude-result-prefixes="jhove mix"
  >

  <xsl:output method="xml" indent="yes" encoding="iso-8859-1"/>

  <xsl:template match="text()"/>

  <xsl:template match="/jpylyzer">
    <properties>
      <xsl:apply-templates/>
    </properties>
  </xsl:template>

  <xsl:template match="jp2HeaderBox/imageHeaderBox/height">
     <property name="width"><xsl:value-of select="text()"/></property>
  </xsl:template>

  <xsl:template match="jp2HeaderBox/imageHeaderBox/width">
     <property name="height"><xsl:value-of select="text()"/></property>
  </xsl:template>

  <xsl:template match="contiguousCodestreamBox/cod">
     <property name="preccints"><xsl:value-of select="precincts/text()"/></property>
     <property name="sop"><xsl:value-of select="sop/text()"/></property>
     <property name="eph"><xsl:value-of select="eph/text()"/></property>
     <property name="order"><xsl:value-of select="order/text()"/></property>
     <property name="layers"><xsl:value-of select="layers/text()"/></property>
     <property name="codeblock"><xsl:value-of select="concat(codeBlockWidth/text(), ' x ', codeBlockHeight/text())"/></property>
     <property name="transformation"><xsl:value-of select="transformation/text()"/></property>
  </xsl:template>

</xsl:stylesheet>