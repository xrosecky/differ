package cz.nkp.differ.gui.windows;

import java.io.IOException;

import com.vaadin.ui.Layout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import cz.nkp.differ.gui.components.ProjectHeaderPanel;
import cz.nkp.differ.gui.components.UserFilesWidget;
import cz.nkp.differ.gui.tabs.DifferProgramTab;
import cz.nkp.differ.gui.tabs.ResultManagerTab;
import cz.nkp.differ.gui.tabs.TabLoader;
import cz.nkp.differ.util.GeneralMacros;
import java.util.List;

/**
 * 
 * @author Joshua Mabrey
 * Mar 30, 2012
 */
@SuppressWarnings("serial")
public class MainDifferWindow extends Window {

    private List<UserFilesWidget> userFilesWidgets;
    private TabSheet menuTabs;
    
    public MainDifferWindow() {
	super("NDK Image Data Validator");//Sets the title of the application

	menuTabs = new TabSheet();

	/*
	 * Adding the dynamic content tabs
	 */
        DifferProgramTab loginContext = new DifferProgramTab(this);
	MainDifferWindow.createDynamicContentTab(loginContext, "DIFFER", menuTabs);
	MainDifferWindow.createDynamicContentTab(new ResultManagerTab(this), "Results", menuTabs);

	/*
	 * Adding the static content tabs
	 */
	MainDifferWindow.createStaticContentTab("about_tab", "About", menuTabs);
	MainDifferWindow.createStaticContentTab("doc_tab", "Documents", menuTabs);
	MainDifferWindow.createStaticContentTab("faq_tab", "FAQ", menuTabs);
	MainDifferWindow.createStaticContentTab("tos_tab", "TOS", menuTabs);
	MainDifferWindow.createStaticContentTab("help_tab", "Help", menuTabs);
        MainDifferWindow.createStaticContentTab("traffic_tab", "Traffic", menuTabs);

	/*
	 * Add the actual completed UI components to the root
	 */
	addComponent(new ProjectHeaderPanel(this, loginContext));//Component that represents the top-page header
	addComponent(menuTabs);//The application view tabs
    }

    /**
     * Loads a Static tab with {@link #cz.nkp.differ.gui.tabs.TabLoader} and adds it to the
     * given <code>TabSheet</code>
     * @param source String identifying the proper load resource as recognized by <code>TabLoader</code>
     * @param caption String that the tab should have as its name in the TabSheet
     * @param parent TabSheet to add the new tab to
     */
    private static void createStaticContentTab(String source, String caption, TabSheet parent) {

	GeneralMacros.errorIfContainsNull(source, caption, parent);//Check for null arguments

	try {
	    VerticalLayout tab = new TabLoader(source);
	    tab.setMargin(true);
	    tab.setCaption(caption);
	    parent.addTab(tab);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Loads a dynamic tab as a <code>Layout</code> which is then added to the given TabSheet
     * given <code>TabSheet</code>
     * @param source Layout to add to the tab, the Layout is responsible for implementing any interactions by itself
     * @param caption String that the tab should have as its name in the TabSheet
     * @param parent TabSheet to add the new tab to
     */
    private static void createDynamicContentTab(Layout source, String caption, TabSheet parent) {

	GeneralMacros.errorIfContainsNull(source, caption, parent);//Check for null arguments

	source.setCaption(caption);
	source.setMargin(true);
	parent.addTab(source);
    }

    public List<UserFilesWidget> getUserFilesWidgets() {
	return userFilesWidgets;
    }

    public void setUserFilesWidgets(List<UserFilesWidget> userFilesWidgets) {
	this.userFilesWidgets = userFilesWidgets;
    }
    
}
