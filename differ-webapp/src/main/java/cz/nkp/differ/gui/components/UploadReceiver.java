package cz.nkp.differ.gui.components;

import com.vaadin.terminal.FileResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import cz.nkp.differ.DifferApplication;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Thomas Truax
 */
public class UploadReceiver implements Receiver, SucceededListener {
    
    private static String tempDir = "/tmp/differ/anon_uploads/";
    private String filePrefix;
    private File file;
    private static int i; 
    private Embedded embedded;
    private Button compareButton;
    
    public UploadReceiver(Embedded embedded, Button compareButton) {
        this.filePrefix = "image" + (++i) + "-";
        this.embedded = embedded;
        this.compareButton = compareButton;
    }
 
    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {           
           FileOutputStream fos;
           try {
               File dir = new File(tempDir);
               if (!dir.exists()) {
                   dir.mkdirs();
               }
               file = new File(tempDir + filePrefix + filename);
               fos = new FileOutputStream(file);
           } catch (FileNotFoundException io) {
               DifferApplication.getCurrentApplication().getMainWindow().showNotification("Error", "Error while uploading file");
               return null;
           }
           return fos;
    }

    @Override
    public void uploadSucceeded(SucceededEvent event) {
        embedded.setVisible(true);
        embedded.setSource(new FileResource(file, DifferApplication.getCurrentApplication()));
        compareButton.setEnabled(true);
    }
      
    public File getFile() {
        return file;
    }
    
}
