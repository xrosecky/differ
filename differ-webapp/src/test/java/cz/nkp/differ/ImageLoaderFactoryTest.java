package cz.nkp.differ;

import cz.nkp.differ.exceptions.ImageDifferException;
import cz.nkp.differ.images.ImageLoader;
import java.awt.image.BufferedImage;
import java.io.File;
import org.junit.Test;

/**
 *
 * @author xrosecky
 */
public class ImageLoaderFactoryTest {

    private ImageLoader imageLoader = null;

    public ImageLoaderFactoryTest() {
	imageLoader = Helper.getImageLoader();
    }

    @Test
    public void testGenericImage() throws ImageDifferException {
	File file = new File("/home/xrosecky/6-20-16.jpg");
	BufferedImage img = imageLoader.load(file);
    }

}