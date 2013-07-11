package cz.nkp.differ.cmdline;

import cz.nkp.differ.compare.metadata.external.*;
import cz.nkp.differ.compare.metadata.external.ResultTransformer;
import cz.nkp.differ.compare.metadata.external.result.MetadataResultTransformer;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Stavel
 * Date: 9.7.13
 * Time: 21:39
 * To change this template use File | Settings | File Templates.
 */


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:appCtx-differ-cmdline-test.xml"})
public class JpylyzerUnitTest {
    @Autowired
    private Map<String,Object> image14Test01;

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void testJpylyzer() throws Exception {
        ResultTransformer transformer =  (ResultTransformer) image14Test01.get("transformer");
        //ResultTransformer transformer = applicationContext.getBean("jpylyzerMetadataTransformer");
        byte[] stdout = readFile("/opt/differ/docs/examples/images_01/14/output-jpylyzer.raw");
        List<ResultTransformer.Entry> transformedData = transformer.transform(stdout,null);
        Assert.assertEquals("test for a word ahoj", "ahoj", "aho");
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
