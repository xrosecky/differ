package cz.nkp.differ.gui.windows;

import java.io.File;

import org.vaadin.easyuploads.MultiFileUpload;

import com.vaadin.ui.Window;
import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.exceptions.ImageDifferException;
import cz.nkp.differ.gui.components.UserFilesWidget;
import cz.nkp.differ.model.Image;

@SuppressWarnings("serial")
public class UploadFilesWindow extends Window {

    private MainDifferWindow mainWindow;

    public UploadFilesWindow(final MainDifferWindow window) {
	this.mainWindow = window;
	setCaption("Upload Files");
	setModal(true);
	setDraggable(false);
	setResizable(false);
	center();
	setWidth("25%");
	upload = new MultiFileUpload() {

	    @Override
	    protected void handleFile(File file, String fileName,
		    String mimeType, long length) {
		DifferApplication app = (DifferApplication) DifferApplication.getCurrentApplication();
		try {
		    Image image = DifferApplication.getImageManager().uploadImage(app.getLoggedUser(), file, fileName);
		    for (UserFilesWidget widget : mainWindow.getUserFilesWidgets()) {
			widget.refresh();
		    }
		} catch (ImageDifferException ide) {
		    window.showNotification("Error when uploading file.", "<br/>" + ide.getMessage(), Window.Notification.TYPE_ERROR_MESSAGE);
		}
	    }
	};
	addComponent(upload);
    }
    MultiFileUpload upload;
}
