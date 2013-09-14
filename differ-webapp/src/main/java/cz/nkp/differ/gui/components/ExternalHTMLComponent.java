package cz.nkp.differ.gui.components;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Window;
import cz.nkp.differ.DifferApplication;

/**
 *
 * @author Thomas Truax
 */
public class ExternalHTMLComponent {
    
    Window parent;
    String url;
    
    /**
     * Generates a layout component from an HTML page.
     * @param parent The parent window
     * @param url The URL of the HTML resource
     */
    public ExternalHTMLComponent(Window parent, String url) {
        this.parent = parent;
        this.url = url;
    }

    /**
     * Returns a component which contains the Embedded HTML resource.
     * @return layout component
     */
    public Component getComponent() {
        ExternalResource resource = new ExternalResource(url);
        final Embedded embedded = new Embedded("", resource);
        embedded.setType(Embedded.TYPE_BROWSER);
        embedded.setWidth(parent.getWidth() - 100 + "px");
        embedded.setHeight(parent.getWidth() - 100 + "px");
        
        parent.addListener(new Window.ResizeListener() {

            @Override
            public void windowResized(Window.ResizeEvent e) {
                embedded.setWidth(parent.getWidth() - 100 + "px");
                embedded.setHeight(parent.getHeight() - 100 + "px");
            }
        });
        return embedded;        
    }    
}
