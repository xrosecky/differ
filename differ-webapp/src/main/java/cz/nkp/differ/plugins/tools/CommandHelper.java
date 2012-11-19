package cz.nkp.differ.plugins.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

public class CommandHelper extends Thread{

	public static final class CommandInfo{
		public String workingDir;
		public String[] commands;
	};
	
	public static abstract class CommandMessageCallback{
		public abstract void messageGenerated(String source, String message);
	};
	
	private static Logger LOGGER = Logger.getRootLogger();
	private CommandMessageCallback callback;
	
	private ProcessBuilder pb;
	private StreamGobbler stdGobbler = null ,errorGobbler = null;
	private boolean errorFlag = false;
	private String name = "CommandHelper";
	
	public CommandHelper(String name, CommandInfo info,CommandMessageCallback callback,Logger logger){
		if(info == null || callback == null || logger == null){
			throw new IllegalArgumentException("Cannot pass null parameters to CommandHelper");
		}
		
		LOGGER = logger;	
		this.callback = callback;
		
		if(name != null){
			this.name = name;
		}
		
		pb = new ProcessBuilder();
		pb.directory(new File(info.workingDir));
		pb.command(info.commands);
	}
	
	public void run(){
		try {
			Process proc = pb.start();
			
			stdGobbler = new StreamGobbler(proc.getInputStream(),LOGGER);
			stdGobbler.start();
			
			errorGobbler = new StreamGobbler(proc.getErrorStream(),LOGGER);
			errorGobbler.start();
			
			try {
				proc.waitFor();
			} catch (InterruptedException e) {
				LOGGER.error("["+ name +"]" + "Command process interrupted",e);
			}
			
			callback.messageGenerated(name,getMessage());
			
		} catch (IOException e) {
			LOGGER.error("["+ name +"]" + "Unable to run process",e);
			errorFlag = true;
			return;
		}
	}
	
	private String getMessage() throws IOException{
		
		if(errorFlag){
			throw new IOException("The command was invalid and no message could be generated");
		}
		
		if(stdGobbler == null || errorGobbler == null){
			throw new IOException("The command streams were not correctly created"); 
		}
		boolean errorStreamProcessed = false;
		while(true){//infinite loop is ok because this is called from IO processing thread.
			if(!errorStreamProcessed && errorGobbler.isReady()){
				String errorMsg = errorGobbler.getMessage();
				if(errorMsg != null && errorMsg.trim() != errorMsg){
					LOGGER.error("["+ name +"][stderr]" + errorMsg);
				}
				
				errorStreamProcessed = true;
			}
			
			if(stdGobbler.isReady()){
				return stdGobbler.getMessage();
			}
		}
		
	}
}

class StreamGobbler extends Thread
{
	private static Logger LOGGER = Logger.getRootLogger();
    InputStream is;
    private boolean isReady = false;
    private String msg = "";
    
    public StreamGobbler(InputStream is, Logger logger){
        this.is = is;
        LOGGER = logger;
    }
    
    public void run(){
    	
    	InputStreamReader stream = null;
    	BufferedReader br = null;
    	 
        try{
            stream = new InputStreamReader(is);
            br = new BufferedReader(stream);
            String line = null;
            while (true){
            	line = br.readLine();
            	if(line == null){
                    isReady = true;
            		break;
            	}else{
            		msg += line;
            	} 
            }  
        } catch (IOException e){
            LOGGER.error("Gobbler had an error!",e);
        } finally{
        	if(stream != null){
        		try {
					stream.close();
				} catch (IOException e) {
					LOGGER.warn("Unable to close stream reader",e);
				}
        	}
        	if(br != null){
        		try {
					br.close();
				} catch (IOException e) {
					LOGGER.warn("Unable to close stream reader",e);
				}
        	}
        }
    }
    
    public boolean isReady(){
    	return isReady;
    }
    
    public String getMessage(){
    	if(isReady()){
    		return msg;
    	}
    	else return null;
    }
}
