package cz.nkp.differ.compare.metadata;

import java.io.File;
import java.util.List;

/**
 *
 * @author xrosecky
 */
public abstract class AbstractMetadataExtractor implements MetadataExtractor {

    private List<String> supportedFileExtensions;

    public List<String> getSupportedFileExtensions() {
        return supportedFileExtensions;
    }

    public void setSupportedFileExtensions(List<String> supportedFileExtensions) {
        this.supportedFileExtensions = supportedFileExtensions;
    }
    
    public boolean isSupported(File file) {
        if (supportedFileExtensions == null || supportedFileExtensions.isEmpty()) {
            return true;
        }
        String extension = "";
        int dot = file.getName().lastIndexOf('.');
        if (dot > 0) {
            extension = file.getName().substring(dot+1);
        }
        return supportedFileExtensions.contains(extension.toLowerCase());
    }
    
}
