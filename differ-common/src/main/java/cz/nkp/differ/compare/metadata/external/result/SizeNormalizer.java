package cz.nkp.differ.compare.metadata.external.result;

import cz.nkp.differ.compare.metadata.external.ResultEntryValueTransformer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: stavel
 * Date: 28.2.13
 * Time: 16:08
 * IF size is larger than 1MB, use MB unit. and size in kb in brackets
 * If size is larger than 1kB use kB unit
 * other use it without any change.
 */
public class SizeNormalizer implements ResultEntryValueTransformer {
    Pattern megabytes = Pattern.compile("^([0-9]+[\\.0-9]*)[\\t ]*[Mm]");
    Pattern kilobytes = Pattern.compile("^([0-9]+[\\.0-9]*)[\\t ]*[kK]");
    Pattern bytes = Pattern.compile("^([0-9]+)$");

    @Override
    public String transform(String value) {
        Double size = null;
        Matcher matcher = megabytes.matcher(value);
        if( matcher.find() ){
            size = Double.parseDouble(matcher.group(1)) * 1e6;
        } else {
            matcher = kilobytes.matcher(value);
            if( matcher.find() ){
                size = Double.parseDouble(matcher.group(1)) * 1e3;
            } else {
                matcher = bytes.matcher(value);
                if( matcher.find() ){
                    size = Double.parseDouble(matcher.group(1));
                }
            }
        }
        if( size != null) {
            if( size > 1e6 ){
                return String.format("%.1f MB (%.1f kB)", size / 1e6, size / 1e3);
            }  else {
                if ( size > 1e4 ){
                    return String.format("%.1f kB", size/1e3);
                } else {
                    return String.format("%.1f B", size);
                }
            }
        }
        return value;
    }
}
