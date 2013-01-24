JPyLyzer
........

.. literalinclude:: outputs/get-jpylyzer.sh
		    :language: bash

`output of jpylyzer <../../outputs/jpylyzer.raw>`_


Significant Properties
~~~~~~~~~~~~~~~~~~~~~~

.. csv-table:: Map of significant properties
   :delim: ;
   :header: "Properties", "Properties as used in program", "XPath in JPylyzer xml output"
   :file: jpylyzer.csv


tabulka
- identifikace
image style format ::

  jp2000

file format ::

  *.jp2 , *.jpf

- validace
  all //tests are important

- charakterizace

commentaries ::

  kakadu

compression ::

  reversible/ireversible

transformation ::

  5-3 reversible
  9-7 ireversible

compression ration ::

  2.39

tails ::

      <contiguousCodestreamBox>
      <siz>
        <numberOfTiles>1</numberOfTiles>

progression order ::

  <order>RPCL</order>

  values
  ======
  CPRL        
  RPCL
  RLCP
  LRCP
  PCRL
  CPRL

num of decomposition levels ::
  
      <cod>
        <levels>5</levels>

quality layers ::

  <layers>1</layers>


precinct ::

..  staci vypsat ve formate

  128x128, 256x256

  <cod>
  <precinctSizeX>128</precinctSizeX>
  <precinctSizeY>128</precinctSizeY>
  <precinctSizeX>128</precinctSizeX>
  <precinctSizeY>128</precinctSizeY>
  <precinctSizeX>128</precinctSizeX>
  <precinctSizeY>128</precinctSizeY>
  <precinctSizeX>128</precinctSizeX>
  <precinctSizeY>128</precinctSizeY>
  <precinctSizeX>128</precinctSizeX>
  <precinctSizeY>128</precinctSizeY>
  <precinctSizeX>256</precinctSizeX>
  <precinctSizeY>256</precinctSizeY>
  

regions of interest ::
  no/yes

code block size ::
  
        <cod>
        <codeBlockWidth>64</codeBlockWidth>
        <codeBlockHeight>64</codeBlockHeight>


tail length layer ::
    yes /no

bypass ::
        <cod>
        <codingBypass>yes</codingBypass>


icc profiles ::
  yes/no


soh start of packet header (zacatek hlavicky paketu) ::
  
  <sop>yes</sop>

eph end of packet header ::
  
  <eph>yes</eph>


xml box ::

   no

uuid box ::

    <uuidBox>
      <uuid>be7acfcb-97a9-42e8-9c71-999491e3afac</uuid>
    </uuidBox>


toto poskladat nejak dale.


rozliseni ::

    <vResdInPixelsPerInch>299.98</vResdInPixelsPerInch>

rozliseni display ::

    <hResdInPixelsPerInch>299.98</hResdInPixelsPerInch>

depth::
  
  <contiguousCodestreamBox>
  <siz>
  <ssizDepth>8</ssizDepth>

precincts ::

  <cod>
  <precincts>yes</precincts>

multiple component transformation ::
  
  <cod>
  <multipleComponentTransformation>yes</multipleComponentTransformation>


reset on boundaries ::

  <resetOnBoundaries>no</resetOnBoundaries>

transformation ::
  
  <transformation>5-3 reversible</transformation>
	
compression ration ::

  <compressionRatio>2.39</compressionRatio>

comments ::
  
  <com>
  <lcom>15</lcom>
  <rcom>ISO/IEC 8859-15 (Latin)</rcom>
  <comment>Kakadu-v6.4</comment>
  </com>

