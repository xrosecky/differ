<xsl:stylesheet
  version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:jhove="http://hul.harvard.edu/ois/xml/ns/jhove"
  xmlns:mix="http://www.loc.gov/mix/v20"
  exclude-result-prefixes="jhove mix"
  >
  <xsl:output method="xml" indent="yes" encoding="iso-8859-1"/>
  <xsl:template match="/jhove:jhove">
    <properties>
      <xsl:apply-templates/>
    </properties>
  </xsl:template>

  <xsl:template match="jhove:repInfo/jhove:format">
     <property name="format"><xsl:value-of select="text()"/></property>
  </xsl:template>

  <xsl:template match="jhove:repInfo/jhove:status">
     <property name="status"><xsl:value-of select="text()"/></property>
  </xsl:template>

  <xsl:template match="jhove:repInfo/jhove:mimeType">
     <property name="mimetype"><xsl:value-of select="text()"/></property>
  </xsl:template>

  <xsl:template match="mix:BasicImageCharacteristics/mix:imageWidth">
     <property name="width"><xsl:value-of select="text()"/></property>
   </xsl:template>

  <xsl:template match="mix:BasicImageCharacteristics/mix:imageHeight">
     <property name="height"><xsl:value-of select="text()"/></property>
   </xsl:template>

  <xsl:template match="text()"/>
</xsl:stylesheet>