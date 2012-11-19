package cz.nkp.differ.compare.metadata.external;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author xrosecky
 */
public class RegexpTransformer implements ResultTransformer {

    private List<String> listOfRegexp;
    private Map<String, String> mapOfRegexp;

    public List<String> getListOfRegexp() {
	return listOfRegexp;
    }

    public void setListOfRegexp(List<String> listOfRegexp) {
	this.listOfRegexp = listOfRegexp;
    }

    public Map<String, String> getMapOfRegexp() {
	return mapOfRegexp;
    }

    public void setMapOfRegexp(Map<String, String> mapOfRegexp) {
	this.mapOfRegexp = mapOfRegexp;
    }

    @Override
    public List<Entry> transform(byte[] stdout, byte[] stderr) throws IOException {
	List<Entry> result = new ArrayList<Entry>();
	BufferedReader bf = new BufferedReader(new StringReader(new String(stdout)));
	String str;
	while ((str = bf.readLine()) != null) {
	    boolean skip = false;
	    if (mapOfRegexp != null) {
		for (Map.Entry<String, String> entry : mapOfRegexp.entrySet()) {
		    Pattern pattern = Pattern.compile(entry.getValue());
		    Matcher matcher = pattern.matcher(str);
		    boolean matchFound = matcher.find();
		    if (matchFound && matcher.groupCount() == 1) {
			String val = matcher.group(1).trim();
			if (!val.isEmpty()) {
			    Entry metadataEntry = new Entry();
			    metadataEntry.setKey(entry.getKey());
			    metadataEntry.setValue(val);
			    result.add(metadataEntry);
			}
			skip = true;
		    }
		}
	    }
	    if (skip) {
		continue;
	    }
	    for (String regexp : listOfRegexp) {
		Pattern pattern = Pattern.compile(regexp);
		Matcher matcher = pattern.matcher(str);
		boolean matchFound = matcher.find();
		if (matchFound && matcher.groupCount() == 2) {
		    String key = matcher.group(1).trim();
		    String val = matcher.group(2).trim();
		    if (!(key.isEmpty() || val.isEmpty())) {
			Entry entry = new Entry();
			entry.setKey(key);
			entry.setValue(matcher.group(2).trim());
			result.add(entry);
		    }
		}

	    }
	}
	return result;
    }
}
