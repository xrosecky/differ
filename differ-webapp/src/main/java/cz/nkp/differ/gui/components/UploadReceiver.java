package cz.nkp.differ.gui.components;

import com.vaadin.ui.Upload.Receiver;
import cz.nkp.differ.DifferApplication;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 *
 * @author Thomas Truax
 */
public class UploadReceiver implements Receiver {
    
    private static String tempDir = "/tmp/differ/anon_uploads/";
    private String filePrefix;
    private File file;
    private static int i;
    
    public UploadReceiver() {
        this.filePrefix = "image" + (++i) + "-";
    }
 
    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {           
           FileOutputStream fos;
           try {
               File dir = new File(tempDir);
               if (!dir.exists()) {
                   dir.mkdirs();
               }
               //guarantee filename is unique before writing to stream
               while ((file = new File(tempDir + filePrefix + filename)).exists()) {
                   filePrefix = "image" + (++i) + "-";
               }
               fos = new FileOutputStream(file);
           } catch (FileNotFoundException io) {
               DifferApplication.getCurrentApplication().getMainWindow().showNotification("Error", "Error while uploading file");
               return null;
           }
           return fos;
    }
      
    public File getFile() {
        return file;
    }
    
}
