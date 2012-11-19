package cz.nkp.differ.compare.metadata;

/**
 *
 * @author xrosecky
 */
public class MetadataSource {

    private int exitCode;
    private String stderr;
    private String stdout;
    private String sourceName;

    public MetadataSource(int exitCode, String stdout, String stderr, String sourceName) {
	this.exitCode = exitCode;
	this.stdout = stdout;
	this.stderr = stderr;
	this.sourceName = sourceName;
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

    @Override
    public String toString() {
	return sourceName;
    }

}
