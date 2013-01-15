package cz.nkp.differ.cmdline;

import cz.nkp.differ.compare.io.ImageProcessorResult;
import cz.nkp.differ.compare.metadata.ImageMetadata;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: stavel
 * Date: 2.1.13
 * Time: 18:45
 */
public class TheSameNameOutputNamer implements OutputNamer {
    @Override
    public String baseName(ImageProcessorResult result) {
        String outFPath = "imageResult";
        for(ImageMetadata metadata: result.getMetadata()){
            if(metadata.getKey().equals("File name")){
                File file = new File((String) metadata.getValue());
                outFPath = file.getPath().split("\\.")[0];
                break;
            }
        }
        return outFPath;
    }

    @Override
    public String rawOutputName(ImageProcessorResult result, String source) {
        String output = String.format("%s-output-%s.raw",this.baseName(result),source);
        return output;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String reportName(ImageProcessorResult result) {
        String output = String.format("%s-report.drep",this.baseName(result));
        return output;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String textName(ImageProcessorResult result) {
        String output = String.format("%s-report.txt", this.baseName(result));
        return output;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String textCompareName(ImageProcessorResult result1, ImageProcessorResult result2){
        String basename1 = this.baseName(result1);
        String basename2 = this.baseName(result2);
        return String.format("%s-%s-report.txt", basename1, basename2);
    }
}
