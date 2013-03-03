package cz.nkp.differ.compare.metadata.external.result;

import cz.nkp.differ.compare.metadata.external.ResultEntryValueTransformer;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: stavel
 * Date: 13.2.13
 * Time: 20:09
 */
public class FilePathNormalizer implements ResultEntryValueTransformer {
    @Override
    public String transform(String value) {
        File file = new File(value);
        String filePath = file.getAbsolutePath();
        return filePath;
    }
}
