JPyLyzer
........

.. literalinclude:: bin/get-jpylyzer.sh
		    :language: bash

.. raw:: html

	 <div class="output jpylyzer">
	 <a class="show" href="#">jpylyzer.raw - show</a>
	 <a class="hide" href="#">jpylyzer.raw - hide</a>

.. literalinclude:: outputs/jpylyzer/jpylyzer.raw
   
.. raw:: html

	 </div>

.. raw:: html

	 <div class="output jpylyzer">
	 <a class="show" href="#">ngc0253-field01-f606w_drz.fits.raw - show</a>
	 <a class="hide" href="#">ngc0253-field01-f606w_drz.fits.raw - hide</a>

.. literalinclude:: outputs/jpylyzer/ngc0253-field01-f606w_drz.fits.raw
   
.. raw:: html

	 </div>


.. raw:: html

	 <div class="output jpylyzer">
	 <a class="show" href="#">01-komLZW.tif.raw - show</a>
	 <a class="hide" href="#">01-komLZW.tif.raw - hide</a>

.. literalinclude:: outputs/jpylyzer/01-komLZW.tif.raw
   
.. raw:: html

	 </div>

.. raw:: html

	 <div class="output jpylyzer">
	 <a class="show" href="#">01.tif.raw - show</a>
	 <a class="hide" href="#">01.tif.raw - hide</a>

.. literalinclude:: outputs/jpylyzer/01.tif.raw
   
.. raw:: html

	 </div>

.. raw:: html

        <div class="output">
        <a class="show" href="#">02.jpg.raw  - show</a>
        <a class="hide" href="#">02.jpg.raw  - hide</a>

.. literalinclude:: outputs/jpylyzer/02.jpg.raw

.. raw:: html

        </div>

.. raw:: html

        <div class="output">
        <a class="show" href="#">03.jpf.raw  - show</a>
        <a class="hide" href="#">03.jpf.raw  - hide</a>

.. literalinclude:: outputs/jpylyzer/03.jpf.raw

.. raw:: html

        </div>

.. raw:: html

        <div class="output">
        <a class="show" href="#">03-PSCS5.jp2.raw  - show</a>
        <a class="hide" href="#">03-PSCS5.jp2.raw  - hide</a>

.. literalinclude:: outputs/jpylyzer/03-PSCS5.jp2.raw

.. raw:: html

        </div>
.. raw:: html

        <div class="output">
        <a class="show" href="#">04-KDU.jp2.raw  - show</a>
        <a class="hide" href="#">04-KDU.jp2.raw  - hide</a>

.. literalinclude:: outputs/jpylyzer/04-KDU.jp2.raw

.. raw:: html

        </div>

.. raw:: html

        <div class="output">
        <a class="show" href="#">05.djvu.raw  - show</a>
        <a class="hide" href="#">05.djvu.raw  - hide</a>

.. literalinclude:: outputs/jpylyzer/05.djvu.raw

.. raw:: html

        </div>

.. raw:: html

        <div class="output">
        <a class="show" href="#">06.png.raw  - show</a>
        <a class="hide" href="#">06.png.raw  - hide</a>

.. literalinclude:: outputs/jpylyzer/06.png.raw

.. raw:: html

        </div>



`output of jpylyzer <../../outputs/jpylyzer/jpylyzer.raw>`_


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

