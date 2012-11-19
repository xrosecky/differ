package cz.nkp.differ.compare.io.generators;

import com.vaadin.ui.Component;
import cz.nkp.differ.compare.io.ImageDatasetProcessor;

import cz.nkp.differ.plugins.tools.DelayedComponentGenerator;

public class HashComponentGenerator extends DelayedComponentGenerator.ComponentGenerator {

    private boolean isSynthesizedImage = false;
    private boolean hashesEqual = false;
    private ImageDatasetProcessor imageProcessor;

    public HashComponentGenerator(boolean isSynthesizedImage, boolean hashesEqual, ImageDatasetProcessor imageProcessor) {
	this.isSynthesizedImage = isSynthesizedImage;
	this.hashesEqual = hashesEqual;
	this.imageProcessor = imageProcessor;
    }

    @Override
    public Component generateComponent(Component c) {
	String hashValue;

	if (isSynthesizedImage) {
	    if (hashesEqual) {
		hashValue = "Are Equal";
	    } else {
		hashValue = "Not Equal";
	    }
	    hashesEqual = false;
	} else {
	    hashValue = imageProcessor.getImageMD5();
	}

	c.setCaption("Hash: " + hashValue);
	return c;
    }
}
