package cz.nkp.differ.images;

import com.lizardtech.djvu.DjVuPage;
import com.lizardtech.djvu.Document;
import com.lizardtech.djvubean.DjVuImage;
import cz.nkp.differ.exceptions.ImageDifferException;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;

/**
 *
 * @author xrosecky
 * @author Joshua Mabrey
 */
public class DjvuImageLoader implements ImageLoader {

    /**
     * this gets rid of exception for not using native acceleration as well
     * as the djvu debug info on the output console
     */
    static {
	System.setProperty("com.sun.media.jai.disableMediaLib", "true");
	PrintStream nullPrintStream = new PrintStream(new OutputStream() {

	    public void write(int c) {
	    }
	});
	com.lizardtech.djvu.DjVuOptions.out = nullPrintStream;
	com.lizardtech.djvu.DjVuOptions.err = nullPrintStream;
    }

    @Override
    public BufferedImage load(File file) throws ImageDifferException {
	BufferedImage image = null;
	Document document = new Document();
	document.setAsync(false);
	Image image_local;
	try {
	    document.read(file.toURI().toURL());
	    DjVuPage[] page = {document.getPage(0, DjVuPage.MAX_PRIORITY, true)};
	    DjVuImage djvuImage = new DjVuImage(page, true);
	    image_local = djvuImage.getImage(new Canvas(), djvuImage.getPageBounds(0))[0];
	} catch (MalformedURLException e) {
	    throw new ImageDifferException(ImageDifferException.ErrorCode.IMAGE_READ_ERROR,
		    "Error reading image", e);
	} catch (IOException e) {
	    throw new ImageDifferException(ImageDifferException.ErrorCode.IMAGE_READ_ERROR,
		    "Error reading image", e);
	}
	image = new BufferedImage(image_local.getWidth(null), image_local.getHeight(null), BufferedImage.TYPE_INT_ARGB);
	Graphics g = image.createGraphics();
	g.drawImage(image_local, 0, 0, null);
	g.dispose();
	return image;
    }
}
