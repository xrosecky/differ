package cz.nkp.differ.cmdline;

import cz.nkp.differ.compare.metadata.external.ResultTransformer;
import cz.nkp.differ.compare.metadata.external.ResultTransformer.Entry;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Stavel
 * Date: 9.7.13
 * Time: 21:39
 * To change this template use File | Settings | File Templates.
 */


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:jpylyzerTestsCtx.xml"})
public class JpylyzerUnitTest {
    @Autowired
    private Map<String,Object> image14Test01;

    @Autowired
    private ResultTransformer jpylyzerMetadataTransformer;

    @Test
    public void testImage14() throws Exception {
        byte[] stdout = readFile("../docs/examples/images_01/14/output-jpylyzer.raw");
        List<ResultTransformer.Entry> transformedData = jpylyzerMetadataTransformer.transform(stdout,null);
        assertNotNull(transformedData);

        /**
         * Compare transformedData with list of
         * manual input of significant properties in image14Test01RecognizedProperties
         * Fails if a property is transformed but is yet not mapped.
         */

        ArrayList recognizedProperties = (ArrayList) image14Test01.get("image14Test01RecognizedProperties");
        assertNotNull(recognizedProperties);
        for(Entry e: transformedData){
            assertTrue("Testing that transformed property is recognized: "+ e.getKey(),recognizedProperties.contains(e.getKey()));
        }


        /**
         * Go through each entry in transformedData,
         * Look for the key in:
         * identificationProperties/validationProperties/characterizationProperties,
         * Assert that the value is identical.
         */

        LinkedHashMap l = (LinkedHashMap) image14Test01.get("image14SignificantProperties");
        LinkedHashMap lh1 = (LinkedHashMap) l.get("identificationProperties");
        LinkedHashMap lh2 = (LinkedHashMap) l.get("validationProperties");
        LinkedHashMap lh3 = (LinkedHashMap) l.get("characterizationProperties");

        String s;

        for(Entry e: transformedData){
            s= (String)lh1.get(e.getKey());
            if(s==null) {
                s= (String)lh2.get(e.getKey());
                if(s==null){
                    s= (String)lh3.get(e.getKey());
                }
            }  // If s is null here, then the entry is missing in manual data

            System.out.println("Testing: "+e.getKey()+ " with: "+ s);

            if(s!= null){
                System.out.println("Checking equality: "+ e.getValue() + " with: "+ s);
                assertEquals("Testing: "+e.getKey(), e.getValue(), s);
            }
            s=null;
        }
    }




    private byte[] readFile(String string) throws IOException {
        RandomAccessFile f = new RandomAccessFile(new File(string), "r");

        try {
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size >= 2 GB");
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        }catch(IOException e){
            e.printStackTrace();
        }
        finally {
            f.close();
        }
        return null;
    }
}
