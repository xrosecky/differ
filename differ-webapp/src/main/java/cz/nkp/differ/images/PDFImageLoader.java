package cz.nkp.differ.images;

import cz.nkp.differ.exceptions.ImageDifferException;
import java.awt.image.BufferedImage;
import java.io.File;

import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;

/**
 *
 * @author xrosecky
 */
public class PDFImageLoader implements ImageLoader {

    @Override
    public BufferedImage load(File file) throws ImageDifferException {
	PdfDecoder pdf = new PdfDecoder();
	try {
	    pdf.openPdfFile(file.getAbsolutePath());
	    BufferedImage image = pdf.getPageAsImage(1);
	    return image;
	} catch (PdfException pe) {
	    throw new ImageDifferException(ImageDifferException.ErrorCode.IMAGE_READ_ERROR,
		    String.format("Error reading image: %s", file.getAbsolutePath()), pe);
	}
    }
}
