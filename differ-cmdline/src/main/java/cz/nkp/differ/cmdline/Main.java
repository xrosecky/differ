package cz.nkp.differ.cmdline;

import com.beust.jcommander.JCommander;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import cz.nkp.differ.compare.io.ImageProcessor;
import cz.nkp.differ.compare.io.ImageProcessorResult;
import cz.nkp.differ.compare.io.SerializableImageProcessorResult;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.util.*;
import javax.xml.transform.stream.StreamResult;

/**
  mvn -fn -o -Dmaven.compiler.showWarnings=true -f pom.xml compile test-compile
*/
public class Main {
    private static CommandArgs commandArgs = new CommandArgs();

    /**
     * This function calls validator and comparator on two files.
     * And will report averything it did.
     */
    public static String processFiles(ApplicationContext context, File file[]) throws Exception {
    //      ApplicationContext context =
    //              new ClassPathXmlApplicationContext(new String[]{"appCtx-differ-cmdline.xml"});
    //      ImageProcessor processor = (ImageProcessor) context.getBean("imageProcessor");
    //      Jaxb2Marshaller marshaller = (Jaxb2Marshaller) context.getBean("jaxb2Marshaller");
    //      File file = new File(args[0]);
    //      ImageProcessorResult result = processor.processImage(file);
    //      SerializableImageProcessorResult resultForSerialization = SerializableImageProcessorResult.create(result, false);
    //      StreamResult streamResult = new StreamResult(System.out);
    //      marshaller.marshal(resultForSerialization, streamResult);

        String output = "";
        processFile(context,file[0]);
        processFile(context,file[1]);
        
        ImageProcessor processor = (ImageProcessor) context.getBean("imageProcessor");
        ImageProcessorResult [] results = processor.processImages(file[0], file[1]);
        TextCompareResultTransformer textCompareResultTransformer =
            new TextCompareResultTransformer( new TheSameNameOutputNamer(),
                                              commandArgs.saveOutputs,
                                              commandArgs.saveReport,
                                              commandArgs.saveProperties,
                                              false
                                              );
        output += textCompareResultTransformer.transform(file, results);
        return output;
    }

    /**
     * Iterates through directories.
     * It finds file pairs to compare.
     * @param files
     * @return
     */
    public static ArrayList<File[]> getPairsToCompare(File [] files){
        ArrayList<File[]> pairs = new ArrayList<File[]> ();
        HashMap<String,File[]> pairsByName = new HashMap<String,File[]>();
        for( File file: files[0].listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return !file.isDirectory();
            }
        })){
            String name = file.getName();
            String nameWithoutExtension = name.replaceFirst("[.][^.]+$", "");
            File [] pair = new File[2];
            pair[0] = file;
            pair[1] = null;
            pairsByName.put(nameWithoutExtension,pair);
        }
        for( File file: files[1].listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return !file.isDirectory();
            }
        })){
            String name = file.getName().replaceFirst("[.][^.]+$", "");
            if( pairsByName.containsKey(name)){
                pairsByName.get(name)[1] = file;
            };
        }
        // use just pairs that do have all elemenents filled.
        for(String name: pairsByName.keySet()){
            File[] pair = pairsByName.get(name);
            if( pair[0] != null && pair[1] != null){
                pairs.add(pair);
            }
        }
        Collections.sort(pairs, new Comparator<File[]>() {
            @Override
            public int compare(File[] files, File[] files2) {
                return files[0].toString().compareToIgnoreCase(files2[0].toString());
            }
        });
   	    return pairs;
	}
	    
    /**
     *
     * This function validates an image and writes all reports regarding to arguments.
     * @throws Exception
     */
    public static String processFile(ApplicationContext context, File file) throws Exception {
        ImageProcessor processor = (ImageProcessor) context.getBean("imageProcessor");
        Jaxb2Marshaller marshaller = (Jaxb2Marshaller) context.getBean("jaxb2Marshaller");

        ImageProcessorResult result = processor.processImage(file);
        TextResultTransformer textResultTransformer =
            new TextResultTransformer(  new TheSameNameOutputNamer(),
                                        commandArgs.saveOutputs,
                                        commandArgs.saveReport,
                                        commandArgs.saveProperties,
                                        false
                                        );
        String output = textResultTransformer.transform(file, result);
        if ( commandArgs.saveReport ) {
            FileWriter writer = new FileWriter(textResultTransformer.outputNamer.textName(file,result).toString());
            writer.write(output);
            writer.close();

            writer = new FileWriter(textResultTransformer.outputNamer.reportName(file,result));
            SerializableImageProcessorResult resultForSerialization = SerializableImageProcessorResult.create(result, false);
            StreamResult streamResult = new StreamResult(writer);
            marshaller.marshal(resultForSerialization, streamResult);
            writer.close();
        }
	    return output;
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"appCtx-differ-cmdline.xml"});
        JCommander jcommander = new JCommander(commandArgs,args);
        jcommander.setAcceptUnknownOptions(true);
        if( commandArgs.files.size() > 1){
            File files[] = new File[2];
            files[0] = new File(commandArgs.files.get(0));
            files[1] = new File(commandArgs.files.get(1));
            if( files[0].isDirectory() && files[1].isDirectory()){
                ArrayList<File[]> pairs = getPairsToCompare(files);
                for( File[] fpair: pairs){
                    System.out.println(String.format("processing: \t%s\n\t\t%s\n", fpair[0], fpair[1]));
                    processFiles(context, fpair);
                }
                System.out.println("Done\n");
            } else {
                System.out.println(processFiles(context, files));
            }
        } else {
            File file = new File(commandArgs.files.get(0));
            if( file.isDirectory()){

            } else {
                System.out.println(processFile(context,file));
            }
        }
    }
}
