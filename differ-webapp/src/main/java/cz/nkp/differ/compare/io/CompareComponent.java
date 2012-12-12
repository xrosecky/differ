package cz.nkp.differ.compare.io;

import org.apache.log4j.Logger;

import com.vaadin.Application;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.model.Image;
import cz.nkp.differ.listener.ProgressListener;

import cz.nkp.differ.plugins.tools.PluginPollingThread;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompareComponent {

    public static Logger LOGGER = Logger.getRootLogger();
    //private ImageFileAnalysisContainer iFAC1, iFAC2, iFAC3;
    private List<ImageFileAnalysisContainer> iFACs = new ArrayList<ImageFileAnalysisContainer>();
    private Application application = null;
    private PluginPollingThread currentThread;
    private Image[] images;

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
	ImageProcessor imageProcessor = (ImageProcessor) DifferApplication.getApplicationContext().getBean("imageProcessor");
	if (images.length == 2) {
	    ImageProcessorResult[] result = null;
	    try {
		result = imageProcessor.processImages(images[0].getFile(), images[1].getFile(), c);
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	    HorizontalLayout layout = new HorizontalLayout();
	    ImageFileAnalysisContainer iFAC1 = new ImageFileAnalysisContainer(result[0], this);
	    layout.addComponent(iFAC1.getComponent());
	    ImageFileAnalysisContainer iFAC2 = new ImageFileAnalysisContainer(result[1], this);
	    layout.addComponent(iFAC2.getComponent());
	    if (result[2] != null) {
		ImageFileAnalysisContainer iFAC3 = new ImageFileAnalysisContainer(result[2], this);
		layout.addComponent(iFAC3.getComponent());
		iFACs.addAll(Arrays.asList(iFAC1, iFAC2, iFAC3));
	    } else {
		TextField errorComponent = new TextField();
		errorComponent.setValue("Images can't be compared.");
		errorComponent.setReadOnly(true);
		layout.addComponent(errorComponent);
		iFACs.addAll(Arrays.asList(iFAC1, iFAC2));
	    }
	    return layout;
	} else {
	    HorizontalLayout layout = new HorizontalLayout();
	    for (Image image : images) {
		try {
		    ImageProcessorResult result = imageProcessor.processImage(image.getFile(), c);
		    ImageFileAnalysisContainer iFAC = new ImageFileAnalysisContainer(result, this);
		    layout.addComponent(iFAC.getComponent());
		    iFACs.add(iFAC);
		} catch (Exception ex) {
		    ex.printStackTrace();
		    throw new RuntimeException(ex);
		}
	    }
	    return layout;
	}
    }

    public Application getApplication() {
	return application;
    }

    public void setApplication(Application application) {
	this.application = application;
    }
}
