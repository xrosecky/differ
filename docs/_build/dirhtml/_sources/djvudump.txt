DJDump
......

.. contents::

od verze 16 do v26 umi DJVU

od verze 27 can recognize sDJVU

.. literalinclude:: bin/get-djvudump.sh
		    :language: bash

.. raw:: html

	 <div class="output djvudump">

.. literalinclude:: outputs/djvudump/djvudump.raw
		    
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

