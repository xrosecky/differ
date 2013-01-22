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
class TheSameValueHider {
    /*
        The class will remember last used value.
        If next value is the same, it will return empty string.

        This is for easier reading rows with the same significant property
        but from other source.
     */
    private String lastValue = "";

    public String getOrHide(String value){
        if( lastValue.equals(value)){
            return "";
        }
        lastValue = value;
        return value;
    }
};


public class TextResultTransformer implements ResultTransformer{
    public Boolean includeOutputs = false;
    public Boolean includeImage = false;
    public OutputNamer outputNamer = null;
    public Boolean saveReport = false;
    public Boolean saveProperties = false;

    public TextResultTransformer(OutputNamer outputNamer,
                                 Boolean includeOutputs,
                                 Boolean saveReport,
                                 Boolean saveProperties,
                                 Boolean includeImage) {
        this.includeOutputs = includeOutputs;
        this.saveReport = saveReport;
        this.includeImage = includeImage;
        this.outputNamer = outputNamer;
        this.saveProperties = saveProperties;
    }

    protected String getStringGivenLength(int length, char chr) {
        if( length > 0 ){
            char [] array = new char[length];
            Arrays.fill(array, chr);
            return new String(array);
        }
        return "";
    };

    @Override
    public String transform(File file, ImageProcessorResult result) {
        String fileName = null;
        Integer keyLength = "Significant Property".length();
        Integer sourceLength = "Source".length();
        Integer unitLength = "Unit".length();
        Integer valueLength = "Value".length();
        PropertiesSummary propertiesSummary = new PropertiesSummary();

        for(ImageMetadata metadata: result.getMetadata()){
            if( metadata.getKey().equals("File name") ){
                fileName = (String) metadata.getValue();
            };
            keyLength = Math.max(keyLength, metadata.getKey().length());
            sourceLength = Math.max(sourceLength, metadata.getSource().toString().length());
            if (metadata.getUnit() != null) unitLength = Math.max(unitLength, metadata.getUnit().length());
            if (metadata.getValue() != null) valueLength = Math.max(valueLength, metadata.getValue().toString().length());
            propertiesSummary.addProperty(metadata.getKey());
        }
        Collections.sort(result.getMetadata(), new Comparator<ImageMetadata>() {
            @Override
            public int compare(ImageMetadata imageMetadata1, ImageMetadata imageMetadata2) {
                String key1 = imageMetadata1.getKey();
                String key2 = imageMetadata2.getKey();
                if( key1.compareToIgnoreCase(key2) == 0){
                    String source1 = imageMetadata1.getSource().toString();
                    String source2 = imageMetadata2.getSource().toString();
                    return source1.compareToIgnoreCase(source2);
                }
                return key1.compareToIgnoreCase(key2);
            }
        });
        TheSameValueHider propertyNameHider = new TheSameValueHider();
        String output = "";
        output += "Characterization\n";
        output += "================\n\n";
        output += String.format("%s: %sx%s\n", fileName, result.getHeight(), result.getWidth());
        output += "\nSignificant Properties\n";
        output += "======================\n\n";
        String format = String.format("%%-%ds %%-%ds  %%-%ds  %%s\n", keyLength, unitLength, sourceLength);
        output += String.format(format, "Significant Property", "Unit", "Source", "Value");
        output += String.format(format,
                getStringGivenLength(keyLength,'-'),
                getStringGivenLength(unitLength, '-'),
                getStringGivenLength(sourceLength,'-'),
                getStringGivenLength(valueLength,'-'));
        for (ImageMetadata metadata: result.getMetadata()) {
            output += String.format(format,
                    propertyNameHider.getOrHide(metadata.getKey()),
                    metadata.getUnit() != null ? metadata.getUnit() : "",
                    metadata.getSource(),
                    metadata.getValue());
        }
        output += String.format(format,
                getStringGivenLength(keyLength,'-'),
                getStringGivenLength(unitLength, '-'),
                getStringGivenLength(sourceLength,'-'),
                getStringGivenLength(valueLength,'-'));
        if( this.includeOutputs ){
            output += "\nRaw outputs of extractors";
            output += "\n=========================\n\n";
            for(ImageMetadata metadata: result.getMetadata()){
                if( metadata.getKey().equals("exit-code") ){
                    File outFile = this.outputNamer.rawOutputName(file, result,metadata.getSource().toString());
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
        if( this.saveReport ){
            output += "\nText report";
            output += "\n===========\n";
            output += String.format("\n  `text report <%s>`_\n", this.outputNamer.textName(file, result));
        }
        if( this.saveProperties ){
            output += "\nUsed significant properties";
            output += "\n===========================\n\n";
            TreeSet<String> properties = propertiesSummary.getProperties();
            for(String property: properties){
                output += String.format("   %s\n", property);
            }
        }
        return output;
    }
}
