package cz.nkp.differ.compare.io;

import cz.nkp.differ.exceptions.ImageDifferException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author xrosecky
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SerializableImage extends Image {

    private static final String IMAGE_FORMAT = "png";
    @XmlAttribute(name="format")
    private String format;
    @XmlElement(name="data")
    private byte[] data;
    private transient Image image;

    public SerializableImage() {
    }

    public SerializableImage(Image image) throws IOException {
	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	ImageIO.write(convert(image), IMAGE_FORMAT, bos);
	this.data = bos.toByteArray();
	this.format = IMAGE_FORMAT;
    }

    public void check() {
	if (image == null) {
	    if (data == null) {
		throw new NullPointerException("data");
	    }
	    ByteArrayInputStream bis = new ByteArrayInputStream(data);
	    try {
		this.image = ImageIO.read(bis);
	    } catch (IOException ioe) {
		throw new RuntimeException("Can't read image", ioe);
	    }
	}
    }

    @Override
    public Graphics getGraphics() {
	check();
	return image.getGraphics();
    }

    @Override
    public int getHeight(ImageObserver observer) {
	check();
	return image.getHeight(observer);
    }

    @Override
    public Object getProperty(String name, ImageObserver observer) {
	check();
	return image.getProperty(name, observer);
    }

    @Override
    public ImageProducer getSource() {
	check();
	return image.getSource();
    }

    @Override
    public int getWidth(ImageObserver observer) {
	check();
	return image.getWidth(observer);
    }

    public static BufferedImage convert(Image image) {
	if (image instanceof BufferedImage) {
	    return (BufferedImage) image;
	}
	BufferedImage result = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
	Graphics2D graphic = result.createGraphics();
	graphic.drawImage(image, null, null);
	return result;
    }
}
