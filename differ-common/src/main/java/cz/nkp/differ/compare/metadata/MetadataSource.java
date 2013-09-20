package cz.nkp.differ.compare.metadata;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author xrosecky
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"stdout", "stderr"})
public class MetadataSource {

    @XmlAttribute
    private int exitCode;
    @XmlAttribute
    private String version;
    @XmlElement
    private String stderr;
    @XmlElement
    private String stdout;
    @XmlAttribute(name = "source")
    @XmlID
    private String sourceName;
    
    public MetadataSource() {
    }

    public MetadataSource(int exitCode, String stdout, String stderr, String sourceName) {
        this(exitCode, stdout, stderr, sourceName, null);
    }

    public MetadataSource(int exitCode, String stdout, String stderr, String sourceName, String version) {
        this.exitCode = exitCode;
	this.stdout = stdout;
	this.stderr = stderr;
	this.sourceName = sourceName;
        this.version = version;
    }
    
    public String getSourceName() {
	return sourceName;
    }

    public void setSourceName(String sourceName) {
	this.sourceName = sourceName;
    }

    public String getStderr() {
	return stderr;
    }

    public void setStderr(String stderr) {
	this.stderr = stderr;
    }

    public String getStdout() {
	return stdout;
    }

    public void setStdout(String stdout) {
	this.stdout = stdout;
    }

    public int getExitCode() {
	return exitCode;
    }

    public void setExitCode(int exitCode) {
	this.exitCode = exitCode;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getVersion() {
        return version;
    }
    
    @Override
    public String toString() {
	return sourceName;
    }
}
