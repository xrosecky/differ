package cz.nkp.differ.images;

import cz.nkp.differ.exceptions.ImageDifferException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author xrosecky
 */
public class GenericImageLoader implements ImageLoader {

    @Override
    public BufferedImage load(File file) throws ImageDifferException {
	BufferedImage image = null;
	try {
	    image = ImageIO.read(file);
	} catch (IOException e) {
	    throw new ImageDifferException(ImageDifferException.ErrorCode.IMAGE_READ_ERROR,
		    String.format("Error reading image: %s", file.getAbsolutePath()), e);
	}
	return image;
    }

}
