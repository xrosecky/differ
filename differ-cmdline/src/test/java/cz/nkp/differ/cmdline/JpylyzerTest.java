package cz.nkp.differ.cmdline;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

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
	private List<StringTriple> metadata;
    private List<Entry> transformedMetadata;

    /**
     * Import test subjects
     */
	@Autowired
	private XSLTTransformer jpylyzerMetadataTransformer;
	
	
	
	@Test
	public void test() {
		setup();


        // Run comparison tests here

        assertTrue(true);
	}

    public void setup(){
        //Step 1: load properties from file into structure
        metadata = new MetadataHolder().setMetadataManually();
        for(StringTriple e: metadata)  {
            System.out.println(e.tag+"++"+e.value+"++"+e.postProcessingTag);
        }
        //Step 2: run file through xslt
        try{
            byte[] output= readFile("src/test/resources/output-jpylyzer.xml");
            //TODO fix intelliJ working directory: http://justsomejavaguy.blogspot.se/2011/06/filenotfoundexception-when-running-unit.html
            transformedMetadata = jpylyzerMetadataTransformer.transform(output, null);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        if(transformedMetadata != null){
            for(Entry e: transformedMetadata)  {
                System.out.println(e.getKey()+"++value:"+e.getValue());
            }
        }
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
