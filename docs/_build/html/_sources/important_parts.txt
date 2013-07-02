Transformers of a external program outputs and cmdline application
..................................................................

Each specified output format has its own Transformer class

  https://github.com/jstavel/differ/tree/master/differ-common/src/main/java/cz/nkp/differ/compare/metadata/external

Each tranformation has its own configuration

  https://github.com/jstavel/differ/tree/master/differ-cmdline/src/main/resources

Main cmdline application context is 

  https://github.com/jstavel/differ/blob/master/differ-cmdline/src/main/resources/appCtx-differ-cmdline.xml

All significant properties that are recognized

  https://github.com/jstavel/differ/blob/master/differ-cmdline/src/main/resources/metadataSignificantProperties.xml

Text report output, main class for cmdline application, various formating classes

  https://github.com/jstavel/differ/tree/master/differ-cmdline/src/main/java/cz/nkp/differ/cmdline

Main Class, that fills list of extracted metadata and do all required reports is

  https://github.com/jstavel/differ/blob/master/differ-cmdline/src/main/java/cz/nkp/differ/cmdline/Main.java

Structure that hold all informations being extracted from all external image metadata extractors is

  https://github.com/jstavel/differ/blob/master/differ-common/src/main/java/cz/nkp/differ/compare/io/ImageProcessorResult.java

A piece of an image metadata is

  https://github.com/jstavel/differ/blob/master/differ-common/src/main/java/cz/nkp/differ/compare/metadata/ImageMetadata.java


Documentation
.............

Documentations is written using sphinx.

   https://github.com/jstavel/differ/tree/master/docs

A lot of examples of an cmdline application outputs

   https://github.com/jstavel/differ/tree/master/docs/examples

A lot of files with outputs of used extractors

   https://github.com/jstavel/differ/tree/master/docs/outputs

Example of comparison text report

   https://github.com/jstavel/differ/blob/master/docs/outputs/cmdline-compare.txt

Example of single image text report
   
   https://github.com/jstavel/differ/blob/master/docs/outputs/cmdline.txt

Examples how the extractors were used

   https://github.com/jstavel/differ/tree/master/docs/bin
   

Web application
...............

Configurations

   https://github.com/jstavel/differ/tree/master/differ-webapp/src/main/resources

Main sources for a web application

   https://github.com/jstavel/differ/tree/master/differ-webapp/src/main/java/cz/nkp/differ


Common things used both cmdline and web application
...................................................

interfaces, classes used both applications.

https://github.com/jstavel/differ/tree/master/differ-common

Interfaces mainly used by transformation process

   https://github.com/jstavel/differ/tree/master/differ-common/src/main/java/cz/nkp/differ/compare/metadata/external

.. note::
   
   this important part for all transformations.

result finishing changes

  https://github.com/jstavel/differ/tree/master/differ-common/src/main/java/cz/nkp/differ/compare/metadata/external/result


**Image Metadata class**

This class holds image metadata extracted from extractors.

   https://github.com/jstavel/differ/blob/master/differ-common/src/main/java/cz/nkp/differ/compare/metadata/ImageMetadata.java
