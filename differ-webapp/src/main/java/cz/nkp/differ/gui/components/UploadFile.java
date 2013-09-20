package cz.nkp.differ.gui.components;

import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.Window;
import cz.nkp.differ.DifferApplication;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import javax.imageio.ImageIO;

/**
 * Immutable class for loading a remote file to the local filesystem, with the
 * ability to restrict by file size. Files are also filtered by extension, 
 * 
 * @author Thomas Truax
 */
public class UploadFile {
    
    public enum TYPE {
        LOCAL_FILESYSTEM, REMOTE_URL;
    }
    
    private TYPE type;
    
    private static String DIR = "/tmp/";
    private File file;
    private boolean valid = true;
    private String path;
    private URL url;
    
    private long maxSize = -1; //in bytes
    static private long DEFAULT_MAX_SIZE = 5242880; //default maxsize = 5MB
    
    static public String[] VALID_EXTENSIONS = {/*DJVU,SDJVU*/
                                                "djv", "djvu",
                                                /*FITS*/
                                                "fit", "fits", "fts",
                                                /*JPEG*/
                                                "jfi", "jfif", "jif", "jpe", "jpeg", "jpg",
                                                /*JPEG2000*/
                                                "j2k", "jp2", "jpf", "jpm", "jpx", "mj2",
                                                /*PDF*/
                                                "pdf",
                                                /*PNG*/
                                                "png",
                                                /*TIFF*/
                                                "tif", "tiff"
    };
    
    private String errorMessage;
    
    /**
     * Runs validation on uploaded files. If the file is a remote file, it is 
     * also downloaded and copied to the filesystem as long as it passed all 
     * validation checks. Use <b>isValid()</b> to check if the file passed all
     * validation and use <b>getFile()</b> to return the valid file.
     * @param type use either UploadFile.TYPE.LOCAL_FILESYSTEM or UploadFile.TYPE.REMOTE_URL
     * @param path Either a filesystem path or a URL
     * <br><i>Example:</i> /home/user/test_image.png OR http://www.differ.com/test_image.png
     */
    public UploadFile(TYPE type, String path) {
        this(type, path, 0);
    }
    
    /**
     * Runs validation on uploaded files. If the file is a remote file, it is 
     * also downloaded and copied to the filesystem as long as it passed all 
     * validation checks. Use <b>isValid()</b> to check if the file passed all
     * validation and use <b>getFile()</b> to return the valid file.
     * @param type Use either UploadFile.TYPE.LOCAL_FILESYSTEM or UploadFile.TYPE.REMOTE_URL
     * @param path Either a filesystem path or a URL
     * <br><i>Example:</i> /home/user/test_image.png OR http://www.differ.com/test_image.png
     * @param maxSize The maximum allowed file size, in bytes
     */
    public UploadFile(TYPE type, String path, long maxSize) {
        this.type = type;
        this.path = path;
        if ((this.maxSize = maxSize) <= 0) {
            this.maxSize = DEFAULT_MAX_SIZE;
        }
        switch (this.type) {
            case LOCAL_FILESYSTEM: 
                this.file = new File(path);
                this.file = validateLocalFile();
                break;
                
            case REMOTE_URL:
                this.file = validateRemoteFile();
                break;
                
            default: 
                break;
        }
    }
    
    /*
     * Runs validation checks on a local file.
     */
    private File validateLocalFile() {
        if (isValidSize()) {
            if (isValidExtension()) {
                return file;
            }
        }
        return null;
    }
    
    /*
     * Runs validation checks on a remote file.
     */
    private File validateRemoteFile() {
        if (isValidURL()) {
            try {
                StringBuilder sb = new StringBuilder(path);
                String tpath = DIR + sb.substring(sb.lastIndexOf("/") + 1);

                url = new URL(path);

                if (isValidSize()) {
                    InputStream is = url.openStream();

                    file = new File(tpath);

                    if (isValidExtension()) {
                        FileOutputStream fos = new FileOutputStream(file);

                        int read = 0;
                        byte[] bytes = new byte[1024];

                        while ((read = is.read(bytes)) != -1) {
                                fos.write(bytes, 0, read);
                        }
                        
                        fos.close();
                    }
                    
                    is.close();
                }

            } catch (IOException ioe) {
                errorMessage = ioe.getMessage();
                valid = false;
                return null;
            }
            return file;
        } 
        return null;
    }    
    
    /*
     * Checks if (remote) file has a malformed URL or an invalid URI.
     */
    private boolean isValidURL() {  

        URL u = null;

        try {  
            u = new URL(path);  
        } catch (MalformedURLException mue) {
            errorMessage = "URL is invalid";
            return valid = false;  
        }

        try {  
            u.toURI();  
        } catch (URISyntaxException use) { 
            errorMessage = "URI is invalid";
            return valid = false;  
        }  

        return true;  
    } 

    /*
     * Checks file size against the maximum size allowed.
     */
    private boolean isValidSize() {
        String usertype = "anonymous";
        if (maxSize != DEFAULT_MAX_SIZE) {
            usertype = "registered";
        }
        switch (type) {
            case LOCAL_FILESYSTEM: 
                if (file.length() > maxSize) {
                    errorMessage = "File must not exceed " + (maxSize/1048576) + "MB for " + usertype + " users";
                    return valid = false;
                }
                return true;
                
            case REMOTE_URL: 
                try {
                    if (url.openConnection().getContentLength() > maxSize) {
                        errorMessage = "File must not exceed " + (maxSize/1048576) + "MB for " + usertype + " users";
                        return valid = false;
                    }
                } catch (IOException ioe) {
                    return valid = false;
                }
                return true;
                
        }

        return valid = false;
    }
    
    /* 
     * Checks file extension against the array of extensions in: VALID_EXTENSIONS 
     */
    private boolean isValidExtension() {
        String ext = "";
        String filename;
        int extIndex;
        if ((filename = file.getName()) != null) {
            if ((extIndex = filename.lastIndexOf('.') + 1) > 0) {
                ext = filename.substring(extIndex);
                for (String s : VALID_EXTENSIONS) {
                    if (s.equalsIgnoreCase(ext)) {
                        return true;
                    }
                }
            }
        }
        errorMessage = "File extension " + ext.toUpperCase() + " is invalid";
        return valid = false;
    }
        
    
    /**
     * Returns a message detailing the most recent error (if
     * any) caused during file validation.
     * @return a human readable String
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    
    /**
     * Returns the validity of the uploaded file.
     * @return true if the file was valid, else false if:
     *         <br>-A bad URL or URI was detected (remote files only)
     *         <br>-An invalid file extension was detected
     *         <br>-The size of the file exceeded the maximum allowed size
     * 
     */
    public boolean isValid() {
        if (file != null) {
            return valid;
        }
        return valid = false;
    }
    
    /**
     * Returns a local copy of the uploaded file. 
     * @return null if the file failed any validation checks
     */
    public File getFile() {
        return file;
    }
}
