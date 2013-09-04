The Image Validator Procedure
.............................

Input data
~~~~~~~~~~

One or two graphic images is the input of the program.

The program recognizes various graphic formats:

- ``tiff``
- ``jpeg``
- ``png``
- ``jpeg2000``
- ``djvu``
- ``pdf``
- ``FITS``

Identification
~~~~~~~~~~~~~~

The program identifies a format of an image by an extension used in a file name:

- ``tiff``: `*.tiff`, `*.tif`
- ``djvu``: `*.djvu`
- ``jpeg 2000``: `*.jp2000`, `*.jpf`, `*.jp2`, `*.jpp`
- ``jpeg``: `*.jpeg`, `*.jpg`
- ``pdf``: `*.pdf`
- ``png``: `*.png`
- ``fits``: `*.fits`

.. note::
   
   Users can add more extensions into the file ``differ-cmdline/src/main/resources/appCtx-differ-cmdline.xml`` and into the corresponding xml file 
   ``exiftoolMetadataExtractor.xml`` 
   or ``exiv2MetadataExtractor.xml`` 
   or ``fitsMetadataExtractor.xml``
   or ``imagemagickMetadataExtractor.xml``
   or ``jhoveMetadataExtractor.xml``
   or ``jpylyzerMetadataExtractor.xml``
   or ``kakaduMetadataExtractor.xml``

   The xml tag ``supportedFileExtensions`` holds information about which file name extensions the given metadata extractor recognizes.

.. important::

   Users must call ``mvn package`` in the directory ``differ-cmdline`` everytime he or she changes xml files.


Format
~~~~~~

The program can work with images in format:

- ``TIFF``
- ``JPEG``
- ``DjVu``
- ``JPEG 2000``
- ``PDF``
- ``PNG``
- ``FITS``

Validation and Characterization
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

The program collects the information calling various image metadata extractors.

A type of the information can be:

- identification::

    the information tells basic information about image file or
    tells basic structural information about image format.

- characterization::

    the information describes image in detail.

- validation::

    the information tells that image format is valid and well formed.

The program calls various external programs to extract image metadata depending on image format:

- ``TIFF``

  - ``Exiftool``
  - ``Exiv2``
  - ``Fits``
  - ``Imagemagick``
  - ``Jhove``

- ``JPEG``
  
  - ``Exiftool``
  - ``Exiv2``
  - ``Fits``
  - ``Imagemagick``
  - ``Jhove``
  - ``Daitss``
    

- ``DjVu``
  
  - ``Exiftool``
  - ``Exiv2``
  - ``Fits``
  - ``Imagemagick``
  - ``Jhove``
  - ``Daitss``

- ``JPEG 2000``

  - ``Exiftool``
  - ``Exiv2``
  - ``Fits``
  - ``Imagemagick``
  - ``Jhove``
  - ``Daitss``
  - ``JPylyzer``
  - ``KDU_expand``


TODO: png, pdf, djvu, fits doplnit plus nastroje

.. note:: Detailed information about each external program can be seen in `Recognized Extractor Outputs`_.
