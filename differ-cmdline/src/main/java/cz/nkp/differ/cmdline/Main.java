package cz.nkp.differ.cmdline;

import com.beust.jcommander.JCommander;
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
        TextResultTransformer textResultTransformer = new TextResultTransformer(
                new TheSameNameOutputNamer(),
                commandArgs.saveOutputs,
                false
        );
        System.out.println(textResultTransformer.transform(result));
    }
}
