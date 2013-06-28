package cz.nkp.differ.compare.io;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import cz.nkp.differ.compare.metadata.ImageMetadata;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Button.ClickEvent;

import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.HorizontalLayout;
import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.compare.metadata.MetadataSource;
import cz.nkp.differ.gui.windows.RawDataWindow;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Thomas Truax
 */
public class ImageMetadataTableGen {

    private ImageProcessorResult[] result;
    private List<String> nonConflictMetadata = Arrays.asList("exit-code");
    private CompareComponent parent;

    /**
     * Constructor which takes a single ImageProcessorResult object (usually for comparison table)
     * @param ImageProcessorResult
     * @param CompareComponent 
     */
    public ImageMetadataTableGen(ImageProcessorResult result, CompareComponent parent) {
        this.result = new ImageProcessorResult[] {result};
        this.parent = parent;
    }
    
    /**
     * Constructor which takes an array of ImageProcessorResult objects for multiple images
     * @param ImageProcessorResult[]
     * @param CompareComponent
     */
    public ImageMetadataTableGen(ImageProcessorResult[] result, CompareComponent parent) {
        this.result = result;
        this.parent = parent;
    }
    
    /**
     * Retrieves table component after it has been generated
     * @return Layout
     */
    public Layout getComponent() {
        HorizontalLayout layout = new HorizontalLayout();
        generateMetadataTable(layout);
        return layout;
    }
    
    /**
     * Builds the table 
     * @param Layout 
     */
    private void generateMetadataTable(Layout layout) {
        final Table metadataTable;
                
        if (result.length == 2) {

            //Cannot use a BeanItemContainer here, since table no longer structured to match the ImageMetadata object

            //BeanItemContainer metadataContainer = new BeanItemContainer<ImageMetadata>(ImageMetadata.class, result.getMetadata());
            //metadataContainer.sort(new String[]{"key"}, new boolean[]{true});
            //final Table metadataTable = new Table("Metadata", metadataContainer);

            metadataTable = new Table("Metadata");
            metadataTable.addContainerProperty("key", String.class, null);
            metadataTable.addContainerProperty("source", MetadataSource.class, null);
            metadataTable.addContainerProperty("image A value", Object.class, null);
            metadataTable.addContainerProperty("image B value", Object.class, null);
            metadataTable.addContainerProperty("unit", String.class, null);
            for (int i = 0; i < result[0].getMetadata().size(); i++) {
                metadataTable.addItem(new Object[] {result[0].getMetadata().get(i).getKey(),
                                                    result[0].getMetadata().get(i).getSource(),
                                                    result[0].getMetadata().get(i).getValue(),
                                                    result[1].getMetadata().get(i).getValue(),
                                                    result[0].getMetadata().get(i).getUnit()
                                                    }, i);
            }
            metadataTable.sort(new String[] {"key"}, new boolean[] {true});
        } else {
            //if only one single ImageProcessorResult object passed, create table normally with BeanItemContainer
            BeanItemContainer metadataContainer = new BeanItemContainer<ImageMetadata>(ImageMetadata.class, result[0].getMetadata());
            metadataContainer.sort(new String[]{"key"}, new boolean[]{true});
            metadataTable = new Table("Metadata", metadataContainer);
            metadataTable.setVisibleColumns(new Object[]{"key", "source", "value", "unit"});
        }
        
        metadataTable.setSelectable(true);
        metadataTable.setMultiSelect(false);
        metadataTable.setImmediate(true);
            
        metadataTable.setCellStyleGenerator(new Table.CellStyleGenerator() {
            @Override
            public String getStyle(Object itemId, Object propertyId) {
                
                //FIXME Code throws error in next line, since table no longer structured to match the ImageMetadata object
                
                //ImageMetadata metadata = (ImageMetadata) itemId;
                //if (result[0].getType() == ImageProcessorResult.Type.COMPARISON) {
                    //String key = metadata.getKey();
                    //if (Arrays.asList("red", "blue", "green").contains(key)) {
                        //return key;
                    //}
                //} else {
                    //if (!nonConflictMetadata.contains(metadata.getKey())) {
                        //if (metadata.isConflict()) {
                        //    return "red";
                        //} else {
                        //    return "green";
                        //}
                    //}
                //}
                return "";
            }
        });
        layout.addComponent(metadataTable);

        final Button rawData = new Button();
        rawData.setCaption("Raw data");
        rawData.addListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    ImageMetadata metadata = (ImageMetadata) metadataTable.getValue();
                    DifferApplication.getCurrentApplication().getMainWindow().addWindow(new RawDataWindow(parent, metadata.getSource()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        rawData.setImmediate(true);
        rawData.setEnabled(false); //FIXME
        layout.addComponent(rawData);
        metadataTable.addListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                rawData.setEnabled(true);
            }
        });
    }
}