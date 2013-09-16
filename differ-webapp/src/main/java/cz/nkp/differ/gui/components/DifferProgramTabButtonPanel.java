package cz.nkp.differ.gui.components;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.MouseEvents.ClickEvent;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;
import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.compare.io.CompareComponent;

import cz.nkp.differ.gui.tabs.DifferProgramTab;
import cz.nkp.differ.gui.windows.MainDifferWindow;
import cz.nkp.differ.gui.windows.ProfileCreationWindow;
import cz.nkp.differ.gui.windows.UploadFilesWindow;
import cz.nkp.differ.model.Image;
import cz.nkp.differ.util.GUIMacros;

public class DifferProgramTabButtonPanel extends CustomComponent {

    private static final long serialVersionUID = -3190731385605086001L;
    private Button uploadImagesButton;
    private Button createProfilesButton;
    private Button deleteImagesButton;
    private Button refreshImagesButton;
    private Button compareButton;
    static private String BTN_TXT_COMPARE = "<div class=\"compare-button-caption\">Compare</div>";
    static private String BTN_TXT_PROCEED = "<div class=\"compare-button-caption\">Proceed</div>";    
    private DifferProgramTab parent;
    private MainDifferWindow mainWindow;

    public DifferProgramTabButtonPanel(DifferProgramTab parent, MainDifferWindow window) {
	this.parent = parent;
	this.mainWindow = window;
	this.setCompositionRoot(createDifferProgramTabButtonPanel());
	for (UserFilesWidget widget : mainWindow.getUserFilesWidgets()) {
	    widget.addSelectionListener(new ValueChangeListener() {

		@Override
		public void valueChange(ValueChangeEvent event) {
		    int left = mainWindow.getUserFilesWidgets().get(0).getSelectedImages().size();
		    int right = mainWindow.getUserFilesWidgets().get(1).getSelectedImages().size();
		    deleteImagesButton.setEnabled(left > 0 || right > 0);
                    compareButton.setEnabled(left + right > 0);
                    if (left + right == 1) {
                        compareButton.setCaption(BTN_TXT_PROCEED);
                    } else {
                        compareButton.setCaption(BTN_TXT_COMPARE);
                    }
		}
	    });
	}
    }

    private Panel createDifferProgramTabButtonPanel() {
	Panel panel = new Panel();
	panel.addComponent(createDifferProgramTabButtonLayout());
	panel.setHeight("100%");
	return panel;
    }

    private Layout createDifferProgramTabButtonLayout() {
	VerticalLayout buttonPanelRoot = new VerticalLayout();

	uploadImagesButton = new Button("Upload images");
	uploadImagesButton.addListener(GUIMacros.createWindowOpenButtonListener(mainWindow, new UploadFilesWindow(mainWindow)));

	refreshImagesButton = new Button("Refresh images");
	refreshImagesButton.addListener(new Listener() {

	    @Override
	    public void componentEvent(Event event) {
		for (UserFilesWidget widget : mainWindow.getUserFilesWidgets()) {
		    widget.refresh();
		}
	    }
	});

	deleteImagesButton = new Button("Remove images");
	deleteImagesButton.addListener(new Listener() {

	    @Override
	    public void componentEvent(Event event) {
		int count = 0;
		for (UserFilesWidget widget : mainWindow.getUserFilesWidgets()) {
		    count += widget.removeSelectedImages();
		}
		mainWindow.showNotification("Remove images", String.format("%s file(s) has been removed.", count),
			Notification.TYPE_TRAY_NOTIFICATION);
		for (UserFilesWidget widget : mainWindow.getUserFilesWidgets()) {
		    widget.refresh();
		}
	    }
	});
	deleteImagesButton.setEnabled(false);

	createProfilesButton = new Button("Create New Profile");
	createProfilesButton.addListener(GUIMacros.createWindowOpenButtonListener(mainWindow, new ProfileCreationWindow()));

	buttonPanelRoot.addComponent(GUIMacros.bindTooltipToComponent(uploadImagesButton, "Upload image", "Use this function to upload new image files"));
	buttonPanelRoot.addComponent(GUIMacros.bindTooltipToComponent(refreshImagesButton, "Refresh images", "Refresh images"));
	buttonPanelRoot.addComponent(GUIMacros.bindTooltipToComponent(deleteImagesButton, "Delete images", "Delete selected images"));
	buttonPanelRoot.addComponent(GUIMacros.bindTooltipToComponent(createProfilesButton, "Create Profile", "Create a new image processing profile"));

	compareButton = new Button(BTN_TXT_COMPARE);
        compareButton.setHtmlContentAllowed(true);
        compareButton.addStyleName("v-bigbutton");
	compareButton.addListener(new ClickListener() {

	    @Override
	    public void buttonClick(Button.ClickEvent event) {
		try {
                    DifferApplication.getGATracker().trackPageview("/compare"); 
		    HorizontalLayout layout = new HorizontalLayout();
		    Image[] selectedImages = parent.getSelectedImages();
		    CompareComponent cp = new CompareComponent();
		    cp.setApplication(DifferApplication.getCurrentApplication());
		    layout.addComponent(new PluginDisplayComponent(cp, selectedImages));
		    parent.setCustomView(layout);
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	    }
	});
	compareButton.setEnabled(false);
	buttonPanelRoot.addComponent(compareButton);

	return buttonPanelRoot;
    }
}
