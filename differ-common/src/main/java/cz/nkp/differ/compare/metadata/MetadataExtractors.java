package cz.nkp.differ.compare.metadata;

import java.util.List;
import java.util.Map;

/**
 *
 * @author xrosecky
 */
public class MetadataExtractors {

    private List<MetadataExtractor> extractors;

    private Map<String,List<MetadataExtractor>> extractorsByFileExtension;

    public List<MetadataExtractor> getExtractors() {
		return extractors;
    }

    public void setExtractors(List<MetadataExtractor> extractors) {
		this.extractors = extractors;
    }
    
	public Map<String,List<MetadataExtractor>> getExtractorsByFileExtension(){
		return extractorsByFileExtension;
	}
	public void setExtractorsByFileExtension ( Map<String,List<MetadataExtractor>> extractorsByFileExtension){
		this.extractorsByFileExtension = extractorsByFileExtension;
	}
}
