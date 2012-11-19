package cz.nkp.differ;

import cz.nkp.differ.plugins.tools.CommandRunner;
import cz.nkp.differ.plugins.tools.CommandRunner.CommandOutput;
import java.io.File;
import java.util.Arrays;
import org.junit.Test;

/**
 *
 * @author xrosecky
 */
public class CommandTest {

    @Test
    public void runCommand() throws Exception {
	CommandOutput output = CommandRunner.runCommandAndWaitForExit(new File("/home/xrosecky"), Arrays.asList("/bin/echo", "ahoj"));
	System.err.println(output.getExitCode());
	System.err.println(new String(output.getStdout()));
    }

}
