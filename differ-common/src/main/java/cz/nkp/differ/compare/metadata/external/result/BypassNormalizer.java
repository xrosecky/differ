package cz.nkp.differ.compare.metadata.external.result;

import cz.nkp.differ.compare.metadata.external.ResultEntryValueTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: stavel
 * Date: 13.2.13
 * Time: 20:09
 */
public class BypassNormalizer implements ResultEntryValueTransformer {
    Logger logger = LogManager.getLogger(BypassNormalizer.class.getName());


    @Override
    public String transform(String value) {
        logger.debug("Entering BypassNormalizer with value: "+ value);
        if(value.equals("0")) return "no";
        else if(value.equals("1")) return "yes";

        return value;
    }
}
