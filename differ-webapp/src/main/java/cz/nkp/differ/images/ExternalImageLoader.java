package cz.nkp.differ.images;

import cz.nkp.differ.exceptions.ImageDifferException;
import cz.nkp.differ.plugins.tools.CommandRunner;
import cz.nkp.differ.plugins.tools.CommandRunner.CommandOutput;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author xrosecky
 */
public class ExternalImageLoader implements ImageLoader {

    private List<String> programArguments;
    private ImageLoader imageLoader;
    private String prefix;

    public List<String> getProgramArguments() {
	return programArguments;
    }

    public void setProgramArguments(List<String> programArguments) {
	this.programArguments = programArguments;
    }

    public ImageLoader getImageLoader() {
	return imageLoader;
    }

    public void setImageLoader(ImageLoader imageLoader) {
	this.imageLoader = imageLoader;
    }

    public String getPrefix() {
	return prefix;
    }

    public void setPrefix(String prefix) {
	this.prefix = prefix;
    }

    @Override
    public BufferedImage load(File inputFile) throws ImageDifferException {
	if (inputFile == null) {
	    throw new NullPointerException("inputFile");
	}
	List<String> arguments = new ArrayList<String>();
	File outputFile = null;
	try {
	    for (String argument : programArguments) {
		if (argument.equals("{output_file}")) {
		    outputFile = File.createTempFile("image", "." + prefix);
		    arguments.add(outputFile.getAbsolutePath());
		} else if (argument.equals("{input_file}")) {
		    arguments.add(inputFile.getAbsolutePath());
		} else {
		    arguments.add(argument);
		}
	    }
	    if (outputFile == null) {
		throw new ImageDifferException(ImageDifferException.ErrorCode.IMAGE_READ_ERROR,
			"Output file not specified in argument list!");
	    }
	    CommandOutput cmdResult = CommandRunner.runCommandAndWaitForExit(null, arguments);
	    if (cmdResult.getExitCode() != 0) {
		throw new ImageDifferException(ImageDifferException.ErrorCode.IMAGE_READ_ERROR,
			String.format("External process returned nonzero exit code: %s", cmdResult.getExitCode()));
	    }
	} catch (IOException ioe) {
	    throw new ImageDifferException(ImageDifferException.ErrorCode.IMAGE_READ_ERROR, "Can't read image", ioe);
	} catch (InterruptedException ie) {
	    throw new ImageDifferException(ImageDifferException.ErrorCode.IMAGE_READ_ERROR, "Image read interrupted", ie);
	}
	return imageLoader.load(outputFile);
    }
}
