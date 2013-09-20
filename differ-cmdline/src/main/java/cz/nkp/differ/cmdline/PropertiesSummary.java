package cz.nkp.differ.cmdline;

import java.util.TreeSet;

/**
 * Created with IntelliJ IDEA.
 * User: stavel
 * Date: 15.1.13
 * Time: 18:24
 *
 * This class will collect all properties that occured in a validator output.
 */
public class PropertiesSummary {
    protected TreeSet<String> properties;

    public PropertiesSummary() {
        properties = new TreeSet<String>();
    }

    public void addProperty(String property){
        properties.add(property);
    }
    public TreeSet<String> getProperties(){
        return properties;
    }
}
