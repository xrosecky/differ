package cz.nkp.differ.compare.metadata.external;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: stavel
 * Date: 15.1.13
 * Time: 7:07
 *
 * This class will call first transformer to get list of entries.
 * After this class gets the list, it calls all chained transformers to finalize list of entries.
 *
 * At the end it returns a list of entries.
 * */
public interface ChainingTransformer extends ResultTransformer {

    public ResultTransformer getPrimaryTransformer();

    public void setPrimaryTransformer (ResultTransformer primaryTransformer);

    public void setChainedTransformers(List<ResultTransformer> chainedTransformers);

    public List<ResultTransformer> getChainedTransformers();
}
