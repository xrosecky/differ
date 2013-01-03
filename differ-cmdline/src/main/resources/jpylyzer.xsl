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
  
  <xsl:template match="fileInfo/fileLastModified">
    <property name="fileLastModified"><xsl:value-of select="text()"/></property>  
  </xsl:template>

  <xsl:template match="fileInfo/fileSizeInBytes">
    <property name="fileSize"><xsl:value-of select="text()"/></property>  
  </xsl:template>

  <xsl:template match="fileInfo/fileSizeInBytes">
    <property name="fileSizeInBytes"><xsl:value-of select="text()"/></property>
  </xsl:template>

  <xsl:template match="jp2HeaderBox/imageHeaderBox/height">
    <property name="ImageWidth"><xsl:value-of select="text()"/></property>
  </xsl:template>
  
  <xsl:template match="jp2HeaderBox/imageHeaderBox/width">
    <property name="ImageHeight"><xsl:value-of select="text()"/></property>
  </xsl:template>

  <xsl:template match="contiguousCodestreamBox/siz/ssizDepth">
    <property name="colorDepth"><xsl:value-of select="text()"/></property>
  </xsl:template>

  <xsl:template match="jp2HeaderBox/imageHeaderBox/nC">
    <property name="numOfChannels"><xsl:value-of select="text()"/></property>
  </xsl:template>

  <xsl:template match="properties/jp2HeaderBox/colourSpecificationBox/enumCS">
    <property name="colorSpace"><xsl:value-of select="text()"/></property>
  </xsl:template>

  <xsl:template match="contiguousCodestreamBox/cod/levels">
    <property name="numOfDecompositionLevels"><xsl:value-of select="text()"/></property>
  </xsl:template>

  <xsl:template match="contiguousCodestreamBox/cod/layers">
    <property name="numOfQualityLayers"><xsl:value-of select="text()"/></property>
  </xsl:template>

  <xsl:template match="jp2HeaderBox/resolutionBox/displayResolutionBox/vResdInPixelsPerInch">
    <property name="resolutionInPixelsPerInch"><xsl:value-of select="text()"/></property>
  </xsl:template>

  <xsl:template match="jp2HeaderBox/resolutionBox/displayResolutionBox/hResdInPixelsPerInch">
    <property name="displayResolutionInPixelsPerInch"><xsl:value-of select="text()"/></property>
  </xsl:template>

  <xsl:template match="jp2HeaderBox/resolutionBox/displayResolutionBox/hResdInPixelsPerInch">
    <property name="displayResolutionInPixelsPerInch"><xsl:value-of select="text()"/></property>
  </xsl:template>

  <xsl:template match="isValidJP2">
    <property name="wellFormedAndValid"><xsl:value-of select="text()"/></property>
  </xsl:template>

  <xsl:template match="properties/jp2HeaderBox/imageHeaderBox/c">
    <property name="imageStyleFormat"><xsl:value-of select="text()"/></property>
  </xsl:template>

  <xsl:template match="properties/uuidBox/uuid">
    <property name="uuid"><xsl:value-of select="text()"/></property>
  </xsl:template>

  <xsl:template match="contiguousCodestreamBox/com/comment">
    <property name="commentary"><xsl:value-of select="text()"/></property>
  </xsl:template>

  <xsl:template match="contiguousCodestreamBox/siz/numberOfTiles">
    <property name="numberOfTiles"><xsl:value-of select="text()"/></property>
  </xsl:template>

  <xsl:template match="contiguousCodestreamBox/cod">
    <property name="codeBlockWidth"><xsl:value-of select="codeBlockWidth/text()"/></property>
    <property name="codeBlockHeight"><xsl:value-of select="codeBlockHeight/text()"/></property>
    <property name="preccints"><xsl:value-of select="precincts/text()"/></property>
    <property name="bypass"><xsl:value-of select="codingBypass/text()"/></property>
    <property name="startOfPacketHeader"><xsl:value-of select="sop/text()"/></property>
    <property name="endOfPacketHeader"><xsl:value-of select="eph/text()"/></property>
    <property name="progressionOrder"><xsl:value-of select="order/text()"/></property>
    <property name="layers"><xsl:value-of select="layers/text()"/></property>
    <property name="transformation"><xsl:value-of select="transformation/text()"/></property>
    <property name="multipleComponentTransformation"><xsl:value-of select="multipleComponentTransformation/text()"/></property>
    <property name="resetOnBoundaries"><xsl:value-of select="resetOnBoundaries/text()"/></property>
  </xsl:template>

  <xsl:template match="tests/contiguousCodestreamBox/*">
    <property> 
      <xsl:attribute name="name"><xsl:value-of select="concat('TESTS/',name())"/></xsl:attribute>
      <xsl:value-of select="text()"/>
    </property>
  </xsl:template>

  <xsl:template match="tests/signatureBox/*">
    <property> 
      <xsl:attribute name="name"><xsl:value-of select="concat('TESTS/',name())"/></xsl:attribute>
      <xsl:value-of select="text()"/>
    </property>
  </xsl:template>

  <xsl:template match="tests/fileTypeBox/*">
    <property> 
      <xsl:attribute name="name"><xsl:value-of select="concat('TESTS/',name())"/></xsl:attribute>
      <xsl:value-of select="text()"/>
    </property>
  </xsl:template>


  <xsl:template match="tests/jp2HeaderBox/imageHeaderBox">
    <property> 
      <xsl:attribute name="name"><xsl:value-of select="concat('TESTS/',name())"/></xsl:attribute>
      <xsl:value-of select="text()"/>
    </property>
  </xsl:template>

  <xsl:template match="tests/jp2HeaderBox/colourSpecificationBox/*">
    <property> 
      <xsl:attribute name="name"><xsl:value-of select="concat('TESTS/',name())"/></xsl:attribute>
      <xsl:value-of select="text()"/>
    </property>
  </xsl:template>

  <xsl:template match="tests/jp2HeaderBox/resolutionBox/displayResolutionBox/*">
    <property> 
      <xsl:attribute name="name"><xsl:value-of select="concat('TESTS/',name())"/></xsl:attribute>
      <xsl:value-of select="text()"/>
    </property>
  </xsl:template>

  <xsl:template match="tests/jp2HeaderBox/resolutionBox/*">
    <property> 
      <xsl:attribute name="name"><xsl:value-of select="concat('TESTS/',name())"/></xsl:attribute>
      <xsl:value-of select="text()"/>
    </property>
  </xsl:template>

  <xsl:template match="tests/*">
    <property> 
      <xsl:attribute name="name"><xsl:value-of select="concat('TESTS/',name())"/></xsl:attribute>
      <xsl:value-of select="text()"/>
    </property>
  </xsl:template>
  
</xsl:stylesheet>