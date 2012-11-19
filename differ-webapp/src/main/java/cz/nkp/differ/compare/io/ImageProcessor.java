package cz.nkp.differ.compare.io;

import cz.nkp.differ.exceptions.ImageDifferException;
import cz.nkp.differ.model.Image;
import cz.nkp.differ.plugins.PluginComponentReadyCallback;

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

    public abstract ImageProcessorResult processImage(Image image, PluginComponentReadyCallback callback) throws ImageDifferException;
    public abstract ImageProcessorResult[] processImages(Image a, Image b, PluginComponentReadyCallback callback) throws ImageDifferException;

    public ImageProcessorResult processImage(Image image) throws ImageDifferException {
	return processImage(image, null);
    }

    public ImageProcessorResult[] processImages(Image a, Image b) throws ImageDifferException {
	return processImages(a, b, null);
    }

}
