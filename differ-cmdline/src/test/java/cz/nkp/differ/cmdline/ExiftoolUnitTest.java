package cz.nkp.differ.cmdline;

import cz.nkp.differ.compare.metadata.external.ResultTransformer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


/**
 * Test class for exiftool transformer
 * User: Jonatan Svensson <jonatansve@gmail.com>
 * Date: 2013-07-19
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:exiftoolTestsCtx.xml"})
public class exiftoolUnitTest {
    List<ResultTransformer.Entry> transformedData;
    @Autowired
    private Map<String,Object> image01Test01;

    @Autowired
    private ResultTransformer exiftoolMetadataTransformer;

    @Test
    public void testImage01() throws Exception {

        byte[] stdout = TestHelper.readFile("../docs/examples/images_02/01/output-exiftool.raw");
        transformedData = exiftoolMetadataTransformer.transform(stdout,null);
        assertNotNull(transformedData);

        ArrayList ignoredProperties = (ArrayList) image01Test01.get("image01Test01IgnoredProperties");
        ArrayList recognizedProperties = (ArrayList) image01Test01.get("image01Test01RecognizedProperties");
        assertNotNull(recognizedProperties);

        /**
         * Test all properties are mapped.
         * Compare transformedData with list of
         * manual input of significant properties in image14Test01RecognizedProperties
         * Fails if a property is transformed but is yet not mapped.
         */
        for(ResultTransformer.Entry e: transformedData){
          assertTrue("Testing that transformed property is recognized: "+ e.getKey(),recognizedProperties.contains(e.getKey())||ignoredProperties.contains(e.getKey()));
        }

        /**
         * Test all properties that are not ignored.
         * Go through each entry in transformedData,
         * Look for the key in:
         * identificationProperties/validationProperties/characterizationProperties,
         * Assert that the value is identical.
         */

        LinkedHashMap l = (LinkedHashMap) image01Test01.get("image01SignificantProperties");
        LinkedHashMap lh1 = (LinkedHashMap) l.get("identificationProperties");
        LinkedHashMap lh2 = (LinkedHashMap) l.get("validationProperties");
        LinkedHashMap lh3 = (LinkedHashMap) l.get("characterizationProperties");

        String s;

        for(ResultTransformer.Entry e: transformedData){
            // Make sure it is not ignored first
            if(recognizedProperties.contains(e.getKey())){
                s= (String)lh1.get(e.getKey());
                if(s==null) {
                    s= (String)lh2.get(e.getKey());
                    if(s==null){
                        s= (String)lh3.get(e.getKey());
                    }
                }
                // If s is null here, then the entry is missing in manual data
               assertNotNull("Testing: "+e.getKey()+ " with: "+ s, s);
               assertEquals("Testing equality: "+e.getKey(), e.getValue(), s);
               s=null;
          }
        }

        /**
         * Last: 1.Check conversely that the recognized properties in test context
         * match the transformed data exactly (no extra entries in list).
         * 2. Ignored properties should also be in the transformed list.
         */

        for(int i=0; i<recognizedProperties.size();i++){
            assertTrue("Testing that manual recognized property was transformed: "+ recognizedProperties.get(i), TestHelper.lookFor((String) recognizedProperties.get(i), transformedData));
        }
        for(int j=0; j<ignoredProperties.size();j++){
            assertTrue("Testing that manual ignored property was transformed: "+ ignoredProperties.get(j),TestHelper.lookFor((String) ignoredProperties.get(j), transformedData));
        }
    }
}

