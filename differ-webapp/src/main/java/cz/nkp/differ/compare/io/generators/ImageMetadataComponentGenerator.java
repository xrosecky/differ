package cz.nkp.differ.compare.io.generators;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.compare.io.CompareComponent;
import cz.nkp.differ.compare.io.ComparedImagesMetadata;
import cz.nkp.differ.compare.io.ImageProcessorResult;
import cz.nkp.differ.compare.metadata.ImageMetadata;
import cz.nkp.differ.compare.metadata.MetadataSource;
import cz.nkp.differ.exceptions.FatalDifferException;
import cz.nkp.differ.gui.windows.RawDataWindow;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Thomas Truax
 */
public class ImageMetadataComponentGenerator {

    private ImageProcessorResult[] result;
    private List<String> nonConflictMetadata = Arrays.asList("exit-code");
    private CompareComponent parent;

    private static String TABLE_NAME            = "Metadata";
    private static String VERSION_PROPERTY_NAME = "Version";
    private static String COLUMN_A1_PROPERTY    = "key";
    private static String COLUMN_A2_PROPERTY    = "source";
    private static String COLUMN_A3_PROPERTY    = "imageValueA";
    private static String COLUMN_A4_PROPERTY    = "imageValueB";
    private static String COLUMN_A5_PROPERTY    = "unit";
    
    private static String COLUMN_B1_PROPERTY    = "value"; //not used in imageA\imageB table
    
    private static String COLUMN_C1_PROPERTY    = "metadataSource";
    private static String COLUMN_C2_PROPERTY    = "sourceName";
    private static String COLUMN_C3_PROPERTY    = "version";
    
    private HashMap<String, String> versionValues = new HashMap<String, String>();
    private ArrayList<ComparedImagesMetadata> cimList = new ArrayList<ComparedImagesMetadata>();
    
    /**
     * Constructor which takes a single ImageProcessorResult object (usually for comparison table)
     * @param ImageProcessorResult
     * @param CompareComponent 
     */
    public ImageMetadataComponentGenerator(ImageProcessorResult result, CompareComponent parent) {
        this(new ImageProcessorResult[] {result}, parent);
    }
    
    /**
     * Constructor which takes an array of ImageProcessorResult objects for multiple images
     * @param ImageProcessorResult[]
     * @param CompareComponent
     */
    public ImageMetadataComponentGenerator(ImageProcessorResult[] result, CompareComponent parent) {
        //FIXME: if results.length > 2, throw unsupported number of args exception
        this.result = result;
        this.parent = parent;
    }
    
    /**
     * Retrieves table component after it has been generated
     * @return Layout
     */
    public Layout getComponent() {
        VerticalLayout layout = new VerticalLayout();
        layout.addStyleName("v-table-metadata");
        layout.setSpacing(true);
        try {
            generateMetadataTable(layout);
        } catch (FatalDifferException fde) {
            fde.printStackTrace();
        }
        return layout;
    }
    
