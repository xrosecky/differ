package cz.nkp.differ.compare.io;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author xrosecky
 */
@XmlRootElement(name = "results")
@XmlAccessorType(XmlAccessType.FIELD)
public class SerializableImageProcessorResults {

    @XmlElement(name = "result")
    private List<SerializableImageProcessorResult> results;

    public SerializableImageProcessorResults(List<SerializableImageProcessorResult> results) {
        this.results = results;
    }

    public SerializableImageProcessorResults() {
    }

    public List<SerializableImageProcessorResult> getResults() {
        return results;
    }

    public void setResults(List<SerializableImageProcessorResult> results) {
        this.results = results;
    }
    
}
