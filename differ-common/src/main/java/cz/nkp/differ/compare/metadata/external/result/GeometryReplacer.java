package cz.nkp.differ.compare.metadata.external.result;

import cz.nkp.differ.compare.metadata.external.ResultEntryReplacer;
import cz.nkp.differ.compare.metadata.external.ResultTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: stavel
 * Date: 12.2.13
 * Time: 6:55
 * To change this template use File | Settings | File Templates.
 */
public class GeometryReplacer implements ResultEntryReplacer {
    @Override
    public List<ResultTransformer.Entry> replace(ResultTransformer.Entry entry) {
        List<ResultTransformer.Entry> result = new ArrayList<ResultTransformer.Entry>();
        String value=entry.getValue();

        Pattern regexp = Pattern.compile("([0-9]+)[ ]*x[ ]*([0-9]+)");
        Matcher matcher = regexp.matcher(value);
        if( matcher.find() ){
            ResultTransformer.Entry widthEntry = new ResultTransformer.Entry();
            ResultTransformer.Entry heightEntry = new ResultTransformer.Entry();

            widthEntry.setKey("Image width");
            widthEntry.setValue(matcher.group(1));

            heightEntry.setKey("Image height");
            heightEntry.setValue(matcher.group(2));

            result.add(widthEntry);
            result.add(heightEntry);
        }
        return result;
    }
}
