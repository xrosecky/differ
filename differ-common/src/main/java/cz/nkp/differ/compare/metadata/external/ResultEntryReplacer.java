package cz.nkp.differ.compare.metadata.external;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: stavel
 * Date: 10.2.13
 * Time: 22:32
 * This class is used to replace one ResultEntry with other entries.
 * It will take one entry and return some new entries to replace the old one.
 */
public interface ResultEntryReplacer {
    public List<ResultTransformer.Entry> replace(ResultTransformer.Entry entry);
}
