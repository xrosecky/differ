package cz.nkp.differ.plugins.tools;

//import static net.sf.dynamicreports.report.builder.DynamicReports.*;

/**
 *  @author Jonatan Svensson
 *  v. 13/05/2013
 */
public class ReportGenerator{
	//TODO
	private void build(){
		
		// Design below
		
		//report()
		//	.columns(...)
			
	}
	
	/**
	 * Fetch image metadata from source 
	 * to be discussed how data should flow
	 */
	public void getResults(PureImageProcessorResult p){
		//Straight from cmd-line?
	}
	
	public void getResults(ResultManager r){
		//From .xml?
	}	
	
	public void toPDF(){
		// Trigger from gui
	}
	
	public void toXls(){
		// Trigger from gui
	}
	
	public Image getThumbnail(){
		// Extract thumbnail/adjusted-size image in some clever way to put in report
		return null;
	}
	
}