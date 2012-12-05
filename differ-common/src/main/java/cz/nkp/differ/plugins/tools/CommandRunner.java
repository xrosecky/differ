package cz.nkp.differ.plugins.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 *
 * @author xrosecky
 */
public class CommandRunner {

    public static CommandOutput runCommandAndWaitForExit(File workingDir, List<String> arguments) throws IOException, InterruptedException {
	ProcessBuilder pb = new ProcessBuilder(arguments);
	pb.directory(workingDir);
	Process process = pb.start();
	ByteArrayOutputStream stderr = new ByteArrayOutputStream();
	ByteArrayOutputStream stdout = new ByteArrayOutputStream();
	StreamGobbler sgerr = new StreamGobbler(process.getErrorStream(), stderr);
	StreamGobbler sgout = new StreamGobbler(process.getInputStream(), stdout);
	sgerr.start();
	sgout.start();
	int exitCode = process.waitFor();
	sgerr.join();
	sgout.join();
	CommandOutput result = new CommandOutput(stdout.toByteArray(), stderr.toByteArray());
	result.setExitCode(exitCode);
	return result;
    }

    public static class CommandOutput {
	private byte[] stdout;
	private byte[] stderr;
	private int exitCode = -1;

	public CommandOutput(byte[] stdout, byte[] stderr) {
	    this.stdout = stdout;
	    this.stderr = stderr;
	}

	public int getExitCode() {
	    return exitCode;
	}

	public void setExitCode(int exitCode) {
	    this.exitCode = exitCode;
	}

	public byte[] getStderr() {
	    return stderr;
	}

	public byte[] getStdout() {
	    return stdout;
	}
	
    }

    public static class StreamGobbler extends Thread {

	InputStream is;
	OutputStream os;

	StreamGobbler(InputStream is, OutputStream os) {
	    this.is = is;
	    this.os = os;
	}

	@Override
	public void run() {
	    try {
		int c;
		while ((c = is.read()) != -1) {
		    os.write(c);
		    os.flush();
		}
	    } catch (IOException x) {
		throw new RuntimeException(x);
	    }
	}
    }
}
