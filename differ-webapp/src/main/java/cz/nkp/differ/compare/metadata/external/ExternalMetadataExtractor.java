package cz.nkp.differ.compare.metadata.external;

import cz.nkp.differ.compare.metadata.ImageMetadata;
import cz.nkp.differ.compare.metadata.MetadataExtractor;
import cz.nkp.differ.compare.metadata.MetadataSource;
import cz.nkp.differ.compare.metadata.external.ResultTransformer.Entry;
import cz.nkp.differ.model.Image;
import cz.nkp.differ.plugins.tools.CommandRunner;
import cz.nkp.differ.plugins.tools.CommandRunner.CommandOutput;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author xrosecky
 */
public class ExternalMetadataExtractor implements MetadataExtractor {

    private List<String> programArguments;
    private ResultTransformer transformer;
    private String source;

    public List<String> getProgramArguments() {
	return programArguments;
    }

    public void setProgramArguments(List<String> programArguments) {
	this.programArguments = programArguments;
    }

    public ResultTransformer getTransformer() {
	return transformer;
    }

    public void setTransformer(ResultTransformer transformer) {
	this.transformer = transformer;
    }

    public String getSource() {
	return source;
    }

    public void setSource(String source) {
	this.source = source;
    }

    @Override
    public List<ImageMetadata> getMetadata(Image image) {
	if (image == null) {
	    throw new NullPointerException("image");
	}
	List<ImageMetadata> result = new ArrayList<ImageMetadata>();
	List<String> arguments = new ArrayList<String>();
	for (String argument : programArguments) {
	    if (argument.equals("{file}")) {
		arguments.add(image.getFile().getAbsolutePath());
	    } else {
		arguments.add(argument);
	    }
	}
	try {
	    CommandOutput cmdResult = CommandRunner.runCommandAndWaitForExit(null, arguments);
	    MetadataSource metadataSource = new MetadataSource(cmdResult.getExitCode(), new String(cmdResult.getStdout()),
		    new String(cmdResult.getStderr()), source);
	    result.add(new ImageMetadata("status", cmdResult.getExitCode(), metadataSource));
	    if (cmdResult.getExitCode() == 0) {
		List<Entry> entries = transformer.transform(cmdResult.getStdout(), cmdResult.getStderr());
		for (Entry entry : entries) {
		    ImageMetadata metadata = new ImageMetadata(entry.getKey(), entry.getValue(), metadataSource);
		    result.add(metadata);
		}
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	return result;
    }
}
