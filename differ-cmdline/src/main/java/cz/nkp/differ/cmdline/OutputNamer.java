package cz.nkp.differ.cmdline;

import cz.nkp.differ.compare.io.ImageProcessorResult;

/**
 * User: stavel
 * Date: 2.1.13
 * Time: 18:39
 */
public interface OutputNamer {
    public String baseName(ImageProcessorResult result);
    public String rawOutputName(ImageProcessorResult result, String source);
    public String reportName(ImageProcessorResult result);
    public String textName(ImageProcessorResult result);
    public String textCompareName(ImageProcessorResult result1, ImageProcessorResult result2);
}
