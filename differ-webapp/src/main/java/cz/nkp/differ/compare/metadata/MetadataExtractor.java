package cz.nkp.differ.compare.metadata;

import cz.nkp.differ.model.Image;
import java.util.List;

/**
 *
 * @author xrosecky
 */
public interface MetadataExtractor {

    public List<ImageMetadata> getMetadata(Image image);

}
