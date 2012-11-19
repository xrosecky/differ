package cz.nkp.differ.images;

import cz.nkp.differ.exceptions.ImageDifferException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;

/**
 *
 * @author xrosecky
 */
public class ImageLoaderFactory implements ImageLoader {

    private Map<String, ImageLoader> loaders = null;
    private ImageLoader defaultImageLoader = null;

    public Map<String, ImageLoader> getLoaders() {
	return loaders;
    }

    public void setLoaders(Map<String, ImageLoader> loaders) {
	this.loaders = loaders;
    }

    public ImageLoader getDefaultImageLoader() {
	return defaultImageLoader;
    }

    public void setDefaultImageLoader(ImageLoader defaultImageLoader) {
	this.defaultImageLoader = defaultImageLoader;
    }

    @Override
    public BufferedImage load(File file) throws ImageDifferException {
	String extension = getExtension(file.getName());
	ImageLoader imageLoader = null;
	if (loaders != null) {
	    imageLoader = loaders.get(extension);
	}
	if (imageLoader == null) {
	    imageLoader = defaultImageLoader;
	}
	return imageLoader.load(file);
    }

    private static String getExtension(String fileName) {
	String extension = "";
	int dotAt = fileName.lastIndexOf('.');
	if (dotAt != -1) {
	    extension = fileName.substring(dotAt + 1);
	}
	return extension;
    }
}
