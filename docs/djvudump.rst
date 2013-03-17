DjVuDump
........

od verze 16 do v26 umi DJVU

od verze 27 can recognize sDJVU

.. literalinclude:: bin/get-djvudump.sh
		    :language: bash

.. raw:: html

	 <div class="output djvudump">
	 <a class="show" href="#">djvudump.raw - show</a>
	 <a class="hide" href="#">djvudump.raw - hide</a>

.. literalinclude:: outputs/djvudump/djvudump.raw
   
.. raw:: html

	 </div>

.. raw:: html

	 <div class="output djvudump">
	 <a class="show" href="#">ngc0253-field01-f606w_drz.fits.raw - show</a>
	 <a class="hide" href="#">ngc0253-field01-f606w_drz.fits.raw - hide</a>

.. literalinclude:: outputs/djvudump/ngc0253-field01-f606w_drz.fits.raw
   
.. raw:: html

	 </div>


.. raw:: html

	 <div class="output djvudump">
	 <a class="show" href="#">01-komLZW.tif.raw - show</a>
	 <a class="hide" href="#">01-komLZW.tif.raw - hide</a>

.. literalinclude:: outputs/djvudump/01-komLZW.tif.raw
   
.. raw:: html

	 </div>

.. raw:: html

	 <div class="output djvudump">
	 <a class="show" href="#">01.tif.raw - show</a>
	 <a class="hide" href="#">01.tif.raw - hide</a>

.. literalinclude:: outputs/djvudump/01.tif.raw
   
.. raw:: html

	 </div>

.. raw:: html

        <div class="output">
        <a class="show" href="#">02.jpg.raw  - show</a>
        <a class="hide" href="#">02.jpg.raw  - hide</a>

.. literalinclude:: outputs/djvudump/02.jpg.raw

.. raw:: html

        </div>

.. raw:: html

        <div class="output">
        <a class="show" href="#">03.jpf.raw  - show</a>
        <a class="hide" href="#">03.jpf.raw  - hide</a>

.. literalinclude:: outputs/djvudump/03.jpf.raw

.. raw:: html

        </div>

.. raw:: html

        <div class="output">
        <a class="show" href="#">03-PSCS5.jp2.raw  - show</a>
        <a class="hide" href="#">03-PSCS5.jp2.raw  - hide</a>

.. literalinclude:: outputs/djvudump/03-PSCS5.jp2.raw

.. raw:: html

        </div>
.. raw:: html

        <div class="output">
        <a class="show" href="#">04-KDU.jp2.raw  - show</a>
        <a class="hide" href="#">04-KDU.jp2.raw  - hide</a>

.. literalinclude:: outputs/djvudump/04-KDU.jp2.raw

.. raw:: html

        </div>

.. raw:: html

        <div class="output">
        <a class="show" href="#">05.djvu.raw  - show</a>
        <a class="hide" href="#">05.djvu.raw  - hide</a>

.. literalinclude:: outputs/djvudump/05.djvu.raw

.. raw:: html

        </div>

.. raw:: html

        <div class="output">
        <a class="show" href="#">06.png.raw  - show</a>
        <a class="hide" href="#">06.png.raw  - hide</a>

.. literalinclude:: outputs/djvudump/06.png.raw

.. raw:: html

        </div>


Significant Properties
~~~~~~~~~~~~~~~~~~~~~~~	 

   ==========================    ======================
   Information                   Value from an example
   ==========================    ======================
   format version                v21
   datum vytvoreni               
   velikost souboru              
   num of colors                 (color .. 24bit)
   rozliseni                     200 dpi
   pocet pixelu                  3776x2520
   gamma correction              2.2
   pocet vrstev                  4
   expozice
   vyvazeni bile
   ==========================    ======================
   

DJVU can use max. 24 bits for a color channel or indexed colors.

Layers and Compressions
~~~~~~~~~~~~~~~~~~~~~~~~

   
   BG .. background
   44 .. compression IW44
   
   FG .. foreground
   BZ .. compression JP2
   2K .. compression JPEG200
   JP .. compression JPEG

   SJBZ .. mask with compression
   
   DJBZ .. shared layer with compression JP2
   TXTA .. hidden text layer
   TXTZ .. hidden text layer

slices nejsou moc dulezite.

