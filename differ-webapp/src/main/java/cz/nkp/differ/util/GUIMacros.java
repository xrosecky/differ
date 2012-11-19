package cz.nkp.differ.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.gui.components.HelpTooltip;

/**
 * Contains GUI Macros to simplify both code readability and future code modifications. These methods are
 * common actions for GUI components.
 * @author Joshua Mabrey
 * Jun 12, 2012
 */ 
public class GUIMacros {
	
	static Logger LOGGER = Logger.getLogger(GUIMacros.class);
	
	public static final Label ErrorLabel = new Label("There has been an error.");
		
	/**
	 * Returns a Button.ClickListener that will open the Window passed to the method whenever the component
	 * the listener is attached to fires the ButtonClick event.
	 * @param target
	 * @return
	 */
	@SuppressWarnings("serial")
	public static final Button.ClickListener createWindowOpenButtonListener(final Window mainWindow, final Window target){
		return new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				GeneralMacros.errorIfContainsNull(target);
				//DifferApplication.getCurrentApplication().getMainWindow().addWindow(target);
				mainWindow.addWindow(target);
			}
		};
	}

	private static final Map<String, Object> CloseVariableMap = new HashMap<String,Object>(1);
	
	static
	{
		CloseVariableMap.put("close", true);
	}
	
	/**
	 * Returns a Button.ClickListener that will close the Window passed to the method whenever the component
	 * the listener is attached to fires the ButtonClick event. If target is null this method will fail with an error
	 * that will end the application session.
	 * @param target
	 * @return
	 */
	@SuppressWarnings("serial")
	public static final Button.ClickListener createWindowCloseButtonListener(final Window target){
		return new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				GeneralMacros.errorIfContainsNull(target);
				target.changeVariables(null, CloseVariableMap);//Workaround. May change in future releases of Vaadin.
				//Generally windows can't be closed by easy function call b/c the call is package local. However
				//this map insertion is the same way currently used by the window to close itself. 
			}
		};
	}
	
	/**
	 * Returns a HorizontalLayout with the tooltip positioned after the component.
	 * @param c
	 * @param h
	 * @return
	 */
	public static final HorizontalLayout bindTooltipToComponent(Component c, String title, String helpText){
		if(GeneralMacros.containsNull(c,title,helpText)){
			return null;
		}
		
		HorizontalLayout layout = new HorizontalLayout();
		layout.addComponent(c);
		layout.addComponent(new HelpTooltip(title,helpText));
		
		return layout;
	}
}
