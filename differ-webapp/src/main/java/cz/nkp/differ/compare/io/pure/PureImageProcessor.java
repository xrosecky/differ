package cz.nkp.differ.compare.io.pure;

import cz.nkp.differ.compare.metadata.ImageMetadata;
import cz.nkp.differ.compare.io.ImageProcessor;
import cz.nkp.differ.compare.io.ImageProcessorResult;
import cz.nkp.differ.compare.metadata.MetadataExtractor;
import cz.nkp.differ.compare.metadata.MetadataExtractors;
import cz.nkp.differ.compare.metadata.MetadataSource;
import cz.nkp.differ.exceptions.ImageDifferException;
import cz.nkp.differ.images.ImageLoader;
import cz.nkp.differ.images.ImageManipulator;
import cz.nkp.differ.model.Image;
import cz.nkp.differ.plugins.PluginComponentReadyCallback;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Implementation of ImageProcessor in Java
 *
 * @author xrosecky
 */
public class PureImageProcessor extends ImageProcessor {

    private ImageLoader imageLoader;
    private MetadataExtractors extractors;
    private MetadataSource core = new MetadataSource(0, "", "", "core");

    public PureImageProcessor(ImageLoader imageLoader, MetadataExtractors extractors) {
        this.imageLoader = imageLoader;
        this.extractors = extractors;
    }

    @Override
    public PureImageProcessorResult processImage(Image image, PluginComponentReadyCallback callback) throws ImageDifferException {
        BufferedImage fullImage = imageLoader.load(image.getFile());
        java.awt.Image preview = ImageManipulator.getBitmapScaledImage(fullImage, this.getConfig().getImageWidth(), true);
        PureImageProcessorResult result = new PureImageProcessorResult(fullImage, preview);
        result.setType(ImageProcessorResult.Type.IMAGE);
        processImage(fullImage, result);
        result.getMetadata().add(new ImageMetadata("height", new Integer(fullImage.getHeight()), core));
        result.getMetadata().add(new ImageMetadata("width", new Integer(fullImage.getWidth()), core));
        for (MetadataExtractor extractor : extractors.getExtractors()) {
            List<ImageMetadata> metadata = extractor.getMetadata(image.getFile());
            result.getMetadata().addAll(metadata);

        }

        return result;
    }

    @Override
    public ImageProcessorResult[] processImages(Image a, Image b, PluginComponentReadyCallback callback) throws ImageDifferException {
        PureImageProcessorResult results[] = new PureImageProcessorResult[3];
        results[0] = this.processImage(a, null);
        results[1] = this.processImage(b, null);
        try {
            BufferedImage compareFull = getImagesDifference(results[0].getFullImage(), results[1].getFullImage());
            java.awt.Image comparePreview = ImageManipulator.getBitmapScaledImage(compareFull, this.getConfig().getImageWidth(), true);
            PureImageProcessorResult result = new PureImageProcessorResult(compareFull, comparePreview);
            result.setType(ImageProcessorResult.Type.COMPARISON);
            this.processImage(compareFull, result);
            this.addMetrics(result);
            results[2] = result;
        } catch (Exception ex) {
            ex.printStackTrace();
            results[2] = null;
        }
        return results;
    }

    private void addMetrics(PureImageProcessorResult result) {
        long size = result.getHeight() * result.getWidth();
        int[][] histogram = result.getHistogram();
        String[] colours = new String[]{"red", "green", "blue"};
        MetadataSource mseSource = new MetadataSource(0, "", "", "MSE");
        MetadataSource psnrSource = new MetadataSource(0, "", "", "PSNR");
        for (int i = 0; i < 3; i++) {
            String colour = colours[i];
            long sqSum = 0;
            int[] values = histogram[i];
            for (int j = 0; j < values.length; j++) {
                sqSum += values[j] * j * j;
            }
            double mse = (1.0 / size) * sqSum;
            double psnr = 10 * Math.log10((255 * 255) / mse);
            result.getMetadata().add(new ImageMetadata(colour, mse, mseSource));
            result.getMetadata().add(new ImageMetadata(colour, psnr, psnrSource));
        }
    }

    private PureImageProcessorResult processImage(BufferedImage image, PureImageProcessorResult result) {
        int width = image.getWidth();
        int height = image.getHeight();
        result.setWidth(width);
        result.setHeight(height);
        // histogram
        int[] imagePixelCache = new int[width * height];
        image.getRGB(0, 0, width, height, imagePixelCache, 0, width); //Get all pixels
        int[][] bins = new int[3][256];
        for (int thisPixel = 0; thisPixel < width * height; thisPixel++) {
            int rgbCombined = imagePixelCache[thisPixel];
            int red = new Color(rgbCombined).getRed();
            bins[0][red]++;
            int green = new Color(rgbCombined).getGreen();
            bins[1][green]++;
            int blue = new Color(rgbCombined).getBlue();
            bins[2][blue]++;
        }
        result.setHistogram(bins);
        // checksum
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        for (int i : imagePixelCache) {
            byteStream.write(i);
        }
        String md5 = DigestUtils.md5Hex(byteStream.toByteArray());
        try {
            byteStream.close();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        result.setMD5Checksum(md5);
        return result;
    }

    private static BufferedImage getImagesDifference(BufferedImage image1, BufferedImage image2) {
        if (image1 == null) {
            throw new NullPointerException("image1");
        }
        if (image2 == null) {
            throw new NullPointerException("image2");
        }
        if (image1.getWidth(null) != image2.getWidth(null)
                || image1.getHeight(null) != image2.getHeight(null)) {
            throw new IllegalArgumentException("Cannot XOR images that are differing dimensions. XOR'ing failed.");
        }
        if (image1.getTransparency() != image2.getTransparency()) {
            throw new IllegalArgumentException("Cannot XOR images that are differing transparencies. XOR'ing failed.");
        }
        int type = BufferedImage.TYPE_INT_RGB;
        if (image1.getType() != type) {
            image1 = convert(image1, type);
        }
        if (image2.getType() != type) {
            image2 = convert(image2, type);
        }
        int width = image1.getWidth(null);
        int height = image1.getHeight(null);
        int resolution = width * height;

        int[] combo1Pixels = new int[resolution];
        int[] combo2Pixels = new int[resolution];
        int[] imagePixels = new int[resolution];

        image1.getRGB(0, 0, width, height, combo1Pixels, 0, width); //Get all pixels
        image2.getRGB(0, 0, width, height, combo2Pixels, 0, width); //Get all pixels

        for (int pixel = 0; pixel < resolution; pixel++) {
            imagePixels[pixel] = Math.abs(combo1Pixels[pixel] - combo2Pixels[pixel]);
        }

        int imageType = image1.getType();
        if (imageType == BufferedImage.TYPE_CUSTOM) {
            imageType = BufferedImage.TYPE_INT_RGB;
        }
        BufferedImage imageXOR = new BufferedImage(width, height, imageType);
        imageXOR.setRGB(0, 0, width, height, imagePixels, 0, width); //Set all pixels

        return imageXOR;
    }

    private static BufferedImage convert(BufferedImage image, int type) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), type);
        ColorConvertOp op = new ColorConvertOp(null);
        op.filter(image, result);
        return result;
    }
}
