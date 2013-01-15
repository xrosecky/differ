package cz.nkp.differ.cmdline;

import cz.nkp.differ.compare.io.ImageProcessorResult;

/**
 * User: stavel
 * Date: 2.1.13
 * Time: 18:39
 */
public interface ResultTransformer {
    public Boolean includeOutputs = false;
    public Boolean includeImage = false;
    public Boolean saveReport = false;
    public Boolean saveProperties = false;
    public OutputNamer outputNamer = null;

    public String transform(ImageProcessorResult result);
}
