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
	private Object value;

	public String getKey() {
	    return key;
	}

	public void setKey(String key) {
	    this.key = key;
	}

	public Object getValue() {
	    return value;
	}

	public void setValue(Object value) {
	    this.value = value;
	}

    }

    public List<Entry> transform(byte[] stdout, byte[] stderr) throws IOException;
}
