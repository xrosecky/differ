package cz.nkp.differ.gui.windows;

import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
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
	setDraggable(false);
	setResizable(false);
	center();
	setWidth("800px");
	setHeight("100%");
	Panel imagePanel = new Panel();
	imagePanel.addComponent(fullImage);
	imagePanel.setScrollable(true);
	imagePanel.addStyleName(Runo.PANEL_LIGHT);
	imagePanel.setSizeFull();
	addComponent(imagePanel);
    }
}
