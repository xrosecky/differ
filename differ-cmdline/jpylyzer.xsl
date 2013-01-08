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
      <property name = "File last modified"><xsl:value-of select="fileInfo/fileLastModified"/></property>
      <property name = "File name"><xsl:value-of select="fileInfo/fileName"/></property>
      <property name = "File path"><xsl:value-of select="fileInfo/filePath"/></property>
      <property name = "File size"><xsl:value-of select="fileInfo/fileSizeInBytes"/></property>
      <property name = "Image width (Pixels)"><xsl:value-of select="jp2HeaderBox/imageHeaderBox/height"/></property>
      <property name = "Image height (Pixels)"><xsl:value-of select="jp2HeaderBox/imageHeaderBox/width"/></property>
      <property name = "Color depth"><xsl:value-of select="contiguousCodestreamBox/siz/ssizDepth"/></property>
      <property name = "Number of channels"><xsl:value-of select="jp2HeaderBox/imageHeaderBox/nC"/></property>
      <property name = "Color space"><xsl:value-of select="properties/jp2HeaderBox/colourSpecificationBox/enumCS"/></property>
      <property name = "Resolution vertical (PPI)"><xsl:value-of select="jp2HeaderBox/resolutionBox/displayResolutionBox/vResdInPixelsPerInch"/></property>
      <property name = "Resolution horizontal (PPI)"><xsl:value-of select="jp2HeaderBox/resolutionBox/displayResolutionBox/hResdInPixelsPerInch"/></property>
      <property name = "Display resolution horizontal (PPI)"><xsl:value-of select="jp2HeaderBox/resolutionBox/displayResolutionBox/hResdInPixelsPerInch"/></property>
      <property name = "Display Resolution vertical (PPI)"><xsl:value-of select="jp2HeaderBox/resolutionBox/displayResolutionBox/vResdInPixelsPerInch"/></property>
      <property name = "Validation (well formed and valid)"><xsl:value-of select="isValidJP2"/></property>
      <property name = "Type of format"><xsl:value-of select="properties/jp2HeaderBox/imageHeaderBox/c"/></property>
      <property name = "Universal unique identifier (UUID)"><xsl:value-of select="properties/uuidBox/uuid/text()"/></property>
      <property name = "Commentary"><xsl:value-of select="contiguousCodestreamBox/com/comment"/></property>
      <property name = "Number of tiles"><xsl:value-of select="contiguousCodestreamBox/siz/numberOfTiles"/></property>
      <property name = "Transformation"><xsl:value-of select="contiguousCodestreamBox/cod/transformation"/></property>
      <property name = "Compression"><xsl:value-of select="contiguousCodestreamBox/cod/transformation"/></property>
      <property name = "Compression ratio"><xsl:value-of select="properties/compressionRatio"/></property>
      <property name = "Number of decomposition levels"><xsl:value-of select="contiguousCodestreamBox/cod/levels"/></property>
      <property name = "Number of quality layers"><xsl:value-of select="contiguousCodestreamBox/cod/layers"/></property>
      <property name = "Progression order"><xsl:value-of select="contiguousCodestreamBox/cod/order"/></property>
      <property name = "Code block width (Pixels)"><xsl:value-of select="contiguousCodestreamBox/cod/codeBlockWidth"/></property>
      <property name = "Code block height (Pixels)"><xsl:value-of select="contiguousCodestreamBox/cod/codeBlockHeight"/></property>
      <property name = "Coding bypass"><xsl:value-of select="contiguousCodestreamBox/cod/codingBypass"/></property>
      <property name = "Start of packet header"><xsl:value-of select="contiguousCodestreamBox/cod/sop"/></property>
      <property name = "End of packet header"><xsl:value-of select="contiguousCodestreamBox/cod/eph"/></property>
   </properties>  
  </xsl:template>
</xsl:stylesheet>
