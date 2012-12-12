package cz.nkp.differ.images;

import cz.nkp.differ.exceptions.ImageDifferException;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 *
 * @author xrosecky
 */
public interface ImageLoader {

    public BufferedImage load(File file) throws ImageDifferException;

}
