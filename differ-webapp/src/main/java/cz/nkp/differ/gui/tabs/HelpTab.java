package cz.nkp.differ.gui.tabs;

import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.ResizeListener;
import com.vaadin.ui.themes.Runo;
import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.gui.components.ExternalHTMLComponent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thomas Truax
 */
public class HelpTab extends HorizontalLayout {
    
    public HelpTab(Window parent) {
        Panel mainpanel = new Panel();
        mainpanel.addStyleName(Runo.PANEL_LIGHT);
        
        CustomLayout custom = new CustomLayout("help_tab");
        custom.setWidth("100%");

        /*
         * TODO: add http address of documentation in line below
         */
        ExternalHTMLComponent extres = new ExternalHTMLComponent(parent, "https://differ.readthedocs.org/en/latest");

        Component cmp = extres.getComponent();
        cmp.setStyleName("v-embedded-docs");
        custom.addComponent(cmp, "docs");

        mainpanel.setContent(custom);
        addComponent(mainpanel);
        
        setSizeFull();
    }

}
