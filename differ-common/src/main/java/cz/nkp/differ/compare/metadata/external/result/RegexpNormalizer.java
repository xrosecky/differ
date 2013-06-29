package cz.nkp.differ.compare.metadata.external.result;

import cz.nkp.differ.compare.metadata.external.ResultEntryValueTransformer;
import cz.nkp.differ.compare.metadata.external.ResultTransformer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: stavel
 * Date: 12.2.13
 * Time: 10:38
 * To change this template use File | Settings | File Templates.
 */
public class RegexpNormalizer implements ResultEntryValueTransformer {
    private String regexp;
    private Pattern regexpPattern;

    public void setRegexp( String regexp ){
        this.regexp = regexp;
        this.regexpPattern = Pattern.compile(regexp);
    }
    @Override
    public String transform(String value) {
        Matcher matcher = this.regexpPattern.matcher(value);
        String result = null;
        if( matcher.find() ){
            result = matcher.group(1);
        }
        return result;
    }
}
