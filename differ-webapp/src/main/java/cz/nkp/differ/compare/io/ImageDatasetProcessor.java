package cz.nkp.differ.compare.io;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ImageDatasetProcessor {

    private static Logger LOGGER = Logger.getLogger(ImageDatasetProcessor.class);
    private int[] imagePixelCache = null;
    private int image_width_cached = 0, image_height_cached = 0;
    private BufferedImage image = null;

    public ImageDatasetProcessor(BufferedImage image) {
        this.image = image;
        loadImage();
    }

    private final void loadImage() {
        image_width_cached = image.getWidth();
        image_height_cached = image.getHeight();
        imagePixelCache = new int[image_width_cached * image_height_cached];
        image.getRGB(0, 0, image_width_cached, image_height_cached, imagePixelCache, 0, image_width_cached); //Get all pixels
    }

    public final XYDataset getHistogramDataset() {
        XYSeries redChannel = new XYSeries("Red");
        XYSeries greenChannel = new XYSeries("Green");
        XYSeries blueChannel = new XYSeries("Blue");

        int[][] bins = new int[3][256];

        for (int thisPixel = 0; thisPixel < image_width_cached * image_height_cached; thisPixel++) {

            int rgbCombined = imagePixelCache[thisPixel];

            int red = new Color(rgbCombined).getRed();
            bins[0][red]++;

            int green = new Color(rgbCombined).getGreen();
            bins[1][green]++;

            int blue = new Color(rgbCombined).getBlue();
            bins[2][blue]++;
        }

        for (int i = 0; i < 256; i++) {
            redChannel.add(i, bins[0][i]);
            greenChannel.add(i, bins[1][i]);
            blueChannel.add(i, bins[2][i]);
        }

        XYSeriesCollection rgb = new XYSeriesCollection();
        rgb.addSeries(redChannel);
        rgb.addSeries(greenChannel);
        rgb.addSeries(blueChannel);

        return rgb;
    }

    public final String getImageMD5() {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        for (int i : imagePixelCache) {
            byteStream.write(i);
        }
        String md5 = DigestUtils.md5Hex(byteStream.toByteArray());
        try {
            byteStream.close();
        } catch (IOException e) {
            LOGGER.warn("Unable to close outputstream while generating md5");
        }

        return md5;
    }
}
