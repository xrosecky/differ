package cz.nkp.differ.compare.metadata.external.result;

import cz.nkp.differ.compare.metadata.external.ResultEntryValueTransformer;

/**
 * Created with IntelliJ IDEA.
 * User: stavel
 * Date: 13.2.13
 * Time: 20:32
 */
public class DateNormalizer implements ResultEntryValueTransformer {
    @Override
    public String transform(String value) {
         return value.replace("T"," ");
    }
}
