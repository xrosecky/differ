package cz.nkp.differ.compare.metadata.external.result;

import cz.nkp.differ.compare.metadata.external.ResultTransformer;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: stavel
 * Date: 3.3.13
 * Time: 3:04
 *
 * This class is used to transform metadata list. It is common to finnish transformations that needs
 * all results to perform transformation.
 */
public interface MetadataResultTransformer {
     public List<ResultTransformer.Entry> transform(List<ResultTransformer.Entry> metadataList);
}
