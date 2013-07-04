package cz.nkp.differ.cmdline;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import java.util.*;
import javax.annotation.Resource;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import org.junit.Test;
import org.junit.runner.RunWith;


import cz.nkp.differ.compare.metadata.external.XSLTTransformer;
import cz.nkp.differ.compare.metadata.external.ResultTransformer.Entry;


@ContextConfiguration("classpath:appCtx-differ-cmdline-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public final class JpylyzerTest {

	/** 
	 * Class variables
	 */
    private List<Entry> manualMetadata;
    private List<Entry> transformedMetadata;

    private String filePathToRawOutput;


    /**
     * Import test subjects
     */
	@Autowired
	private XSLTTransformer jpylyzerMetadataTransformer;
    @Resource (name="metadataExtractorTests")
    private List<Map> testList;
	
	@Test
	public void test() {
		setup();

        assertNotNull(manualMetadata);
        assertNotNull(transformedMetadata);

        // Run comparison tests here

	}

    public void setup(){
        System.out.println(testList);
        for(int i = 0;i<testList.size();i++){
            //Step 1: load properties for this test
            Map m =testList.get(i);
            Object o =m.get("filePathToOutput");
            filePathToRawOutput=o.toString();

            //Step 2: run raw file through transformation
            try{
                byte[] output= readFile("../"+filePathToRawOutput); // variable is: docs/examples/images_01/14/output-jpylyzer.raw
                //TODO fix intelliJ working directory: http://justsomejavaguy.blogspot.se/2011/06/filenotfoundexception-when-running-unit.html
                transformedMetadata = jpylyzerMetadataTransformer.transform(output, null);
            }
            catch(IOException e){
                e.printStackTrace();
            }
            // Step 3: load significant properties into list
            manualMetadata=new LinkedList<Entry>();
            //Find elements and insert into structure
            o = m.get("significantProperties");
            LinkedHashMap l = (LinkedHashMap)o;
            System.out.println(l);
            o= l.get("identificationProperties");
            createNewEntries((LinkedHashMap)o);
            o= l.get("validationProperties");
            createNewEntries((LinkedHashMap)o);
            o= l.get("characterizationProperties");
            createNewEntries((LinkedHashMap)o);

            // Verify
            for(Entry e : manualMetadata){
                System.out.println(e.getKey());
            }

        }  // Loop to next test
    }

    private void createNewEntries(LinkedHashMap l){
        if(l != null){
            for(Object key : l.keySet()){
                Entry e = new Entry();
                e.setKey(key.toString());
                e.setSource("");
                e.setValue("");
                manualMetadata.add(e);
            }
        }
        else System.err.println("Could not load list from context");

    }
	private byte[] readFile(String string) throws IOException{
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
