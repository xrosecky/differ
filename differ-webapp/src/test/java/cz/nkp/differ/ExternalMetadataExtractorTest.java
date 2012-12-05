package cz.nkp.differ;

import cz.nkp.differ.compare.metadata.ImageMetadata;
import cz.nkp.differ.compare.metadata.MetadataExtractor;
import cz.nkp.differ.model.Image;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author xrosecky
 */
public class ExternalMetadataExtractorTest {

    @Test
    public void testMetadataExtractors() throws Exception {
	List<MetadataExtractor> extractors = Helper.getMetadataExtractors().getExtractors();
	for (MetadataExtractor extractor : extractors) {
            Image image = new Image();
	    List<ImageMetadata> metadata = extractor.getMetadata(image.getFile());
	    for (ImageMetadata entry : metadata) {
		System.err.println(String.format("%s, %s, %s", entry.getKey(), entry.getValue(), entry.getSource()));
	    }
	}
    }

}
