package cz.nkp.differ.gui.windows;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import java.io.File;

import org.vaadin.easyuploads.MultiFileUpload;

import com.vaadin.ui.Window;
import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.exceptions.ImageDifferException;
import cz.nkp.differ.gui.components.RemoteFile;
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
        
        final Label lbl = new Label("OR");
        lbl.addStyleName("v-labelspacer");
        addComponent(lbl);
        
        HorizontalLayout remote = new HorizontalLayout();
        final TextField urlPaste = new TextField("Select Remote File");
        urlPaste.setInputPrompt("Paste URL here...");
        final Button uploadBtn = new Button("Upload");
        uploadBtn.addListener(new ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                RemoteFile remoteFile = new RemoteFile((String) urlPaste.getValue());
                if (remoteFile.isValid()) {
                    File file = remoteFile.getFile();
                    try {
                        DifferApplication.getImageManager().uploadImage(
                            DifferApplication.getCurrentApplication().getLoggedUser(), file, file.getName());
                        for (UserFilesWidget widget : mainWindow.getUserFilesWidgets()) {
                            widget.refresh();
                        }
                    } catch (ImageDifferException ide) {
                        window.showNotification("Error when uploading file.", "<br/>" + ide.getMessage(), Window.Notification.TYPE_ERROR_MESSAGE);
                    }
                    urlPaste.setValue("");
                }

            }   
        });

        remote.addComponent(urlPaste);
        remote.addComponent(uploadBtn);
        remote.setComponentAlignment(uploadBtn, Alignment.BOTTOM_RIGHT);
        addComponent(remote);
    }
    MultiFileUpload upload;
}
