package cz.nkp.differ.cmdline;

import com.beust.jcommander.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: stavel
 * Date: 1.1.13
 * Time: 15:37
 * To change this template use File | Settings | File Templates.
 */

public class CommandArgs {
    @Parameter(description = "Files to validate.")
    public List<String> files = new ArrayList<String>();

    @Parameter(names = {"-s","--save-report"}, description = "Report will be stored in a file named the same as input file. With extension: *.report")
    public boolean saveReport=false;

    @Parameter(names = {"-l","--load-report"}, description = "Report will loaded from a given file named the same as input file. With extension: *.report")
    public boolean loadReport=false;

    @Parameter(names = {"--save-raw-outputs"}, description = "Raw outputs of extractors will be saved too.")
    public boolean saveOutputs=false;
};
