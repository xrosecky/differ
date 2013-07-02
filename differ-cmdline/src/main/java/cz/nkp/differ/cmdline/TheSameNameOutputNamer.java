package cz.nkp.differ.cmdline;

import cz.nkp.differ.compare.io.ImageProcessorResult;
import cz.nkp.differ.compare.metadata.ImageMetadata;

import java.awt.geom.Path2D;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: stavel
 * Date: 2.1.13
 * Time: 18:45
 */
public class TheSameNameOutputNamer implements OutputNamer {
    @Override
    public File baseName(File file, ImageProcessorResult result) {
        File outFPath = new File(file.toString().replaceFirst("[.][^.]+$", ""));
        outFPath.mkdirs();
        return outFPath;
    }

    @Override
    public File rawOutputName(File file, ImageProcessorResult result, String source) {
        File output =  new File(this.baseName(file, result),String.format("output-%s.raw",source));
        return output;
    }

    @Override
    public File rawErrorOutputName(File file, ImageProcessorResult result, String source) {
        File output =  new File(this.baseName(file, result),String.format("stderr-%s.raw",source));
        return output;
    }

    @Override
    public File reportName(File file, ImageProcessorResult result) {
        File output = new File(this.baseName(file, result),"report.drep");
        return output;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public File textName(File file, ImageProcessorResult result) {
        File output = new File(this.baseName(file, result),"report.txt");
        return output;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public File textCompareName(File file1, File file2, ImageProcessorResult results[]){
        File basename1 = this.baseName(file1,results[0]);
        File basename2 = this.baseName(file2,results[1]);
        return new File(basename1, String.format("%s-%s-report.txt", basename1.getName(), basename2.getName()));
    }

    @Override
    public File reportCompareName(File file1, File file2, ImageProcessorResult[] results) {
        File basename1 = this.baseName(file1,results[0]);
        File basename2 = this.baseName(file2,results[1]);
        return new File(basename1, String.format("%s-%s-report.drep", basename1.getName(), basename2.getName()));
    }

    @Override
    public File propertiesSummaryName(File file, ImageProcessorResult result){
        File output = new File(this.baseName(file, result),"used-properties.txt");
        return output;
    }

}
