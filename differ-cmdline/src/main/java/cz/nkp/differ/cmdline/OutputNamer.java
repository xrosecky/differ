package cz.nkp.differ.cmdline;

import cz.nkp.differ.compare.io.ImageProcessorResult;

import java.io.File;

/**
 * User: stavel
 * Date: 2.1.13
 * Time: 18:39
 */
public interface OutputNamer {
    public File baseName(File file, ImageProcessorResult result);
    public File rawOutputName(File file, ImageProcessorResult result, String source);
    public File reportName(File file, ImageProcessorResult result);
    public File textName(File file, ImageProcessorResult result);
    public File textCompareName(File file1, File file2, ImageProcessorResult result1, ImageProcessorResult result2);
    public File propertiesSummaryName(File file, ImageProcessorResult result);
}
