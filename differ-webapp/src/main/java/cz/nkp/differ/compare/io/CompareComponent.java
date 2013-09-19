package cz.nkp.differ.compare.io;

import cz.nkp.differ.compare.io.generators.ImageMetadataComponentGenerator;
import org.apache.log4j.Logger;

import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.io.ResultManager;
import cz.nkp.differ.model.Image;
import cz.nkp.differ.listener.ProgressListener;
import cz.nkp.differ.plugins.tools.PluginPollingThread;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;


public class CompareComponent {

    public static Logger LOGGER = Logger.getRootLogger();
    //private ImageFileAnalysisContainer iFAC1, iFAC2, iFAC3;
    private List<ImageFileAnalysisContainer> iFACs = new ArrayList<ImageFileAnalysisContainer>();
    private Application application = null;
    private PluginPollingThread currentThread;
    private Image[] images;
    private ImageProcessorResult[] results;
    
    public String getName() {
	return "Compare";
    }

    public void showSeriousError(String message) {
	Window.Notification errorNotif = new Window.Notification("Plugin Error",
		"A runtime error has occured while executing a plugin. Plugin operation halted. Message: " + message,
		Window.Notification.TYPE_ERROR_MESSAGE);

	application.getMainWindow().showNotification(errorNotif);
    }

    public void addImages(Image[] images) {
	this.images = images;
    }

    public void setPluginDisplayComponentCallback(final ProgressListener c) {
	try {
	    currentThread = new PluginPollingThread(this, c);
	    currentThread.start();
	} catch (Exception e) {
	    showSeriousError(e.getLocalizedMessage());
	}
    }

    public Component getPluginDisplayComponent(ProgressListener c) {
        VerticalLayout layout = new VerticalLayout();
	ImageProcessor imageProcessor = (ImageProcessor) DifferApplication.getApplicationContext().getBean("imageProcessor");
	if (images.length == 2) {
	    ImageProcessorResult[] result = null;
	    try {
		result = imageProcessor.processImages(images[0].getFile(), images[1].getFile(), c);
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	    HorizontalLayout childALayout = new HorizontalLayout();
            HorizontalLayout childBLayout = new HorizontalLayout();
	    ImageFileAnalysisContainer iFAC1 = new ImageFileAnalysisContainer(result[0], this, 0, images[0].getFileName());
	    childALayout.addComponent(iFAC1.getComponent());
	    ImageFileAnalysisContainer iFAC2 = new ImageFileAnalysisContainer(result[1], this, 1, images[1].getFileName());
	    childALayout.addComponent(iFAC2.getComponent());
            results = new ImageProcessorResult[] {result[0], result[1]};
            
            ImageMetadataComponentGenerator table = new ImageMetadataComponentGenerator(results, this);
            ImageMetadataComponentGenerator tableComp = null;
	    if (result[2] != null) {
                Label comparedChecksum;
		if (iFAC1.getChecksum().equals(iFAC2.getChecksum())) {
                    comparedChecksum = new Label("Image hash values are equal.", Label.CONTENT_XHTML);
                } else {
                    comparedChecksum = new Label("Image hash values are NOT equal.", Label.CONTENT_XHTML);
                }
                ImageFileAnalysisContainer iFAC3 = new ImageFileAnalysisContainer(result[2], this, 2);
                iFAC3.setChecksumLabel(comparedChecksum);
		childALayout.addComponent(iFAC3.getComponent());
		iFACs.addAll(Arrays.asList(iFAC1, iFAC2, iFAC3));
                tableComp = new ImageMetadataComponentGenerator(new ImageProcessorResult[] {result[2]}, this);
                tableComp.setTableName("SILARITY METRICS");
	    } else {
		TextField errorComponent = new TextField();
		errorComponent.setValue("Images can't be compared.");
		errorComponent.setReadOnly(true);
		childALayout.addComponent(errorComponent);
		iFACs.addAll(Arrays.asList(iFAC1, iFAC2));
	    }  
            childBLayout.addComponent(table.getComponent());
            if (tableComp != null) {
                childBLayout.addComponent(tableComp.getComponent());
            }
            layout.addComponent(childALayout);
            layout.addComponent(childBLayout);
            layout.addComponent(addExportResultsButton(results));
	    return layout;
	} else {
	    HorizontalLayout childLayout = new HorizontalLayout();
            final ImageProcessorResult[] result = new ImageProcessorResult[images.length];
            for (int i = 0; i < images.length; i++) {
		try {
		    result[i] = imageProcessor.processImage(images[i].getFile(), c);
		    ImageFileAnalysisContainer iFAC = new ImageFileAnalysisContainer(result[i], this, i, images[i].getFileName());
		    childLayout.addComponent(iFAC.getComponent());
		    iFACs.add(iFAC);
		} catch (Exception ex) {
		    ex.printStackTrace();
		    throw new RuntimeException(ex);
		}
	    }
            layout.addComponent(childLayout);
            ImageMetadataComponentGenerator table = new ImageMetadataComponentGenerator(result, this);
            layout.addComponent(table.getComponent());
            layout.addComponent(addExportResultsButton(result));
	    return layout;
	}
    }

    public Application getApplication() {
	return application;
    }

    public void setApplication(Application application) {
	this.application = application;
    }
    
    private Component addExportResultsButton(final ImageProcessorResult[] ipr) {
        Button btnSave = new Button("Save Results");
        btnSave.addListener(new ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                exportResultsToXml(ipr);
                DifferApplication.getMainApplicationWindow().showNotification("Success", 
                    "<br/>Results XML has been exported successfully," +
                    "<br/>they can be found in the Results tab", 
                    Window.Notification.TYPE_HUMANIZED_MESSAGE);                
            }   
        });
        return btnSave;
    }
    
    private void exportResultsToXml(ImageProcessorResult[] ipr) {
        ArrayList<SerializableImageProcessorResult> resultsList = new ArrayList<SerializableImageProcessorResult>();
        for (ImageProcessorResult result : ipr) {
            SerializableImageProcessorResult res = new SerializableImageProcessorResult();
            try {
                if (result.getFullImage() != null) {
                    res.setFullImage(new SerializableImage(result.getFullImage()));
                }
                if (result.getPreview() != null) {
                    res.setPreview(new SerializableImage(result.getPreview()));
                } 
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(CompareComponent.class.getName()).log(Level.SEVERE, null, ex);
            }
            res.setHistogram(result.getHistogram());
            res.setType(result.getType());
            res.setWidth(result.getWidth());
            res.setHeight(result.getHeight());
            res.setMetadata(result.getMetadata());
            resultsList.add(res);
        }
        SerializableImageProcessorResults sipr = new SerializableImageProcessorResults();
        sipr.setResults(resultsList);
        ResultManager resultMan = DifferApplication.getResultManager();
        String resultsDir = "/tmp/differ/" + DifferApplication.getUserManager().getLoggedInUser() + "/results";
        new File(resultsDir).mkdirs(); //make path in case it don't exist
        resultMan.setDirectory(resultsDir);
        resultMan.createJAXBContext(SerializableImageProcessorResults.class);
        try {
            resultMan.save(sipr);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(CompareComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
