package cz.nkp.differ.compare.io;

import cz.nkp.differ.exceptions.ImageDifferException;
import cz.nkp.differ.listener.ProgressListener;
import java.io.File;

/**
 *
 * @author xrosecky
 */
public abstract class ImageProcessor {

    protected ImageProcessorConfig config;

    public ImageProcessorConfig getConfig() {
	return config;
    }

    public void setConfig(ImageProcessorConfig config) {
	this.config = config;
    }

    public abstract ImageProcessorResult processImage(File image, ProgressListener callback) throws ImageDifferException;
    public abstract ImageProcessorResult[] processImages(File a, File b, ProgressListener callback) throws ImageDifferException;

    public ImageProcessorResult processImage(File image) throws ImageDifferException {
	return processImage(image, null);
    }

    public ImageProcessorResult[] processImages(File a, File b) throws ImageDifferException {
	return processImages(a, b, null);
    }

}
