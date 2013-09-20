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

    private String tableName = "METADATA";
    
    private static String VERSION_PROPERTY_NAME = "Version";
    private static String COLUMN_A1_PROPERTY    = "key";
    private static String COLUMN_A2_PROPERTY    = "source";
    private static String COLUMN_A3_PROPERTY    = "imageValueA";
    private static String COLUMN_A4_PROPERTY    = "imageValueB";
    private static String COLUMN_A5_PROPERTY    = "unit";
    
    private static String COLUMN_B1_PROPERTY    = "value"; //not used in imageA\imageB table
    
    private static String COLUMN_C1_PROPERTY    = "metadataSource";
    private static String COLUMN_C2_PROPERTY    = "version";
    
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
            }
            
            for (int i = 0; i < result[1].getMetadata().size(); i++) {
                String id = result[1].getMetadata().get(i).getKey() + "&&" +
                            result[1].getMetadata().get(i).getSource().getSourceName();
                if (!hashmap.containsKey(id)) { //prevent creating new ComparedImagesMetadata objects unneccessarily
                    hashmap.put(id, new ComparedImagesMetadata(id));
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
                    hashmap.get(id).setMetadataSource(msrc);
                }
                
                hashmap.get(id).setSourceName(result[1].getMetadata().get(i).getSource().getSourceName());
                
                hashmap.get(id).setValueB("" + result[1].getMetadata().get(i).getValue());
                hashmap.get(id).setConflict(result[1].getMetadata().get(i).isConflict());

            }

            metadataTable = new Table(tableName);
            metadataTable.addContainerProperty(COLUMN_A1_PROPERTY, String.class, null);
            metadataTable.addContainerProperty(COLUMN_A2_PROPERTY, Button.class, null);
            metadataTable.addContainerProperty(COLUMN_A3_PROPERTY, String.class, null);
            metadataTable.addContainerProperty(COLUMN_A4_PROPERTY, String.class, null);
            metadataTable.addContainerProperty(COLUMN_A5_PROPERTY, String.class, null);
            metadataTable.addContainerProperty(COLUMN_C2_PROPERTY, String.class, null);
            metadataTable.addContainerProperty(COLUMN_C1_PROPERTY, MetadataSource.class, null);

            //prevent column overflow
            metadataTable.setColumnWidth(COLUMN_A3_PROPERTY, 200);
            metadataTable.setColumnWidth(COLUMN_A4_PROPERTY, 200);
            metadataTable.setColumnWidth(COLUMN_C2_PROPERTY, 100);
            
            //first iteration merely gleans the version property from the various tools
            HashMap<String, String> versionmap = new HashMap<String, String>();
            Iterator it = hashmap.entrySet().iterator();
            int j = 0;
            while (it.hasNext()) {
                ComparedImagesMetadata cim = (ComparedImagesMetadata) ((Map.Entry)it.next()).getValue();
                if (cim.getKey().equals(VERSION_PROPERTY_NAME)) {
                    versionmap.put(cim.getSourceName(), cim.getValueA());
                }
                j++;
            }
            
            Iterator itx = hashmap.entrySet().iterator();
            int x = 0;
            while (itx.hasNext()) {
                ComparedImagesMetadata cim = (ComparedImagesMetadata) ((Map.Entry)itx.next()).getValue();
                cim.setVersion(versionmap.get(cim.getSourceName()));
                MetadataSource msrc = cim.getMetadataSource();
                cim.setSource(createClickableTool(layout, msrc, cim.getVersion()));
                metadataTable.addItem(new Object[] {cim.getKey(), cim.getSource(), cim.getValueA(), 
                                                    cim.getValueB(), cim.getUnit(),
                                                    cim.getVersion(), cim.getMetadataSource()}, x);
                x++;
            }
            
            //the following line must be set AFTER adding ALL data to the table
            //otherwise data will not be set properly (table will be empty)
            metadataTable.setVisibleColumns(new Object[] {COLUMN_A1_PROPERTY, COLUMN_A2_PROPERTY, COLUMN_A3_PROPERTY,
                                                          COLUMN_A4_PROPERTY, COLUMN_A5_PROPERTY});
              
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
                    
                    if (result.length == 2) {                   
                        String valA = (String) metadataTable.getContainerProperty(itemId, COLUMN_A3_PROPERTY).getValue();
                        String valB = (String) metadataTable.getContainerProperty(itemId, COLUMN_A4_PROPERTY).getValue();   
                        if (valA != null && valB != null) {
                            if (valA.equalsIgnoreCase(valB)) {
                                return "green";
                            }
                        }
                        return "red";
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
            metadataTable = new Table(tableName, metadataContainer);
            metadataTable.setVisibleColumns(new Object[]{COLUMN_A1_PROPERTY,COLUMN_A2_PROPERTY,
                                                         COLUMN_B1_PROPERTY,COLUMN_A5_PROPERTY});
            metadataTable.setColumnWidth(COLUMN_B1_PROPERTY, 200);
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
            toolName = "tool name unknown";
        }
        Button button = new Button("" + toolName);
        final String vrsn;
        if (version != null && version.length() > 0) {
            vrsn = version;
        } else {
            vrsn = "unknown";
        }
        button.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                layout.getWindow().showNotification(toolName, "<br/>version " + vrsn, Window.Notification.TYPE_HUMANIZED_MESSAGE);
            } 
        });
        button.addStyleName("link");
        
        return button;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
