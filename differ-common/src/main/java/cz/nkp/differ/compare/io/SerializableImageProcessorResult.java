package cz.nkp.differ.compare.io;

import cz.nkp.differ.compare.metadata.ImageMetadata;
import cz.nkp.differ.compare.metadata.MetadataSource;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author xrosecky
 */
@XmlRootElement(name = "result")
@XmlType(propOrder = {"type", "width", "height", "checksum", "metadata", "sources", "histogram", "fullImage", "preview"})
@XmlAccessorType(XmlAccessType.FIELD)
public class SerializableImageProcessorResult implements ImageProcessorResult {

    @XmlElement(name = "image-full")
    private SerializableImage fullImage;
    @XmlElement(name = "image-preview")
    private SerializableImage preview;
    @XmlElementWrapper(name = "histogram-list")
    @XmlElement(name = "histogram")
    private int[][] histogram;
    private String checksum;
    private Type type;
    private int width;
    private int height;
    @XmlElementWrapper(name = "metadata-list")
    @XmlElement(name = "metadata")
    private List<ImageMetadata> metadata = null;
    @XmlElementWrapper(name = "sources-list")
    @XmlElement(name = "source")
    private List<MetadataSource> sources = null;

    public SerializableImageProcessorResult() {
    }

    @Override
    public Image getFullImage() {
	return fullImage;
    }

    public void setFullImage(SerializableImage fullImage) {
	this.fullImage = fullImage;
    }

    @Override
    public int[][] getHistogram() {
	return histogram;
    }

    public void setHistogram(int histogram[][]) {
	this.histogram = histogram;
    }

    @Override
    public Image getPreview() {
	return preview;
    }

    public void setPreview(SerializableImage preview) {
	this.preview = preview;
    }

    @Override
    public String getMD5Checksum() {
	return checksum;
    }

    public void setMD5Checksum(String checksum) {
	this.checksum = checksum;
    }

    @Override
    public Type getType() {
	return type;
    }

    public void setType(Type type) {
	this.type = type;
    }

    @Override
    public int getHeight() {
	return height;
    }

    public void setHeight(int height) {
	this.height = height;
    }

    @Override
    public int getWidth() {
	return width;
    }

    public void setWidth(int width) {
	this.width = width;
    }

    @Override
    public List<ImageMetadata> getMetadata() {
	return metadata;
    }

    public void setMetadata(List<ImageMetadata> metadata) {
	this.metadata = metadata;
    }

    public String getChecksum() {
	return checksum;
    }

    public void setChecksum(String checksum) {
	this.checksum = checksum;
    }

    public List<MetadataSource> getSources() {
	return sources;
    }

    public void setSources(List<MetadataSource> sources) {
	this.sources = sources;
    }

    public static SerializableImageProcessorResult create(ImageProcessorResult original, boolean fullImage) {
	SerializableImageProcessorResult result = new SerializableImageProcessorResult();
	result.setHeight(original.getHeight());
	result.setWidth(original.getWidth());
	result.setMetadata(original.getMetadata());
	result.setHistogram(original.getHistogram());
	result.setType(original.getType());
	Set<MetadataSource> sources = new HashSet<MetadataSource>();
	for (ImageMetadata metadata : original.getMetadata()) {
	    sources.add(metadata.getSource());
	}
	result.setSources(new ArrayList<MetadataSource>());
	result.getSources().addAll(sources);
	if (original.getPreview() != null) {
	    try {
		result.setPreview(new SerializableImage(original.getPreview()));
	    } catch (IOException ioe) {
		result.setPreview(null);
	    }
	}
	if (fullImage && original.getFullImage() != null) {
	    try {
		result.setPreview(new SerializableImage(original.getFullImage()));
	    } catch (IOException ioe) {
		result.setFullImage(null);
	    }
	}
	return result;
    }
}
