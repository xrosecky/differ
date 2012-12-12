package cz.nkp.differ.gui.components;


import org.apache.log4j.Logger;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.VerticalLayout;
import cz.nkp.differ.compare.io.CompareComponent;
import cz.nkp.differ.model.Image;

import cz.nkp.differ.listener.ProgressListener;

public class PluginDisplayComponent extends CustomComponent {

    private static final long serialVersionUID = -5172306282663506101L;
    private Logger LOGGER = Logger.getLogger(PluginDisplayComponent.class);

    public PluginDisplayComponent(CompareComponent d, Image[] images) {
	super();
	if (images == null) {
	    throw new NullPointerException("images");
	}
	this.setCompositionRoot(createPluginCompareComponent(d, images));

    }

    private Layout createPluginCompareComponent(CompareComponent d, Image[] images) {
	HorizontalLayout layout = new HorizontalLayout();
	layout.addComponent(new PluginDisplayPanel(d, images));
	return layout;
    }
}

class PluginDisplayPanel extends VerticalLayout implements ProgressListener {

    private static final long serialVersionUID = -4597810967107465071L;
    private ProgressIndicator progress = new ProgressIndicator();
    static Logger LOGGER = Logger.getLogger(PluginDisplayPanel.class);

    public PluginDisplayPanel(CompareComponent d, Image[] images) {
	d.addImages(images);
	d.setPluginDisplayComponentCallback(this);
	synchronized (progress) {
	    progress.setIndeterminate(false);
	    progress.setImmediate(true);
	    progress.setPollingInterval(750);
	    progress.setCaption("Loading plugin...");
	    progress.setValue(0f);
	    this.addComponent(progress);
	}
    }

    @Override
    public void ready(Object c) {
	this.removeAllComponents();
	this.addComponent((Component) c);
    }

    @Override
    public void setCompleted(int percentage) {
	synchronized (progress) {
	    progress.setValue(((float) (percentage)) / 100f);
	}
    }
}
