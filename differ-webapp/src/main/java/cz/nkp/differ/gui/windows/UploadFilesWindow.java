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
import cz.nkp.differ.gui.components.UploadFile;
import cz.nkp.differ.gui.components.UserFilesWidget;
import cz.nkp.differ.model.Image;

@SuppressWarnings("serial")
public class UploadFilesWindow extends Window {

    private MainDifferWindow mainWindow;
    static private long MAX_SIZE = 15728640; //in bytes (15MB)
    MultiFileUpload upload; 
    
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

                if (isValid(fileName, length)) {
                    try {
                        Image image = DifferApplication.getImageManager().uploadImage(app.getLoggedUser(), file, fileName);
                        for (UserFilesWidget widget : mainWindow.getUserFilesWidgets()) {
                            widget.refresh();
                        }
                    } catch (ImageDifferException ide) {
                        window.showNotification("File failed to upload", "<br/>" + ide.getMessage(), Window.Notification.TYPE_ERROR_MESSAGE);
                    }
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
                UploadFile remoteFile = new UploadFile(UploadFile.TYPE.REMOTE_URL, (String) urlPaste.getValue(), MAX_SIZE);
                if (remoteFile.isValid()) {
                    File file = remoteFile.getFile();
                    try {
                        DifferApplication.getImageManager().uploadImage(
                            DifferApplication.getCurrentApplication().getLoggedUser(), file, file.getName());
                        for (UserFilesWidget widget : mainWindow.getUserFilesWidgets()) {
                            widget.refresh();
                        }
                    } catch (ImageDifferException ide) {
                        window.showNotification("File failed to upload", "<br/>" + ide.getMessage(), Window.Notification.TYPE_ERROR_MESSAGE);
                    }
                    urlPaste.setValue("");
                } else {
                    window.showNotification("File failed to upload",
                                            "<br/>" + remoteFile.getErrorMessage(),
                                            Window.Notification.TYPE_WARNING_MESSAGE);
                    urlPaste.setValue("");
                }

            }   
        });

        remote.addComponent(urlPaste);
        remote.addComponent(uploadBtn);
        remote.setComponentAlignment(uploadBtn, Alignment.BOTTOM_RIGHT);
        addComponent(remote);
    }
    

    private boolean isValid(String filename, long length) {
        if (length > MAX_SIZE) {
           DifferApplication.getCurrentApplication().getMainWindow().showNotification("File failed to upload",
                "<br/>File must not exceed " + (MAX_SIZE/1048576) + "MB for registered users", Window.Notification.TYPE_WARNING_MESSAGE);
           return false; 
        }    
        String ext = "";
        int extIndex;
        if (filename != null) {
            if ((extIndex = filename.lastIndexOf('.') + 1) > 0) {
                ext = filename.substring(extIndex);
                for (String s : UploadFile.VALID_EXTENSIONS) {
                    if (s.equalsIgnoreCase(ext)) {
                        return true;
                    }
                }
            }
        }
        DifferApplication.getCurrentApplication().getMainWindow().showNotification("File failed to upload",
            "<br/>File extension " + ext.toUpperCase() + " is invalid", Window.Notification.TYPE_WARNING_MESSAGE);
        return false;
    }
}
