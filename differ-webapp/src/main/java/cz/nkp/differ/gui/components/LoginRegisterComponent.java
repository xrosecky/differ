package cz.nkp.differ.gui.components;

import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Layout;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.LoginForm.LoginListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import cz.nkp.differ.gui.windows.RegisterUserWindow;
import cz.nkp.differ.util.GUIMacros;

/**
 * Basic login form wrapper created so that adding customization to the form later would be much easier.
 * @author Joshua Mabrey
 * May 4, 2012
 */
@SuppressWarnings("serial")
public class LoginRegisterComponent extends CustomComponent {

    private Window mainWindow;

    public LoginRegisterComponent(Window window, LoginListener parent) {
        this.mainWindow = window;
        setCompositionRoot(createUserLoginForm(parent));
    }

    private Layout createUserLoginForm(final LoginListener parent) {
        AbsoluteLayout layout = new AbsoluteLayout();
        layout.setWidth("400px");
        layout.setHeight("200px");
        LoginForm loginForm = new LoginForm();
        loginForm.addListener(parent);
        layout.addComponent(loginForm, "left: 0px; top: 0px;");
        Button registerButton = new Button("Register");
        registerButton.addListener(GUIMacros.createWindowOpenButtonListener(mainWindow, new RegisterUserWindow()));
        layout.addComponent(registerButton, "left: 65px; top: 80px;");

        VerticalLayout wrapper = new VerticalLayout();

        wrapper.addComponent(layout);

        /*
        Button guestLogin = new Button("Guest Login");
        guestLogin.addListener(new Listener() {

        @Override
        public void componentEvent(Event event) {
        DifferProgramTab tab = (DifferProgramTab) parent;
        tab.guestLogin();
        }
        });

        wrapper.addComponent(guestLogin);
         */

        return wrapper;
    }
}
