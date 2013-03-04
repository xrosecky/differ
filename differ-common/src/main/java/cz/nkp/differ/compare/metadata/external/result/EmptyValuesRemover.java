package cz.nkp.differ.compare.metadata.external.result;

import cz.nkp.differ.compare.metadata.external.ResultTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: stavel
 * Date: 1.3.13
 * Time: 19:40
 */
public class EmptyValuesRemover implements MetadataResultTransformer {
    public List<ResultTransformer.Entry> transform(List<ResultTransformer.Entry> metadataList){
        List<ResultTransformer.Entry> toRemove = new ArrayList<ResultTransformer.Entry>();
        for(ResultTransformer.Entry entry: metadataList){
            String value=entry.getValue();
            if( value == null || value.isEmpty() ){
                toRemove.add(entry);
            }
        }
        metadataList.removeAll(toRemove);
        return metadataList;
    }
}
