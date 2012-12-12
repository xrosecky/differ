package cz.nkp.differ.cmdline;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import cz.nkp.differ.compare.io.ImageProcessor;
import cz.nkp.differ.compare.io.ImageProcessorResult;
import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
	ApplicationContext context =
		new ClassPathXmlApplicationContext(new String[]{"appCtx-differ-cmdline.xml"});
	ImageProcessor processor = (ImageProcessor) context.getBean("imageProcessor");
	File file = new File(args[0]);
	ImageProcessorResult result = processor.processImage(file);
	System.out.println(String.format("%s: %sx%s", file.getAbsolutePath(), result.getHeight(), result.getWidth()));
    }
}
