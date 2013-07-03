Tests of Metadata Extractors
............................

It is necessary to test that all significant properties are well mapped into internal structure.


Configuration of metadata extractor test contains:

- significant properties recognized by hand for a given image
- list of significant properties that extractor recognizes
- file with raw output from metadata extractor


main file with context of all tests is: =src/test/resources/appCtx-differ-cmdline-test.xml=

.. list-table:: Tests of metadata extractors
   :header-rows: 1
   
   * - Metadata extractor
     - Image
     - file with recognized significant properties
     - file with raw output
   
   * - jpylyzerMetadataExtractor
     - examples/images_01/14.jpf
     - resources/tests/image14-significant-properties.xml
     - examples/images_01/14/output-jpylyzer.raw
     
