package cz.nkp.differ.cmdline;

import static org.junit.Assert.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    // TODO set proper config
    Logger logger = LogManager.getLogger(JpylyzerTest.class.getName());


    /**
     * Import test subjects
     */
	@Autowired
	private XSLTTransformer jpylyzerMetadataTransformer;
    @Resource (name="metadataExtractorTests")
    private List<Map> testList;
	
	@Test
	public void testProperties() {
		setup();

        assertNotNull(manualMetadata);
        assertNotNull(transformedMetadata);

        // assertEquals(manualMetadata.size(),transformedMetadata.size());


        // Look for the differences between the two structures
        logger.error("Testing properties that appear in manualMetadata but not in transformedMetadata");
        significantProperties(manualMetadata, true);

        // Until config is set proper root level
        logger.error("----------------------------------");

        logger.error("Testing properties that appear in transformedMetadata but not in manualMetadata");
        significantProperties(transformedMetadata, false);

	}
    // Assuming transformed has more properties than manual
    public void significantProperties(List<Entry> list, boolean manualTest){
       int listIndex = 0;
       boolean success = false;
       Entry e;
       while(listIndex < list.size()){
          e= list.get(listIndex);
          if(manualTest==true){
            success=lookForinTransform(e.getKey());
          }
          else success=lookForinManual(e.getKey());

          if(!success) {
              logger.error(">>>>>>>>>>>>"+e.getKey()+"<<<<<<<<<< is missing...");
          }
          listIndex++;
       }

    }
    private boolean lookForinTransform(String key){
        Entry e;
        for(int i=0; i <transformedMetadata.size();i++){
            e = transformedMetadata.get(i);
            if(key.equals(e.getKey())){  // Test value equality
                logger.trace("Found!!!>>>>>>>>" + key + "<<<<<<<<<<<<");
                return true;
            }
        }
         return false;
    }
    private boolean lookForinManual(String key){
        Entry e;
        for(int i=0; i <manualMetadata.size();i++){
            e = manualMetadata.get(i);
            if(key.equals(e.getKey())){  // Test value equality
                logger.trace("Found!!!>>>>>>>>" + key + "<<<<<<<<<<<<");
                return true;
            }
        }
        return false;
    }

    public void setup(){
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
            o= l.get("identificationProperties");
            createNewEntries((LinkedHashMap)o);
            o= l.get("validationProperties");
            createNewEntries((LinkedHashMap)o);
            o= l.get("characterizationProperties");
            createNewEntries((LinkedHashMap)o);

        }  // Loop to next test TODO if more than one test run tests inside this method
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
        else logger.error("Could not load properties list from application context");

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
