package cz.nkp.differ.compare.metadata.external;

import cz.nkp.differ.compare.metadata.ImageMetadata;
import cz.nkp.differ.compare.metadata.MetadataExtractor;
import cz.nkp.differ.compare.metadata.MetadataSource;
import cz.nkp.differ.compare.metadata.external.ResultTransformer.Entry;
import cz.nkp.differ.plugins.tools.CommandRunner;
import cz.nkp.differ.plugins.tools.CommandRunner.CommandOutput;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author xrosecky
 */
public class ExternalMetadataExtractor implements MetadataExtractor {

    private List<String> programArguments;
    private ResultTransformer transformer;
    private String source;
    private Map<String, String> units;

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

    public Map<String, String> getUnits() {
        return units;
    }

    public void setUnits(Map<String, String> units) {
        this.units = units;
    }

    @Override
    public List<ImageMetadata> getMetadata(File file) {
        if (file == null) {
            throw new NullPointerException("file");
        }
        List<ImageMetadata> result = new ArrayList<ImageMetadata>();
        List<String> arguments = new ArrayList<String>();
        for (String argument : programArguments) {
            if (argument.equals("{file}")) {
                arguments.add(file.getAbsolutePath());
            } else {
                arguments.add(argument);
            }
        }
        try {
            CommandOutput cmdResult = CommandRunner.runCommandAndWaitForExit(null, arguments);
            MetadataSource metadataSource = new MetadataSource(cmdResult.getExitCode(), new String(cmdResult.getStdout()),
                    new String(cmdResult.getStderr()), source);
            int exitCode = cmdResult.getExitCode();
            String exitCodeString = "";
            switch (exitCode) {
                case 0:
                    exitCodeString = "ok";
                    break;
                case -1:
                    exitCodeString = "failed (-1)";
                    break;
                default:
                    exitCodeString = String.format("error (%s)", exitCode);
                    break;
            }
            result.add(new ImageMetadata("exit-code", exitCodeString, metadataSource));
            if (cmdResult.getExitCode() == 0) {
                List<Entry> entries = transformer.transform(cmdResult.getStdout(), cmdResult.getStderr());
                for (Entry entry : entries) {
                    ImageMetadata metadata = new ImageMetadata(entry.getKey(), entry.getValue(), metadataSource);
                    if (units != null) {
                        String unit = units.get(entry.getKey());
                        metadata.setUnit(unit);
                    }
                    result.add(metadata);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
