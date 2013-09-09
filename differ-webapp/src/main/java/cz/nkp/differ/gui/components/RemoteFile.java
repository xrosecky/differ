package cz.nkp.differ.gui.components;

import com.vaadin.ui.Window;
import cz.nkp.differ.DifferApplication;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Immutable class for loading a remote file to the local filesystem, with the
 * ability to restrict by file size.
 * 
 * @author Thomas Truax
 */
public class RemoteFile {
    
    private static String DIR = "/tmp/";
    private File file;
    private boolean valid;
    
    private long maxSize = -1; //in bytes
    static private long DEFAULT_MAX_SIZE = 5242880; //default maxsize = 5MB
    
    /**
     * Downloads a remote file from the supplied URL and writes it to the local filesystem.
     * If the URL is valid and the file was downloaded and written successfully,
     * it may be obtained via the <b>getFile()</b> method.
     * @param url 
     */
    public RemoteFile(String url) {
        this(url, 0);
    }
    
    public RemoteFile(String url, long maxSize) {
        if ((this.maxSize = maxSize) <= 0) {
            this.maxSize = DEFAULT_MAX_SIZE;
        }
        file = getRemoteFile(url);
    }
    
    private File getRemoteFile(String url) {
        if (!isValidURL(url)) {
            DifferApplication.getCurrentApplication().getMainWindow().showNotification("Malformed URL",
                    "The URL does not appear to be valid", Window.Notification.TYPE_WARNING_MESSAGE);
            return null;
        }
        //File file = null;
        try {
            StringBuilder sb = new StringBuilder(url);
            String path = DIR + sb.substring(sb.lastIndexOf("/") + 1);
            
            URL remfile = new URL(url);

            checkSize(remfile);
            
            InputStream is = remfile.openStream();

            FileOutputStream fos = new FileOutputStream(file = new File(path));

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = is.read(bytes)) != -1) {
                    fos.write(bytes, 0, read);
            }

            fos.close();
            is.close();
        } catch (IOException ioe) {
            DifferApplication.getCurrentApplication().getMainWindow().showNotification("Remote file failed to upload",
                    "<br/>" + ioe.getMessage(), Window.Notification.TYPE_WARNING_MESSAGE);
        }
        return file;
    }    
    
    private boolean isValidURL(String url) {  

        URL u = null;

        try {  
            u = new URL(url);  
        } catch (MalformedURLException e) {  
            return false;  
        }

        try {  
            u.toURI();  
        } catch (URISyntaxException e) {  
            return false;  
        }  

        return valid = true;  
    } 
    
    /**
     * Returns the validity of the URL passed to the constructor.
     * @return false if the supplied URL failed the URL & URI syntax
     * test, else true
     */
    public boolean isValid() {
        return valid;
    }
    
    private void checkSize(URL u) throws IOException {
        if (u.openConnection().getContentLength() > maxSize) {
            throw new IOException("File must not exceed 5MB for anonymous users");
        }
    }
    
    /**
     * Returns a local copy of the remote file which was downloaded. 
     * @return null if the remote file's URL was invalid or if it failed to
     * download or if it failed to write to the local filesystem
     */
    public File getFile() {
        return file;
    }
}
