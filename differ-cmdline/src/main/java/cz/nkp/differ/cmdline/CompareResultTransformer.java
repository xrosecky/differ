package cz.nkp.differ.cmdline;

import cz.nkp.differ.compare.io.ImageProcessorResult;

/**
 * User: stavel
 * Date: 2.1.13
 * Time: 18:39
 */
public interface CompareResultTransformer {
    public Boolean includeOutputs = false;
    public Boolean includeImage = false;
    public OutputNamer outputNamer = null;
    public Boolean saveProperties = false;
    public Boolean saveReport = false;

    public String transform(ImageProcessorResult results[]);
}
