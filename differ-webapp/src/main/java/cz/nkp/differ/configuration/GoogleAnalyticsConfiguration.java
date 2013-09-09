package cz.nkp.differ.configuration;

/**
 *
 * @author xrosecky
 */
public class GoogleAnalyticsConfiguration {
    
    private String domainName;
    
    private String trackerId;

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getTrackerId() {
        return trackerId;
    }

    public void setTrackerId(String trackerId) {
        this.trackerId = trackerId;
    }
    
}
