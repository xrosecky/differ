package cz.nkp.differ.compare.metadata.external.result;

import cz.nkp.differ.compare.metadata.external.ResultEntryReplacer;
import cz.nkp.differ.compare.metadata.external.ResultTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: stavel
 * Date: 14.2.13
 * Time: 10:38
 */
public class EntryRemover implements ResultEntryReplacer {
    @Override
    public List<ResultTransformer.Entry> replace(ResultTransformer.Entry entry) {
        return new ArrayList<ResultTransformer.Entry>();
    }
}
