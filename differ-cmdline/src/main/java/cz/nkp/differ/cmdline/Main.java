package cz.nkp.differ.cmdline;

import com.beust.jcommander.JCommander;
import cz.nkp.differ.compare.metadata.ImageMetadata;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import cz.nkp.differ.compare.io.ImageProcessor;
import cz.nkp.differ.compare.io.ImageProcessorResult;
import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"appCtx-differ-cmdline.xml"});
        ImageProcessor processor = (ImageProcessor) context.getBean("imageProcessor");
        CommandArgs commandArgs = new CommandArgs();
        new JCommander(commandArgs,args);
        File file = new File(commandArgs.files.get(0));
        ImageProcessorResult result = processor.processImage(file);
        ResultSaver saver = new TheSameNameResultSaver();
        saver.save(result);
        System.out.println(String.format("%s: %sx%s", file.getAbsolutePath(), result.getHeight(), result.getWidth()));
        for (ImageMetadata metadata: result.getMetadata()) {
            System.out.println(String.format("%s (%s):\t%s", metadata.getKey(),metadata.getSource(), metadata.getValue()));
        }
    }
}
