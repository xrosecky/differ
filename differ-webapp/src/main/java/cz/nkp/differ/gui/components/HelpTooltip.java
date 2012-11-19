package cz.nkp.differ.gui.components;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;

/**
 * A component that places a help icon and displays a message when that icon is hovered over.
 * @author Joshua Mabrey
 * Mar 30, 2012
 */
@SuppressWarnings("serial")
public class HelpTooltip extends CustomComponent{
	
	/**
	 * Creates a <code>HelpTooltip</code> with the specified message as its help message
	 * @param message
	 */
	public HelpTooltip(String title,String message){
		setMessage(title,message);
	}
	
	/**
	 * Changes the message currently used to the message given
	 * @param message
	 */
	public void setMessage(String title, String message){
		this.setCompositionRoot(createHelpTooltip(title, message));
	}
		
	/**
	 * Creates and returns the component. 
	 * @param message
	 * @return
	 */
	private PopupView createHelpTooltip(final String title, final String message){
		PopupView.Content content = new PopupView.Content() {
			
			@Override
			public Component getPopupComponent() {
				Label label = new Label("<b> " + 
				title.replaceAll("[<>%()]", "").replaceAll("[&]", "&amp;") //No bad chars
				+ "</b></br>" + 
				message.replaceAll("[<>%()]", "").replaceAll("[&]", "&amp;"));  //No bad chars
				
				label.setContentMode(Label.CONTENT_XHTML);
				label.setSizeUndefined();
				return label;
			}
			
			@Override
			public String getMinimizedValueAsHTML() {
				//Help icon, or text backup if img is 404 etc
				return "<img src=\"/differ/VAADIN/themes/differ/img/help_tooltip_icon.png\" alt= \"[?]\"height=\"15\" width=\"15\"/>";
			}
		};	
		PopupView view = new PopupView(content);
		view.setSizeUndefined();
		return view;
	}
}
