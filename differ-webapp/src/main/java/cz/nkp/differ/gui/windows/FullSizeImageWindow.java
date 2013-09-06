package cz.nkp.differ.gui.windows;

import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Runo;

/**
 *
 * @author xrosecky
 */
public class FullSizeImageWindow extends Window {
    
    public FullSizeImageWindow(Component fullImage) {
	setCaption("Image Display");
	setModal(true);
	setDraggable(true);
	setResizable(true);
	center();
	setWidth("800px");
	setHeight("400px");
        getContent().setSizeUndefined(); //without this line, no horizontal scroll will appear
	Panel imagePanel = new Panel();
	imagePanel.addComponent(fullImage);
	imagePanel.setScrollable(true);
	imagePanel.addStyleName(Runo.PANEL_LIGHT);
	addComponent(imagePanel);
    }
    
}
