New project was born
....................

A project ``The Image Validator`` is a successor of a project ``Differ``.
``Differ`` means ``Determinator of Image File Format properties``.

A project ``Differ`` was written in ``C++`` and ``PHP`` and it allowed to get
main properties for images in various image formats.

The main purpose of creating a succesor of this project was a need to have a program running
on a various platforms. That is why new project ``The Image Validator`` is written in ``Java``.


The Image Validator Procedure
.............................

Input data
~~~~~~~~~~

There is one or two graphic images at the input of the program.

The program recognizes various graphic formats:

- ``tiff``
- ``jpeg``
- ``png``
- ``jpeg2000``
- ``djvu``
- ``pdf``


Identification
~~~~~~~~~~~~~~

The program identifies a format of an image by an extension used in a file name:

- ``tiff``: `*.tiff`, `*.tif`
- ``djvu``: `*.djvu`
- ``jpeg 2000``: `*.jp2000`, `*.jpf`, `*.jp2`, `*.jpp`
- ``jpeg``: `*.jpeg`, `*.jpg`
- ``pdf``: `*.pdf`

.. note::
   
   A user can add more extensions into a file ``differ-cmdline/src/main/resources/appCtx-differ-cmdline.xml`` and into a proper xml file 
   ``exiftoolMetadataExtractor.xml`` 
   or ``exiv2MetadataExtractor.xml`` 
   or ``fitsMetadataExtractor.xml``
   or ``imagemagickMetadataExtractor.xml``
   or ``jhoveMetadataExtractor.xml``
   or ``jpylyzerMetadataExtractor.xml``
   or ``kakaduMetadataExtractor.xml``

   A xml tag ``supportedFileExtensions`` holds an information what file name extensions given metadata extractor recognizes.

.. important::

   A user must call ``mvn package`` in a directory ``differ-cmdline`` everytime he or she changes xml files.


Format
~~~~~~

The program can work with images in format:

- ``TIFF``
- ``JPEG``
- ``DjVu``
- ``JPEG 2000``
- ``PDF``

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


.. note:: User can see detailed information about each external program in `Recognized Extractor Outputs`_.
