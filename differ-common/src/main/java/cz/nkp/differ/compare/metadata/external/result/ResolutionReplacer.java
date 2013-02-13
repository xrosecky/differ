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
 * Date: 11.2.13
 * Time: 16:11
 * To change this template use File | Settings | File Templates.
 */
public class ResolutionReplacer implements ResultEntryReplacer {
    @Override
    public List<ResultTransformer.Entry> replace(ResultTransformer.Entry entry) {
        String value=entry.getValue();
        List<ResultTransformer.Entry> result = new ArrayList<ResultTransformer.Entry>();

        Pattern regexp = Pattern.compile("([0-9]+)[ ]*x[ ]*([0-9]+)");
        Matcher matcher = regexp.matcher(value);
        if( matcher.find() ){
            ResultTransformer.Entry xEntry = new ResultTransformer.Entry();
            ResultTransformer.Entry yEntry = new ResultTransformer.Entry();

            xEntry.setKey("Resolution horizontal");
            xEntry.setValue(matcher.group(1));

            yEntry.setKey("Resolution vertical");
            yEntry.setValue(matcher.group(2));

            result.add(xEntry);
            result.add(yEntry);
        }
        return result;
    }
}
