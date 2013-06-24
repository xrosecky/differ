package cz.nkp.differ.cmdline;


import javax.xml.parsers.*;

import org.junit.Test;

public class JpylyzerTest {

	private String rawJpylyzerOutput1="/differ-cmdline/output-jpylyzer.xml";
	private String rawJpylyzerOutput2="";

	private String reportJpylyzer1;
	private String reportJpylyzer2;

	@Test
	public void test() {
		//fail("Not yet implemented");
	}
	
	public void setUp(){
		parseApplicationOutput(reportJpylyzer1);

		
		try {
			parseDocument(rawJpylyzerOutput1);
		}catch(Exception e){
			e.printStackTrace();
		}
			
		
	
	}
	
	private void parseApplicationOutput(String reportJpylyzer1) {
		// TODO Auto-generated method stub
		
	}

	private void parseDocument(String path) {
		
	}	
}
