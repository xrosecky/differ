package cz.nkp.differ.compare.metadata;

import java.util.List;

/**
 *
 * @author xrosecky
 */
public class MetadataExtractors {

    private List<MetadataExtractor> extractors;

    public List<MetadataExtractor> getExtractors() {
	return extractors;
    }

    public void setExtractors(List<MetadataExtractor> extractors) {
	this.extractors = extractors;
    }
    
}
