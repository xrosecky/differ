package cz.nkp.differ.cmdline;

import java.util.ArrayList;

/**
 * User: Jonatan Svensson <jonatansve@gmail.com>
 * Date: 2013-07-01
 * Time: 19:11
 */
public class MetadataHolder {


    /**
     * Data structure to save values from raw jpylyzer output
     * and tags after xslt transformation
     *
     * Class only used for testing of xslt transformations
     */
    private ArrayList<StringTriple> holder;

    public MetadataHolder(){
        holder = new ArrayList<StringTriple>();
    }

    private void addEntry(String tag, String value, String postProcessingTag){
        holder.add(new StringTriple(tag, value, postProcessingTag));
    }

    private ArrayList<StringTriple> getMetadata(){
        return holder;
    }

    public ArrayList<StringTriple> setMetadataManually(){
        holder.add(new StringTriple("jpylyzer/toolInfo/toolName","jpylyzer","Version"));
        holder.add(new StringTriple("jpylyzer/toolInfo/toolVersion","1.6.0","Version"));
        holder.add(new StringTriple("jpylyzer/fileInfo/fileName","03-PSCS5.jp2","File name"));
        holder.add(new StringTriple("jpylyzer/fileInfo/filePath","/home/klas/workspace/differ/docs/images/03-PSCS5.jp2","File path"));
        holder.add(new StringTriple("jpylyzer/fileInfo/fileSizeInBytes","9227854","File size"));
        holder.add(new StringTriple("","",""));

        return holder;
    }

}
class StringTriple{

    public final String tag;
    public final String value;
    public final String postProcessingTag;
    public StringTriple(String tag, String value, String postProcessingTag) {
        this.tag=tag;
        this.value=value;
        this.postProcessingTag=postProcessingTag;
    }
}
