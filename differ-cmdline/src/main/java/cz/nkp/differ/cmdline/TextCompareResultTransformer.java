package cz.nkp.differ.cmdline;

import cz.nkp.differ.compare.io.ImageProcessorResult;
import cz.nkp.differ.compare.metadata.ImageMetadata;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


/**
 * Created with IntelliJ IDEA.
 * User: stavel
 * Date: 5.1.13
 * Time: 14:11
 */

public class TextCompareResultTransformer implements CompareResultTransformer {
    public Boolean includeOutputs = false;
    public Boolean includeImage = false;
    public Boolean saveReport = false;
    public OutputNamer outputNamer = null;
    public Boolean saveProperties = false;

    private ApplicationContext context;

    private class MetadataGroup {
        Integer [] valueWithUnitLength = new Integer [2];
        HashMap<String, HashMap<String, ImageMetadata[]>> metadataByKeyName =
                new HashMap<String, HashMap<String,ImageMetadata[]>>();

        Integer keyLength = "Significant Property".length();
        Integer sourceLength = "Source".length();
        Integer unitLength = "Unit".length();
        Integer valueLength = "Value".length();

        MetadataGroup(){
            valueWithUnitLength[0] = "Value".length();
            valueWithUnitLength[1] = "Value".length();
        }
        public void add(ImageMetadata metadata, int imageOrder){
            String key = metadata.getKey();
            String source = metadata.getSource().toString();
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
            if( ! metadataByKeyName.containsKey(key)){
                metadataByKeyName.put(key, new HashMap<String, ImageMetadata[]>());
            }
            if( ! metadataByKeyName.get(key).containsKey(source)){
                metadataByKeyName.get(key).put(source, new ImageMetadata[2] );
            }
            metadataByKeyName.get(key).get(source)[imageOrder] = metadata;
        }
        public String toString(){
            String output = "";
            TheSameValueHider propertyNameHider = new TheSameValueHider();
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

            return output;
        }
    }
    public TextCompareResultTransformer(ApplicationContext context,
                                        OutputNamer outputNamer,
                                        Boolean includeOutputs,
                                        Boolean saveReport,
                                        Boolean saveProperties,
                                        Boolean includeImage) {
        this.includeOutputs = includeOutputs;
        this.saveReport = saveReport;
        this.includeImage = includeImage;
        this.outputNamer = outputNamer;
        this.saveProperties = saveProperties;
        this.context = context;
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
        ImageMetadata [] exitCodeMetadata = new ImageMetadata[2];
        PropertiesSummary propertiesSummary = new PropertiesSummary();

        Set<String> extractorProperties = (Set<String>) context.getBean("extractorProperties");
        Set<String> identificationProperties = (Set<String>) context.getBean("identificationProperties");
        Set<String> validationProperties = (Set<String>) context.getBean("validationProperties");
        Set<String> characterizationProperties = (Set<String>) context.getBean("characterizationProperties");

        MetadataGroup characterizationMetadataGroup = new MetadataGroup();
        MetadataGroup validationMetadataGroup = new MetadataGroup();
        MetadataGroup identificationMetadataGroup = new MetadataGroup();
        MetadataGroup extractorMetadataGroup = new MetadataGroup();
        MetadataGroup otherMetadataGroup = new MetadataGroup();

        for(int imageOrder = 0;imageOrder < 2; imageOrder++ ){
            ImageProcessorResult result = results[imageOrder];
            for(ImageMetadata metadata: result.getMetadata()){
                String key = metadata.getKey();
                propertiesSummary.addProperty(key);
                if (key.equals("exit-code")){
                    exitCodeMetadata[imageOrder] = metadata;
                } else {
                    if( identificationProperties.contains(key)){
                        identificationMetadataGroup.add(metadata,imageOrder);
                    } else {
                        if( validationProperties.contains(key)){
                            validationMetadataGroup.add(metadata,imageOrder);
                        } else {
                            if( characterizationProperties.contains(key)){
                                characterizationMetadataGroup.add(metadata,imageOrder);
                            } else {
                                if(     !key.equals("Clipping path") &&
                                        !key.equals("Histogram") &&
                                        !key.equals("Colormap")
                                        ){
                                    if( extractorProperties.contains(key) ){
                                        extractorMetadataGroup.add(metadata, imageOrder);
                                    } else {
                                        otherMetadataGroup.add(metadata,imageOrder);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        String output = "";
        output += "Used extractors\n";
        output += "===============\n\n";
        output += extractorMetadataGroup.toString();
        output += "\nIdentification";
        output += "\n==============\n\n";
        output += String.format("  Image A :: %s: %sx%s\n", files[0].toString(), results[0].getHeight(), results[0].getWidth());
        output += String.format("  Image B :: %s: %sx%s\n\n", files[1].toString(), results[1].getHeight(), results[1].getWidth());
        output += identificationMetadataGroup.toString();
        output += "\nValidation";
        output += "\n==========\n\n";
        output += validationMetadataGroup.toString();
        output += "\nCharacterization";
        output += "\n================\n\n";
        output += characterizationMetadataGroup.toString();
        output += "\nOther properties";
        output += "\n================\n\n";
        output += otherMetadataGroup.toString();

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
            output += String.format("\n  `text comparison report <%s>`_\n", this.outputNamer.textCompareName(
                    files[0], files[1], results
                    ));

            output += "\nReports for web";
            output += "\n============\n\n";

            for(int imageOrder = 0; imageOrder < 2; imageOrder++){
                File outFile = this.outputNamer.reportName(files[imageOrder], results[imageOrder]);
                output += String.format("   %-10s   'web report <%s>'_\n",
                        files[imageOrder].toString(),
                        outFile
                );
            }
            output += String.format("\n  `web comparison report <%s>`_\n", this.outputNamer.reportCompareName(
                    files[0], files[1], results
            ));

            FileWriter writer = new FileWriter(this.outputNamer.textCompareName(files[0], files[1], results));
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
