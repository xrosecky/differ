package cz.nkp.differ.cmdline;

import cz.nkp.differ.compare.io.ImageProcessorResult;
import cz.nkp.differ.compare.metadata.ImageMetadata;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


/**
 * Created with IntelliJ IDEA.
 * User: stavel
 * Date: 5.1.13
 * Time: 14:11
 * To change this template use File | Settings | File Templates.
 */

class MetadataWithImageName {
    /*
      The class will hold metadata even with image name.
      It is important to join two results into one text/html table to compare them.
      There is necesary to show a image name that a metadata relates to.
     */
    public String imageName;
    public ImageMetadata metadata;

    MetadataWithImageName (String imageName, ImageMetadata metadata){
        this.metadata = metadata;
        this.imageName = imageName;
    }
}

public class TextCompareResultTransformer implements CompareResultTransformer {
    public Boolean includeOutputs = false;
    public Boolean includeImage = false;
    public OutputNamer outputNamer = null;

    public TextCompareResultTransformer(OutputNamer outputNamer, Boolean includeOutputs, Boolean includeImage) {
        this.includeOutputs = includeOutputs;
        this.includeImage = includeImage;
        this.outputNamer = outputNamer;
    }

    @Override
    public String transform(ImageProcessorResult results[]) {
        String fileName = null;
        Integer keyLength = 0;
        Integer sourceLength = 0;
        String fileName0 = null;
        String fileName1 = null;
        HashMap<String, ArrayList<MetadataWithImageName>> metadataByKeyName =
                new HashMap<String, ArrayList<MetadataWithImageName>>();

        ImageProcessorResult result = results[0];
        for(ImageMetadata metadata: result.getMetadata()){
            if( metadata.getKey().equals("File name") ){
                fileName = (String) metadata.getValue();
            };
            keyLength = Math.max(keyLength, metadata.getKey().length());
            sourceLength = Math.max(sourceLength, metadata.getSource().toString().length());
        }
        fileName0 = fileName;
        for(ImageMetadata metadata: result.getMetadata()){
            String key = metadata.getKey();
            if( ! metadataByKeyName.containsKey(key)){
                metadataByKeyName.put(key, new ArrayList<MetadataWithImageName>());
            }
            metadataByKeyName.get(key).add(new MetadataWithImageName(fileName, metadata));
        }
        result = results[1];
        for(ImageMetadata metadata: result.getMetadata()){
            if( metadata.getKey().equals("File name") ){
                fileName = (String) metadata.getValue();
            };
            keyLength = Math.max(keyLength, metadata.getKey().length());
            sourceLength = Math.max(sourceLength, metadata.getSource().toString().length());
        }
        fileName1 = fileName;
        for(ImageMetadata metadata: result.getMetadata()){
            String key = metadata.getKey();
            if( ! metadataByKeyName.containsKey(key)){
                metadataByKeyName.put(key, new ArrayList<MetadataWithImageName>());
            }
            metadataByKeyName.get(key).add(new MetadataWithImageName(fileName, metadata));
        }

        TheSameValueHider propertyNameHider = new TheSameValueHider();
        String output = "";

        Integer fileNameLength = Math.max(fileName0.length(), fileName1.length());
        output += "Characterization\n";
        output += "================\n\n";
        output += String.format("%s: %sx%s\n", fileName0, results[0].getHeight(), results[0].getWidth());
        output += String.format("%s: %sx%s\n", fileName1, results[1].getHeight(), results[1].getWidth());
        output += "\nSignificant Properties\n";
        output += "======================\n\n";
        String format = String.format("%%-%ds  %%-%ds  %%-%ds  %%s\n", keyLength, sourceLength, fileNameLength);

        TreeSet<String> keys = new TreeSet<String>();
        for( String key: metadataByKeyName.keySet()){
            keys.add(key);
        }
        for( String key: keys){
            ArrayList<MetadataWithImageName> metadataWithImageNames = metadataByKeyName.get(key);
            for( MetadataWithImageName metadataWithImageName: metadataWithImageNames  ){
                output += String.format(format,
                        propertyNameHider.getOrHide(metadataWithImageName.metadata.getKey())
                        ,metadataWithImageName.metadata.getSource(),
                        metadataWithImageName.imageName,
                        metadataWithImageName.metadata.getValue());
            }
        }
        if( this.includeOutputs ){
            output += "\nRaw outputs of extractors";
            output += "\n=========================\n\n";
            result = results[0];
            for(ImageMetadata metadata: result.getMetadata()){
                if( metadata.getKey().equals("exit-code") ){
                    File outFile = new File(this.outputNamer.rawOutputName(result,metadata.getSource().toString()));
                    FileWriter writer = null;
                    try {
                        output += String.format("   %-10s   'output <%s>'_\n",
                                metadata.getSource().toString(),
                                outFile
                        );
                        writer = new FileWriter(outFile);
                        writer.write(metadata.getSource().getStdout());
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            result = results[1];
            for(ImageMetadata metadata: result.getMetadata()){
                if( metadata.getKey().equals("exit-code") ){
                    File outFile = new File(this.outputNamer.rawOutputName(result,metadata.getSource().toString()));
                    FileWriter writer = null;
                    try {
                        output += String.format("   %-10s   'output <%s>'_\n",
                                metadata.getSource().toString(),
                                outFile
                        );
                        writer = new FileWriter(outFile);
                        writer.write(metadata.getSource().getStdout());
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return output;
    }
}
