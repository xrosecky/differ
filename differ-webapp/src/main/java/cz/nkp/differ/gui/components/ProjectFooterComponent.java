package cz.nkp.differ.gui.components;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import java.util.ArrayList;

/**
 * Generates the footer of the main application window, adding banners/logos/text as necessary.
 * @author Thomas Truax
 */
public class ProjectFooterComponent extends CustomComponent {
    
    public ProjectFooterComponent() {
        Panel footerPanel = new Panel();
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("100%");
        Layout footer = createFooterLayout();
        layout.addComponent(footer);
        layout.setComponentAlignment(footer, Alignment.MIDDLE_CENTER);
        footerPanel.setWidth("100%");
        footerPanel.addComponent(layout);
        this.setCompositionRoot(footerPanel);
    }
    
    private Layout createFooterLayout() {
        ArrayList<Embedded> embedded = new ArrayList<Embedded>();
        embedded.add(new Embedded(null, new ThemeResource("img/LOGO_Ministerstvo_Kultury.png")));
        embedded.add(new Embedded(null, new ThemeResource("img/LOGO_gsoc_2013.png")));
        embedded.add(new Embedded(null, new ThemeResource("img/LOGO_nkp_en.png")));
        GridLayout layout = new GridLayout(embedded.size(), 1);
        layout.addStyleName("v-footer-logos");
        for (Embedded img : embedded) {
            layout.addComponent(img);
        }
        return layout;
    }
}
