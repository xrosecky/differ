package cz.nkp.differ.cmdline;

import com.thoughtworks.xstream.XStream;
import cz.nkp.differ.compare.io.ImageProcessorResult;
import cz.nkp.differ.compare.metadata.ImageMetadata;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: stavel
 * Date: 2.1.13
 * Time: 18:45
 * To change this template use File | Settings | File Templates.
 */
public class TheSameNameResultSaver implements ResultSaver {
    @Override
    public void save(ImageProcessorResult result) {
        String outFPath = "imageResult.dres";
        for(ImageMetadata metadata: result.getMetadata()){
            if(metadata.getKey().equals("File name")){
                File file = new File((String) metadata.getValue());
                outFPath = file.getPath().split("\\.")[0] + ".dres";
                break;
            }
        };
        System.out.println(outFPath);
        FileWriter out = null;
        try {
            out = new FileWriter(outFPath);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        XStream xstream = new XStream();
        if (out != null) {
            try {
                out.write(xstream.toXML(result));
                out.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
}
