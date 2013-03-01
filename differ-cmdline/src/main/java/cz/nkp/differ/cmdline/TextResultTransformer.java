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
}


public class TextResultTransformer implements ResultTransformer{
    public Boolean includeOutputs = false;
    public Boolean includeImage = false;
    public OutputNamer outputNamer = null;
    public Boolean saveReport = false;
    public Boolean saveProperties = false;

    protected ApplicationContext context;
    public TextResultTransformer(ApplicationContext context,
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
    }
    @Override
    public String transform(File file, ImageProcessorResult result) {
        List<ImageMetadata> identificationMetadata = new ArrayList<ImageMetadata>();
        List<ImageMetadata> validationMetadata = new ArrayList<ImageMetadata>();
        List<ImageMetadata> characterizationMetadata = new ArrayList<ImageMetadata>();
        List<ImageMetadata> otherMetadata = new ArrayList<ImageMetadata>();

        List<ImageMetadata>  metadataList = result.getMetadata();

        Set<String> identificationProperties = (Set<String>) context.getBean("identificationProperties");
        Set<String> validationProperties = (Set<String>) context.getBean("validationProperties");
        Set<String> characterizationProperties = (Set<String>) context.getBean("characterizationProperties");

        /* save special elements */
        for(ImageMetadata metadata: metadataList){
            String key = metadata.getKey();
            if(key.equalsIgnoreCase("Histogram")){
                saveHistogram(metadata);
            } else {
                if( key.equalsIgnoreCase("Clipping path")){
                    saveClippingPath(metadata);
                } else {
                    if (key.equalsIgnoreCase("Colormap")){
                        saveColorMap(metadata);
                    } else {
                        if( identificationProperties.contains(key)){
                            identificationMetadata.add(metadata);
                        } else {
                            if(validationProperties.contains(key)){
                                validationMetadata.add(metadata);
                            } else {
                                if( characterizationProperties.contains(key)){
                                    characterizationMetadata.add(metadata);
                                } else {
                                    otherMetadata.add(metadata);
                                }
                            }
                        }
                    }
                }
            }
        }
        String output = "";
        output += "Identification\n";
        output += "==============\n\n";
        output += String.format("%s: %sx%s\n\n",file.getName(),result.getWidth(),result.getHeight());
        output += reportMetadataList(identificationMetadata);

        output += "\nValidation";
        output += "\n==========\n\n";
        output += reportMetadataList(validationMetadata);

        output += "\nCharacterization";
        output += "\n================\n\n";
        output += reportMetadataList(characterizationMetadata);

        output += "\nOther properties";
        output += "\n================\n\n";
        output += reportMetadataList(otherMetadata);

        if( this.includeOutputs ){
            output += "\nRaw outputs of extractors";
            output += "\n=========================\n\n";
            for(ImageMetadata metadata: result.getMetadata()){
                if( metadata.getKey().equals("exit-code") ){
                    File outFile = this.outputNamer.rawOutputName(file, result,metadata.getSource().toString());
                    FileWriter writer;
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
                    File errorOutputFile = this.outputNamer.rawErrorOutputName(file, result,metadata.getSource().toString());
                    writer = null;
                    try {
                        output += String.format("   %-10s   'stderr output <%s>'_\n",
                                metadata.getSource().toString(),
                                outFile
                        );
                        writer = new FileWriter(errorOutputFile);
                        writer.write(metadata.getSource().getStderr());
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
            output += "\nReport for web";
            output += "\n===========\n";
            output += String.format("\n  `web report <%s>`_\n", this.outputNamer.reportName(file, result));
        }
        if( this.saveProperties ){
            output += "\nUsed significant properties";
            output += "\n===========================\n";
            output += String.format("\n  `used properties <%s>`_",
                        this.outputNamer.propertiesSummaryName(file, result)
            );
        }
        return output;
    }

    public String reportMetadataList(List<ImageMetadata> metadataList){
        Integer keyLength = "Significant Property".length();
        Integer sourceLength = "Source".length();
        Integer unitLength = "Unit".length();
        Integer valueWithUnitLength = "Value".length();
        Integer valueLength = "Value".length();
        PropertiesSummary propertiesSummary = new PropertiesSummary();

        for(ImageMetadata metadata: metadataList){
            keyLength = Math.max(keyLength, metadata.getKey().length());
            sourceLength = Math.max(sourceLength, metadata.getSource().toString().length());
            if (metadata.getUnit() != null) {
                unitLength = Math.max(unitLength, metadata.getUnit().length());
                if (metadata.getValue() != null){
                    valueWithUnitLength = Math.max(valueWithUnitLength, metadata.getValue().toString().length());
                    valueLength = Math.max(valueLength, valueWithUnitLength + 1 + metadata.getUnit().length());
                }
            } else {
                if (metadata.getValue() != null) valueLength =
                        Math.max(valueLength, metadata.getValue().toString().length());
            }
            propertiesSummary.addProperty(metadata.getKey());
        }
        Collections.sort(metadataList, new Comparator<ImageMetadata>() {
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
        String format = String.format("%%-%ds %%-%ds  %%-%ds\n", keyLength, sourceLength,valueLength);
        String formatWithUnit = String.format("%%-%ds %%s", valueWithUnitLength );
        output += String.format(format, "Significant Property", "Source", "Value");
        output += String.format(format,
                getStringGivenLength(keyLength,'-'),
                getStringGivenLength(sourceLength,'-'),
                getStringGivenLength(valueLength,'-'));

        for (ImageMetadata metadata: metadataList) {
            output += String.format(format,
                    propertyNameHider.getOrHide(metadata.getKey()),
                    metadata.getSource(),
                    metadata.getUnit() != null ? String.format(formatWithUnit,metadata.getValue(),
                                                               metadata.getUnit()) :
                            metadata.getValue()
                    );
        }
        output += String.format(format,
                getStringGivenLength(keyLength,'-'),
                getStringGivenLength(sourceLength,'-'),
                getStringGivenLength(valueLength,'-'),
                getStringGivenLength(unitLength, '-'));

        return output;
    }

    private void saveHistogram(ImageMetadata metadata){
           /* Todo: doplnit ukladani histogramu do souboru */
    }

    private void saveClippingPath(ImageMetadata metadata){
        /* Todo: doplnit ukladani clipping path */
    }

    private void saveColorMap(ImageMetadata metadata) {
        /* Todo: */
    }
}
