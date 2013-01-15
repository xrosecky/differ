package cz.nkp.differ.cmdline;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: stavel
 * Date: 15.1.13
 * Time: 18:24
 *
 * This class will collect all properties that occured in a validator output.
 */
public class PropertiesSummary {
    protected Set<String> properties;
    void addProperty(String property){
        this.properties.add(property);
    }
    Set<String> getProperties(){
        return this.properties;
    }
}
