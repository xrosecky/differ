package cz.nkp.differ.cmdline;

import com.beust.jcommander.JCommander;
import com.sun.jmx.snmp.defaults.DefaultPaths;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import cz.nkp.differ.compare.io.ImageProcessor;
import cz.nkp.differ.compare.io.ImageProcessorResult;
import cz.nkp.differ.compare.io.SerializableImageProcessorResult;
import java.io.File;
import java.io.FileWriter;
import javax.xml.transform.stream.StreamResult;


public class Main {
    public static void main(String[] args) throws Exception {
// =======
// 	ApplicationContext context =
// 		new ClassPathXmlApplicationContext(new String[]{"appCtx-differ-cmdline.xml"});
// 	ImageProcessor processor = (ImageProcessor) context.getBean("imageProcessor");
// 	Jaxb2Marshaller marshaller = (Jaxb2Marshaller) context.getBean("jaxb2Marshaller");
// 	File file = new File(args[0]);
// 	ImageProcessorResult result = processor.processImage(file);
// 	SerializableImageProcessorResult resultForSerialization = SerializableImageProcessorResult.create(result, false);
// 	StreamResult streamResult = new StreamResult(System.out);
// 	marshaller.marshal(resultForSerialization, streamResult);
// >>>>>>> master

        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"appCtx-differ-cmdline.xml"});
        ImageProcessor processor = (ImageProcessor) context.getBean("imageProcessor");
	Jaxb2Marshaller marshaller = (Jaxb2Marshaller) context.getBean("jaxb2Marshaller");
        CommandArgs commandArgs = new CommandArgs();
        new JCommander(commandArgs,args);
        if( commandArgs.files.size() > 1){
            File file0 = new File(commandArgs.files.get(0));
            File file1 = new File(commandArgs.files.get(1));
            ImageProcessorResult[] results = processor.processImages(file0, file1);
            TextCompareResultTransformer textCompareResultTransformer =
                    new TextCompareResultTransformer( new TheSameNameOutputNamer(),
                            commandArgs.saveOutputs,
                            commandArgs.saveReport,
                            false
                            );
            String output = textCompareResultTransformer.transform(results);
            System.out.println(output);
            if( commandArgs.saveReport ) {
                FileWriter writer = new FileWriter(textCompareResultTransformer.outputNamer.textCompareName(results[0],
                            results[1]));
                writer.write(output);
                writer.close();
            }
        } else {
            File file = new File(commandArgs.files.get(0));
            ImageProcessorResult result = processor.processImage(file);
            TextResultTransformer textResultTransformer =
                new TextResultTransformer(  new TheSameNameOutputNamer(),
                        commandArgs.saveOutputs,
                        commandArgs.saveReport,
                        false
                        );
            String output = textResultTransformer.transform(result);
            System.out.println(output);
            if ( commandArgs.saveReport ) {
                FileWriter writer = new FileWriter(textResultTransformer.outputNamer.textName(result));
                writer.write(output);
                writer.close();
                
                writer = new FileWriter(textResultTransformer.outputNamer.reportName(result));
                SerializableImageProcessorResult resultForSerialization = SerializableImageProcessorResult.create(result, false);
                StreamResult streamResult = new StreamResult(writer);
                marshaller.marshal(resultForSerialization, streamResult);
                writer.close();
            }
        }
    }
}
