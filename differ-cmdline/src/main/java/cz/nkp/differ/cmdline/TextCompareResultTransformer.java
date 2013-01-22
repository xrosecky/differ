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

public class TextCompareResultTransformer implements CompareResultTransformer {
    public Boolean includeOutputs = false;
    public Boolean includeImage = false;
    public Boolean saveReport = false;
    public OutputNamer outputNamer = null;
    public Boolean saveProperties = false;

    public TextCompareResultTransformer(OutputNamer outputNamer,
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
    public String transform(File files[], ImageProcessorResult results[]) throws Exception{
        Integer keyLength = "Significant Property".length();
        Integer sourceLength = "Source".length();
        Integer unitLength = "Unit".length();
        Integer valueLength = "Value".length();
        Integer [] valueWithUnitLength = new Integer [2];
        ImageMetadata [] exitCodeMetadata = new ImageMetadata[2];

        valueWithUnitLength[0] = "Value".length();
        valueWithUnitLength[1] = "Value".length();

        HashMap<String, HashMap<String, ImageMetadata[]>> metadataByKeyName =
                new HashMap<String, HashMap<String,ImageMetadata[]>>();
        PropertiesSummary propertiesSummary = new PropertiesSummary();

        for(int imageOrder = 0;imageOrder < 2; imageOrder++ ){
            ImageProcessorResult result = results[imageOrder];
            for(ImageMetadata metadata: result.getMetadata()){

                String key = metadata.getKey();
                String source = metadata.getSource().toString();

                if (key.equals("exit-code")){
                    exitCodeMetadata[imageOrder] = metadata;
                } else {
                    keyLength = Math.max(keyLength, key.length());
                    sourceLength = Math.max(sourceLength, source.length());
                    if( metadata.getUnit() != null) unitLength = Math.max(unitLength,metadata.getUnit().length());
                    if( metadata.getValue() != null){
                        if( metadata.getUnit() != null ){
                            valueWithUnitLength[imageOrder] = Math.max(
                                    valueWithUnitLength[imageOrder],
                                    metadata.getValue().toString().length()
                                    );
                        }
                        valueLength = Math.max(valueLength,metadata.getValue().toString().length());
                    }
                    propertiesSummary.addProperty(key);
                    if( ! metadataByKeyName.containsKey(key)){
                        metadataByKeyName.put(key, new HashMap<String, ImageMetadata[]>());
                    }
                    if( ! metadataByKeyName.get(key).containsKey(source)){
                        metadataByKeyName.get(key).put(source, new ImageMetadata[2] );
                    }
                    metadataByKeyName.get(key).get(source)[imageOrder] = metadata;
                }
            }
        }

        TheSameValueHider propertyNameHider = new TheSameValueHider();
        String output = "";

        Integer fileNameLength = Math.max(files[0].toString().length(), files[1].toString().length());
        output += "Characterization\n";
        output += "================\n\n";
        output += String.format("  Image A :: %s: %sx%s\n", files[0].toString(), results[0].getHeight(), results[0].getWidth());
        output += String.format("  Image B :: %s: %sx%s\n", files[1].toString(), results[1].getHeight(), results[1].getWidth());
        output += "\nSignificant Properties\n";
        output += "======================\n\n";
        String format = String.format("%%-%ds  %%-%ds  %%-%ds  %%-%ds\n",
               keyLength, sourceLength, valueLength, valueLength);
        String [] valueFormats = new String [2];
        for ( int imageOrder = 0; imageOrder < 2; imageOrder++){
            valueFormats[imageOrder] = String.format("%%-%ds %%s", valueWithUnitLength[imageOrder]);
        }
        TreeSet<String> keys = new TreeSet<String>();
        for( String key: metadataByKeyName.keySet()){
            keys.add(key);
        }
        output += String.format(format,"Significant Property", "Source", "Value for Image A", "Value for Image B");
        output += String.format(format,
                getStringGivenLength(keyLength,'-'),
                getStringGivenLength(sourceLength,'-'),
                getStringGivenLength(valueLength,'-'),
                getStringGivenLength(valueLength,'-'));
        for( String key: keys){
            HashMap<String, ImageMetadata[]> metadataBySource = metadataByKeyName.get(key);
            for( String source: metadataBySource.keySet() ){
                ImageMetadata[] pair = metadataBySource.get(source);
                String unit = (pair[0] != null ? pair[0].getUnit() : (pair[1] != null ? pair[1].getUnit() : ""));
                output += String.format(format,
                        propertyNameHider.getOrHide(key),
                        source,
                        String.format(valueFormats[0],
                                (pair[0] != null ? pair[0].getValue() : ""),
                                (unit != null ? unit : "")),
                        String.format(valueFormats[1],
                                (pair[1] != null ? pair[1].getValue() : ""),
                                (unit != null ? unit : ""))
                        );
            }
        }
        output += String.format(format,
                getStringGivenLength(keyLength,'-'),
                getStringGivenLength(sourceLength,'-'),
                getStringGivenLength(valueLength,'-'),
                getStringGivenLength(valueLength,'-'));

        if( this.includeOutputs ){
            output += "\nRaw outputs of extractors";
            output += "\n=========================\n\n";
            for(int imageOrder = 0; imageOrder < 2; imageOrder++){
                File outFile = this.outputNamer.rawOutputName(files[imageOrder], results[imageOrder],
                        exitCodeMetadata[imageOrder].getSource().toString());
                FileWriter writer = null;
                output += String.format("   %-10s   'output <%s>'_\n",
                            exitCodeMetadata[imageOrder].getSource().toString(),
                            outFile
                );
                writer = new FileWriter(outFile);
                writer.write(exitCodeMetadata[imageOrder].getSource().getStdout());
                writer.close();
            }
        }

        if( this.saveReport ){
            output += "\nText reports";
            output += "\n============\n\n";

            for(int imageOrder = 0; imageOrder < 2; imageOrder++){
                File outFile = this.outputNamer.textName(files[imageOrder], results[imageOrder]);
                output += String.format("   %-10s   'text report <%s>'_\n",
                        files[imageOrder].toString(),
                        outFile
                );
            }
            output += String.format("\n  `text report <%s>`_\n", this.outputNamer.textCompareName(
                    files[0],
                    files[1],
                    results[0],
                    results[1]
                    ));
            FileWriter writer = new FileWriter(this.outputNamer.textCompareName(files[0], files[1], results[0],results[1]));
            writer.write(output);
            writer.close();
        }

        if( this.saveProperties) {
            output += "\nUsed significant properties";
            output += "\n===========================\n";
            for( int imageOrder=0; imageOrder < 2; imageOrder++){
                output += String.format("\n  `used properties <%s>`_",
                        this.outputNamer.propertiesSummaryName(files[imageOrder], results[imageOrder])
                );

                File outFile = this.outputNamer.propertiesSummaryName(files[imageOrder], results[imageOrder]);
                FileWriter writer = null;
                try {
                    writer = new FileWriter(outFile);
                    for(String property: propertiesSummary.getProperties()){
                        writer.write(String.format("1;%s\n",property));
                    }
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return output;
    }
}
