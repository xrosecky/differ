package cz.nkp.differ.gui.windows;

import com.vaadin.data.validator.NullValidator;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.LoginForm.LoginEvent;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.gui.components.ProjectHeaderPanel;
import cz.nkp.differ.gui.tabs.DifferProgramTab;
import cz.nkp.differ.user.UserManager;
import cz.nkp.differ.util.GUIMacros;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Thomas Truax
 */
public class LoginUserWindow extends Window implements ClickListener {
    
    DifferProgramTab appBody;
    ProjectHeaderPanel appHeader;
    LoginUserWindow internal_this;
    TextField nameField;
    PasswordField passField;   
    
    public LoginUserWindow(DifferProgramTab appBody, ProjectHeaderPanel appHeader) {
        internal_this = this;
        setCaption("Login");
	setModal(true);
	setDraggable(false);
	setResizable(false);
	center();
	setWidth("25%");
        this.appBody = appBody;
        this.appHeader = appHeader;
        
        addComponent(createLoginUserWindow());
        
        HorizontalLayout buttonLayout = new HorizontalLayout();
        
        Button login = new Button("Login");
	login.addListener(this);
	buttonLayout.addComponent(login);

	Button close = new Button("Close");
	close.addListener(GUIMacros.createWindowCloseButtonListener(this));
	buttonLayout.addComponent(close);

	addComponent(buttonLayout);
    }
    
    private Layout createLoginUserWindow() {
        
        VerticalLayout layout = new VerticalLayout();

	nameField = new TextField("Username");
	nameField.addValidator(new NullValidator("You must provide a username!", false));
	layout.addComponent(nameField);

	passField = new PasswordField("Password");
	passField.addValidator(new NullValidator("You must provide a password!", false));
	layout.addComponent(passField);

	return layout;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        
	String nameValue = (String) nameField.getValue();
	String passValue = (String) passField.getValue();

        if (nameValue != null && passValue != null) {
            if (appBody.login(nameValue, passValue)) {
                appHeader.setLoggedIn(nameValue);
                DifferApplication.getCurrentApplication().getMainWindow().showNotification("Success", "<br/>You are now logged in as " + nameValue);
                Map<String, Object> CloseVariableMap = new HashMap<String,Object>(1);
                CloseVariableMap.put("close", true);
                internal_this.changeVariables(null, CloseVariableMap); 
            }               
        }
    }
}
