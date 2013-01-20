package cz.nkp.differ.compare.metadata.external;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author xrosecky
 */
public interface ResultTransformer {

    public static class Entry {

	private String key;
	private String source;
	private String value;

	public String getKey() {
	    return key;
	}

	public void setKey(String key) {
	    this.key = key;
	}

	public String getValue() {
	    return value;
	}

	public void setValue(String value) {
	    this.value = value;
	}

	public String getSource() {
	    return source;
	}

	public void setSource(String source) {
	    this.source = source;
	}

    }

    public List<Entry> transform(byte[] stdout, byte[] stderr) throws IOException;
}
