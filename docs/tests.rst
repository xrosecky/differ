.. raw:: html

    <style> .red {color:red} </style>
    <style> .green {color:green} </style>
    <style> .yellow {color:yellow} </style>
.. role:: red
.. role:: green
.. role:: yellow

Tests of Metadata Extractors
............................

Tests are important to make sure that the properties from metadata extractors are properly mapped into the application.

The most significant tests are:

- Verifying that all properties from metadata extractor are handled in one way or another.
- Checking that the output value after transformation matches the raw extractor metadata.

Each extractor is contained to its own test application context.
	src/test/resources/${extractor}UnitTest.xml=

The context includes:

- File with raw output from metadata extractor to run through transformer 
- Significant properties list recognized manually for a given image
- Ignored properties that extractor recognizes but that are not included in Differ

Values not normalized but still passing are put in the top half of ignored properties list. 

.. list-table:: List of tests run
   :header-rows: 1
   :widths: 10 10 10 10 10
   
   * - Metadata extractor
     - Image
     - File with recognized significant properties
     - File with raw output
     - Verified	
   
   * - jpylyzerMetadataExtractor
     - examples/images_01/14.jpf
     - resources/tests/image14-significant-properties.xml
     - examples/images_01/14/output-jpylyzer.raw
     - :green:`OK`

   * - jhoveMetadataExtractor
     - examples/images_01/14.jpf
     - resources/tests/image14-significant-properties.xml
     - examples/images_01/14/output-jhove.raw
     - :green:`OK`

   * - jhoveMetadataExtractor
     - examples/images_01/01.jpg
     - resources/tests/image01-significant-properties.xml
     - examples/images_01/01/output-jhove.raw
     - :green:`OK`
     
   * - kakaduMetadataExtractor
     - examples/images_01/14.jpf
     - resources/tests/image14-significant-properties.xml
     - examples/images_01/14/output-kakadu.raw
     - :green:`OK`

   * - imagemagickMetadataExtractor
     - examples/images_01/01.jpg
     - resources/tests/image01-significant-properties.xml
     - examples/images_01/01/output-imagemagick.raw
     - :green:`OK`

   * - imagemagickMetadataExtractor
     - examples/images_14/14.jpf
     - resources/tests/image01-significant-properties.xml
     - examples/images_01/14/output-imagemagick.raw
     - :green:`OK`

   * - djvudumpMetadataExtractor
     - docs/examples/images/05.djvu
     - resources/tests/image05-significant-properties.xml
     - docs/examples/images/05/output-djvudump.raw
     - :green:`OK`

   * - exiftoolMetadataExtractor
     - images/images_01/01.jpg
     - resources/tests/image01-significant-properties.xml
     - images/images_02/01/output-exiftool.raw
     - :green:`OK`

   * - exiftoolMetadataExtractor
     - examples/images_01/14.jpf
     - resources/tests/image14-significant-properties.xml
     - examples/images_01/14/output-exiftool.raw
     - :green:`OK`

To create a new test for s new image:

1) Go to the appropriate test class
2) Create a new method copying the previous test with @Test annotation
3) Import the image properties with @Resource (these are the properties from context file). Change in the method all imports to this file.
4) Create resources in appropriate test context file in /resources by copying previous context (if there is one for same image format, choose that one as many properties are already in the correct category)
5) Change name of context files to reflect the import done in 3) with @Resource
6) Run tests. If it's the first test for the particular image format, there is a big chance some properties need to be deleted/added or moved to ignored/special properties.
     
