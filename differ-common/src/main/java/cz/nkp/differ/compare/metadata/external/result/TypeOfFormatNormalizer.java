package cz.nkp.differ.compare.metadata.external.result;

import cz.nkp.differ.compare.metadata.external.ResultEntryReplacer;
import cz.nkp.differ.compare.metadata.external.ResultEntryValueTransformer;

/**
 * Created with IntelliJ IDEA.
 * User: stavel
 * Date: 11.2.13
 * Time: 16:08
 * To change this template use File | Settings | File Templates.
 */
public class TypeOfFormatNormalizer implements ResultEntryValueTransformer {

    @Override
    public String transform(String value) {
        return value.replaceFirst("JPEG.*","JPEG");
    }
}
