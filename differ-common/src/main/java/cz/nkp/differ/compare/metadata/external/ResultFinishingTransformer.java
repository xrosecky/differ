package cz.nkp.differ.compare.metadata.external;

import cz.nkp.differ.compare.metadata.external.result.MetadataResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: stavel
 * Date: 10.2.13
 * Time: 22:36
 * This class can be used to post process finishing changes in result metadata list.
 */

public class ResultFinishingTransformer implements MetadataResultTransformer {
    private HashMap<String,String> mapOfEntryNames;
    private HashMap<String,ResultEntryValueTransformer> entryTransformers;
    private HashMap<String,ResultEntryReplacer> entryReplacers;

    public List<ResultTransformer.Entry> transform(List<ResultTransformer.Entry> metadataList){
        List<ResultTransformer.Entry> toRemove = new ArrayList<ResultTransformer.Entry>();
        List<ResultTransformer.Entry> toAdd = new ArrayList<ResultTransformer.Entry>();

        for(ResultTransformer.Entry entry: metadataList){
                String key=entry.getKey();
                String newKey = mapOfEntryNames.get(key);
                if( newKey != null ){
                    key = newKey;
                    entry.setKey(newKey);
                }
                String value=entry.getValue();
                if( entryTransformers.containsKey(key) ){
                    String newValue = entryTransformers.get(key).transform(value);
                    if ( newValue != null ){
                        entry.setValue(newValue);
                    };
                }
                if( entryReplacers.containsKey(key)){
                    toAdd.addAll(entryReplacers.get(key).replace(entry));
                    toRemove.add(entry);
                }
        }
        if( ! toRemove.isEmpty() ){
            metadataList.removeAll(toRemove);
        }
        if( ! toAdd.isEmpty() ){
            metadataList.addAll(toAdd);
        }
        return metadataList;
    }
    public void setMapOfEntryNames( HashMap<String,String> mapOfEntryNames){
        this.mapOfEntryNames = mapOfEntryNames;
    }
    public void setEntryTransformers( HashMap<String,ResultEntryValueTransformer> entryTransformers) {
        this.entryTransformers = entryTransformers;
    }
    public void setEntryReplacers( HashMap<String, ResultEntryReplacer> entryReplacers){
        this.entryReplacers = entryReplacers;
    }
    public HashMap<String,String> getMapOfEntryNames(){
        return mapOfEntryNames;
    }
    public HashMap<String,ResultEntryValueTransformer> getEntryTransformers(){
        return entryTransformers;
    }
    public HashMap<String,ResultEntryReplacer> getEntryReplacers(){
        return entryReplacers;
    }
}
