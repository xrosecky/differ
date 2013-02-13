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
 * Time: 7:04
 */
public class RegexpReplacer implements ResultEntryReplacer {
    private String regexp;
    private List<String> entryNames;
    private Pattern regexpPattern;

    public void setRegexp (String regexp){
        this.regexp = regexp;
        this.regexpPattern = Pattern.compile(regexp);
    }
    public String getRegexp(){
        return this.regexp;
    }
    public void setEntryNames (List<String> entryNames){
        this.entryNames = entryNames;
    }
    public List<String> getEntryNames (){
        return entryNames;
    }

    @Override
    public List<ResultTransformer.Entry> replace(ResultTransformer.Entry entry) {
        String value = entry.getValue();
        Matcher matcher = regexpPattern.matcher(value);
        ArrayList<ResultTransformer.Entry> result = new ArrayList<ResultTransformer.Entry>();
        if( matcher.find() ){
            for( int groupId = 1; groupId <= matcher.groupCount(); groupId++){
                ResultTransformer.Entry newEntry = new ResultTransformer.Entry();
                newEntry.setValue(matcher.group(groupId));
                newEntry.setKey(entryNames.get(groupId-1));
                result.add(newEntry);
            }
        }
        return result;
    }
}