package cz.nkp.differ.compare.metadata.external;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: stavel
 * Date: 10.2.13
 * Time: 20:25
 */
public class ImagemagickInChainTransformer {
    public void transform(Object result) {
        List<ResultTransformer.Entry> entryList = (List<ResultTransformer.Entry>) result;
        List<ResultTransformer.Entry> toRemove = new ArrayList<ResultTransformer.Entry>();
        List<ResultTransformer.Entry> toAdd = new ArrayList<ResultTransformer.Entry>();

        for( ResultTransformer.Entry entry: entryList){
            String key = entry.getKey();
            if( key.equals("Type of format")){
                String value = entry.getValue();
                entry.setValue( value.replaceFirst("JPEG.*","JPEG"));
            }
            if( key.equals("Image/Resolution")){
                String value=entry.getValue();
                Pattern regexp = Pattern.compile("([0-9]+)[ ]*x[ ]*([0-9]+)");
                Matcher matcher = regexp.matcher(value);
                if( matcher.find() ){
                    ResultTransformer.Entry xEntry = new ResultTransformer.Entry();
                    ResultTransformer.Entry yEntry = new ResultTransformer.Entry();

                    xEntry.setKey("Resolution horizontal");
                    xEntry.setValue(matcher.group(1));

                    yEntry.setKey("Resolution vertical");
                    yEntry.setValue(matcher.group(2));

                    toAdd.add(xEntry);
                    toAdd.add(yEntry);

                    toRemove.add(entry);
                }
            }
        }
        entryList.removeAll(toRemove);
        entryList.addAll(toAdd);
    }
}