    /**
     * Builds the table 
     * @param Layout 
     */
    private void generateMetadataTable(final Layout layout) throws FatalDifferException {
        final Table metadataTable;
        
        //TODO: add logic for handling array length > 2
        if (result.length == 2) {
         
            HashMap<String, ComparedImagesMetadata> hashmap = new HashMap<String, ComparedImagesMetadata>();
            
            for (int i = 0; i < result[0].getMetadata().size(); i++) {
                String id = result[0].getMetadata().get(i).getKey() + "&&" +
                            result[0].getMetadata().get(i).getSource().getSourceName();
                hashmap.put(id, new ComparedImagesMetadata(id));
                if (result[0].getMetadata().get(i).getKey().contentEquals(VERSION_PROPERTY_NAME)) {
                    versionValues.put(result[0].getMetadata().get(i).getSource().getSourceName(), "" + result[0].getMetadata().get(i).getValue());
                }                
            }
            
            for (int i = 0; i < result[1].getMetadata().size(); i++) {
                String id = result[1].getMetadata().get(i).getKey() + "&&" +
                            result[1].getMetadata().get(i).getSource().getSourceName();
                if (!hashmap.containsKey(id)) { //prevent creating new ComparedImagesMetadata objects unneccessarily
                    hashmap.put(id, new ComparedImagesMetadata(id));
                    if (result[1].getMetadata().get(i).getKey().contentEquals(VERSION_PROPERTY_NAME)) {
                        versionValues.put(result[1].getMetadata().get(i).getSource().getSourceName(), "" + result[1].getMetadata().get(i).getValue());
                    }
                }

            }
            
            for (int i = 0; i < result[0].getMetadata().size(); i++) {
                String id = result[0].getMetadata().get(i).getKey() + "&&" +
                            result[0].getMetadata().get(i).getSource().getSourceName();
                
                hashmap.get(id).setKey(result[0].getMetadata().get(i).getKey());

                hashmap.get(id).setValueA("" + result[0].getMetadata().get(i).getValue());
                hashmap.get(id).setUnit(result[0].getMetadata().get(i).getUnit());
                hashmap.get(id).setConflict(result[0].getMetadata().get(i).isConflict());
                MetadataSource msrc = result[0].getMetadata().get(i).getSource();
                hashmap.get(id).setSource(createClickableTool(layout, msrc, versionValues.get(hashmap.get(id).getSourceName())));
                hashmap.get(id).setMetadataSource(msrc);
                hashmap.get(id).setSourceName(result[0].getMetadata().get(i).getSource().getSourceName());
            }
            
            for (int i = 0; i < result[1].getMetadata().size(); i++) {
                String id = result[1].getMetadata().get(i).getKey() + "&&" +
                            result[1].getMetadata().get(i).getSource().getSourceName();
                
                if (hashmap.get(id).getKey() == null) {
                    hashmap.get(id).setKey(result[1].getMetadata().get(i).getKey());
                }          
                
                if (hashmap.get(id).getUnit() == null) {
                    hashmap.get(id).setUnit(result[1].getMetadata().get(i).getUnit());
                }
                
                if (hashmap.get(id).getSource() == null) {
                    MetadataSource msrc = result[1].getMetadata().get(i).getSource();
                    hashmap.get(id).setSource(createClickableTool(layout, msrc, versionValues.get(hashmap.get(id).getSourceName())));
                    hashmap.get(id).setMetadataSource(msrc);
                }
                
                hashmap.get(id).setSourceName(result[1].getMetadata().get(i).getSource().getSourceName());
                
                hashmap.get(id).setValueB("" + result[1].getMetadata().get(i).getValue());
                hashmap.get(id).setConflict(result[1].getMetadata().get(i).isConflict());

            }

            metadataTable = new Table(TABLE_NAME);
            metadataTable.addContainerProperty(COLUMN_A1_PROPERTY, String.class, null);
            metadataTable.addContainerProperty(COLUMN_A2_PROPERTY, Button.class, null);
            metadataTable.addContainerProperty(COLUMN_A3_PROPERTY, String.class, null);
            metadataTable.addContainerProperty(COLUMN_A4_PROPERTY, String.class, null);
            metadataTable.addContainerProperty(COLUMN_A5_PROPERTY, String.class, null);
            metadataTable.addContainerProperty(COLUMN_C1_PROPERTY, MetadataSource.class, null);
            metadataTable.addContainerProperty(COLUMN_C2_PROPERTY, String.class, null);
            metadataTable.addContainerProperty(COLUMN_C3_PROPERTY, String.class, null);
            
            Iterator it = hashmap.entrySet().iterator();
            int j = 0;
            while (it.hasNext()) {
                ComparedImagesMetadata cim = (ComparedImagesMetadata) ((Map.Entry)it.next()).getValue();
                metadataTable.addItem(new Object[] {cim.getKey(), cim.getSource(), cim.getValueA(), 
                                                    cim.getValueB(), cim.getUnit(), cim.getMetadataSource(),
                                                    cim.getSourceName(), cim.getVersion()}, j);
                cimList.add(cim);
                j++;
            }

            final Button rawData = new Button();
            rawData.setCaption("Raw data");
            rawData.addListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    Item selectedRow = metadataTable.getItem((Integer) metadataTable.getValue());
                    Property rowData = selectedRow.getItemProperty("metadataSource");
                    MetadataSource metadata = (MetadataSource) rowData.getValue();
                    Window rawDataWindow = new RawDataWindow(parent, metadata);
                    Window mainWindow = DifferApplication.getMainApplicationWindow();
                    mainWindow.addWindow(rawDataWindow);
                }
            });
            rawData.setImmediate(true);
            rawData.setEnabled(true); 
            
            metadataTable.sort(new String[] {COLUMN_A1_PROPERTY}, new boolean[] {true});
            metadataTable.setSelectable(true);
            metadataTable.setMultiSelect(false);
            metadataTable.setImmediate(true);

            metadataTable.setCellStyleGenerator(new Table.CellStyleGenerator() {
                @Override
                public String getStyle(Object itemId, Object propertyId) {
                    final ImageMetadata metadata;
                    if (itemId instanceof Integer) {
                        metadata = result[0].getMetadata().get((Integer) itemId);
                    } else {
                        metadata = (ImageMetadata) itemId;
                    }
                    if (result[0].getType() == ImageProcessorResult.Type.COMPARISON) {
                        String key = metadata.getKey();
                        if (Arrays.asList("red", "blue", "green").contains(key)) {
                            return key;
                        }
                    } else {
                        if (!nonConflictMetadata.contains(metadata.getKey())) {
                            if (metadata.isConflict()) {
                                return "red";
                            } else {
                                return "green";
                            }
                        }
                    }
                    return "";
                }
            });
            layout.addComponent(metadataTable);
            layout.addComponent(rawData);
            metadataTable.addListener(new ValueChangeListener() {
                @Override
                public void valueChange(ValueChangeEvent event) {
                    rawData.setEnabled(true);
                }
            });
        } else {
            
            //if only one single ImageProcessorResult object passed, create table with BeanItemContainer
            BeanItemContainer metadataContainer = 
                    new BeanItemContainer<ImageMetadata>(ImageMetadata.class, result[0].getMetadata());
            metadataTable = new Table(TABLE_NAME, metadataContainer);
            metadataTable.setVisibleColumns(new Object[]{COLUMN_A1_PROPERTY,COLUMN_A2_PROPERTY,
                                                         COLUMN_B1_PROPERTY,COLUMN_A5_PROPERTY});
            
            metadataTable.sort(new String[] {COLUMN_A1_PROPERTY}, new boolean[] {true});
            metadataTable.setSelectable(true);
            metadataTable.setMultiSelect(false);
            metadataTable.setImmediate(true);

            metadataTable.setCellStyleGenerator(new Table.CellStyleGenerator() {
                @Override
                public String getStyle(Object itemId, Object propertyId) {
                    final ImageMetadata metadata;
                    if (itemId instanceof Integer) {
                        metadata = result[0].getMetadata().get((Integer) itemId);
                    } else {
                        metadata = (ImageMetadata) itemId;
                    }
                    if (result[0].getType() == ImageProcessorResult.Type.COMPARISON) {
                        String key = metadata.getKey();
                        if (Arrays.asList("red", "blue", "green").contains(key)) {
                            return key;
                        }
                    } else {
                        if (!nonConflictMetadata.contains(metadata.getKey())) {
                            if (metadata.isConflict()) {
                                return "red";
                            } else {
                                return "green";
                            }
                        }
                    }
                    return "";
                }
            });
            layout.addComponent(metadataTable);            
        }
        



    }
    
    private Button createClickableTool(final Layout layout, MetadataSource source, final String version) {
       
        final String toolName;
        if (source.getSourceName() != null && source.getSourceName().length() > 0) {
            toolName = source.getSourceName();
        } else {
            toolName = "unknown";
        }
        Button button = new Button(toolName);

        //if (cSource.getVersion() != null && cSource.getVersion().length() > 0) {
            //version = cSource.getVersion();
        //} else {
        //    version = "Tool version unknown";
        //}
        button.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                layout.getWindow().showNotification(toolName + ": " + version);
            } 
        });
        button.addStyleName("link");
        
        return button;
    }

}
