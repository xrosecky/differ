package cz.nkp.differ.compare.metadata.external;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: stavel
 * Date: 15.1.13
 * Time: 7:11
 *
 * This class is used to partial transform of a list of entries.
 *
 */

public interface InChainTransformer {
    public List<ResultTransformer.Entry> transform(List<ResultTransformer.Entry> entries);
}
