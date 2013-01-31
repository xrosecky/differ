package cz.nkp.differ.cmdline;

import cz.nkp.differ.compare.io.ImageProcessor;
import cz.nkp.differ.compare.io.ImageProcessorResult;
import cz.nkp.differ.compare.io.SerializableImageProcessorResult;
import cz.nkp.differ.compare.io.SerializableImageProcessorResults;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.stream.StreamResult;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

public class Main {

    public static void main(String[] args) throws Exception {
	ApplicationContext context =
		new ClassPathXmlApplicationContext(new String[]{"appCtx-differ-cmdline.xml"});
	ImageProcessor processor = (ImageProcessor) context.getBean("imageProcessor");
	Jaxb2Marshaller marshaller = (Jaxb2Marshaller) context.getBean("jaxb2Marshaller");
	File file1 = new File(args[0]);
	File file2 = new File(args[1]);
	ImageProcessorResult[] results = processor.processImages(file1, file2);
        List<SerializableImageProcessorResult> resultsForSerialization = new ArrayList<SerializableImageProcessorResult>();
        for (ImageProcessorResult result : results) {
            resultsForSerialization.add(SerializableImageProcessorResult.create(result, false));
        }
        SerializableImageProcessorResults serializableResults = new SerializableImageProcessorResults(resultsForSerialization);
	StreamResult streamResult = new StreamResult(System.out);
	marshaller.marshal(serializableResults, streamResult);
    }
}
