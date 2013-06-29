package cz.nkp.differ.compare.metadata.external;

/**
 * Created with IntelliJ IDEA.
 * User: stavel
 * Date: 10.2.13
 * Time: 22:28
 * This class is used for post processing value formatting.
 */
public interface ResultEntryValueTransformer {
    public String transform(String value);
}
