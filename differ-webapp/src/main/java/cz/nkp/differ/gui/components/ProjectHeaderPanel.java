package cz.nkp.differ.gui.components;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.gui.tabs.DifferProgramTab;
import cz.nkp.differ.gui.windows.LoginUserWindow;
import cz.nkp.differ.gui.windows.RegisterUserWindow;
import cz.nkp.differ.util.GUIMacros;

/**
 * Creates and populates a Component that has an image, page title, and copyright. Intended to be displayed
 * as the page header across the entire set of non-modal, non-dialog, non-tabular windows/views.
 * @author Joshua Mabrey
 * Mar 30, 2012
 */
@SuppressWarnings("serial")
public class ProjectHeaderPanel extends CustomComponent {

    Button buttonLogin;
    Button buttonLogout;
    Button buttonRegister;
    Label loginMessage;
    DifferProgramTab parent;
    Window application;
    /**
     * Creates the Component
     */
    public ProjectHeaderPanel() {
        Panel headerPanel = new Panel();//Create outer panel
        headerPanel.setWidth("100%");
        HorizontalLayout layout = new HorizontalLayout();//Create Layout to hold content components
        layout.setWidth("100%");
        layout.setHeight("100%");
        headerPanel.addComponent(layout);//Add content container to outer container
        Component title = createHeaderTitle();
        layout.addComponent(title);//add the created title component
        Component loginPanel = createLoginPanel();
        layout.addComponent(loginPanel);
        layout.setComponentAlignment(loginPanel, Alignment.BOTTOM_RIGHT);
        this.setCompositionRoot(headerPanel);
    }

    public ProjectHeaderPanel(Window application, DifferProgramTab parent) {
        this.parent = parent;
        this.application = application;
        Panel headerPanel = new Panel();//Create outer panel
        headerPanel.setWidth("100%");
        HorizontalLayout layout = new HorizontalLayout();//Create Layout to hold content components
        layout.setWidth("100%");
        layout.setHeight("100%");
        headerPanel.addComponent(layout);//Add content container to outer container
        Component title = createHeaderTitle();
        layout.addComponent(title);//add the created title component
        Component loginPanel = createLoginPanel();
        layout.addComponent(loginPanel);
        layout.setComponentAlignment(loginPanel, Alignment.BOTTOM_RIGHT);
        this.setCompositionRoot(headerPanel);
    }
    
    /**
     * A valid Header Title with copyright, page title and an anchor header image.
     * @return
     */
    private Layout createHeaderTitle() {
        Layout projectTitlePanel = new HorizontalLayout(); // Out Container for the Header Title layout
        projectTitlePanel.setHeight("100%");
        Embedded image = new Embedded(null, new ThemeResource("img/header_title_image.png")); // Load the logo from the package resources
        image.setSizeUndefined(); // Remove any size info from the image
        VerticalLayout titleInner = new VerticalLayout();
        Label titleMain = new Label("<h1><b>NDK Image Data Validator</b></h1>", Label.CONTENT_XHTML); // Page title is large directly next to image
        titleMain.setSizeUndefined();
        //Label titleCopyright = new Label("<h6>© National Library of the Czech Republic</h6>", Label.CONTENT_XHTML); // Copyright is next to image under title
        //titleCopyright.setSizeUndefined();
        titleInner.addComponent(titleMain);
        //titleInner.addComponent(titleCopyright);
        projectTitlePanel.addComponent(image);
        projectTitlePanel.addComponent(titleInner);
        return projectTitlePanel;
    }
    
    private Layout createLoginPanel() {
        VerticalLayout loginPanel = new VerticalLayout();
        HorizontalLayout buttonPanel = new HorizontalLayout();
        HorizontalLayout messagePanel = new HorizontalLayout();
        buttonLogin = new Button("Login");
        buttonLogin.addListener(GUIMacros.createWindowOpenButtonListener(application, new LoginUserWindow(parent, this)));
        buttonRegister = new Button("Register");
        buttonRegister.addListener(GUIMacros.createWindowOpenButtonListener(application, new RegisterUserWindow()));
        buttonLogout = new Button("Logout");
        buttonLogout.addListener(new Listener() {
            @Override
            public void componentEvent(Event event) {
                setLoggedOut();
            }
        });
        buttonPanel.addComponent(buttonLogin);
        buttonPanel.addComponent(buttonRegister);
        buttonPanel.addComponent(buttonLogout);
        loginMessage = new Label();
        messagePanel.addComponent(loginMessage);
        loginPanel.addComponent(buttonPanel);
        loginPanel.addComponent(messagePanel);
        loginPanel.setComponentAlignment(buttonPanel, Alignment.TOP_RIGHT);
        loginPanel.setComponentAlignment(messagePanel, Alignment.BOTTOM_RIGHT);
        setLoggedOut();
        return loginPanel;
    }
    
    public void setLoggedIn(String username) {
        buttonLogin.setVisible(false);
        buttonLogout.setVisible(true);
        buttonRegister.setVisible(false);
        if (username != null) {
            loginMessage.setCaption("You are currently logged in as " + username);
        }
        parent.setLoggedInView();
    }
    
    public void setLoggedOut() {
        DifferApplication.getCurrentApplication().setLoggedUser(null);
        buttonLogin.setVisible(true);
        buttonLogout.setVisible(false);
        buttonRegister.setVisible(true);
        loginMessage.setCaption("You are not logged in.");     
        parent.setLoggedOutView();
    }
}
