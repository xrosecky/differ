package cz.nkp.differ.gui.tabs;

import java.io.IOException;

import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;

import cz.nkp.differ.util.GeneralMacros;

/**
 * This is a static stub class that allows the rest of the application to easily access the differ custom
 * themes static html files in a location independent way. These html files hold
 * the html stubs that outline the <b>static</b> content of the applications static pages. Do note
 * that it is not possible to insert AJAX or other dynamic content into these files successfully
 * without modification. (Dynamic taken to mean any content the browser would have to break out of 
 * our domain root to access, as this is disallowed under most browser's security modules) 
 * 
 * TODO: Make it possible to override the default content with external non-jar content
 * @author Joshua Mabrey
 * Mar 30, 2012
 */
@SuppressWarnings("serial")
public class TabLoader extends VerticalLayout{
	
	/**
	 * Creates a TabLoader by accessing the files stored within the differ custom theme.
	 * @param resource
	 * @throws IOException if the file is not present or readable from the package.
	 */
	public TabLoader(String resource) throws IOException{
		super();//Create this as a VerticalLayout
		
		GeneralMacros.errorIfContainsNull(resource);
		
		/*
		 * Load the content of the static html into a custom layout which is placed in a panel and 
		 * set as this layouts component
		 */
		CustomLayout custom = new CustomLayout(resource);
		Panel tab = new Panel();
		tab.setContent(custom);
		tab.addStyleName(Runo.PANEL_LIGHT);//No borders or styling on the panel
		this.addComponent(tab);
	}
}
