package cz.nkp.differ.cmdline;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import cz.nkp.differ.compare.io.ImageProcessor;
import cz.nkp.differ.compare.io.ImageProcessorResult;
import cz.nkp.differ.compare.io.SerializableImageProcessorResult;
import java.io.File;
import javax.xml.transform.stream.StreamResult;

public class Main {

    public static void main(String[] args) throws Exception {
	ApplicationContext context =
		new ClassPathXmlApplicationContext(new String[]{"appCtx-differ-cmdline.xml"});
	ImageProcessor processor = (ImageProcessor) context.getBean("imageProcessor");
	Jaxb2Marshaller marshaller = (Jaxb2Marshaller) context.getBean("jaxb2Marshaller");
	File file = new File(args[0]);
	ImageProcessorResult result = processor.processImage(file);
	SerializableImageProcessorResult resultForSerialization = SerializableImageProcessorResult.create(result, false);
	StreamResult streamResult = new StreamResult(System.out);
	marshaller.marshal(resultForSerialization, streamResult);
    }
}
