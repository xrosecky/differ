package cz.nkp.differ.gui.windows;


import com.vaadin.data.validator.NullValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.gui.components.CaptchaComponent;
import cz.nkp.differ.model.User;
import cz.nkp.differ.user.UserManager;
import cz.nkp.differ.util.GUIMacros;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class RegisterUserWindow extends Window implements ClickListener {

    RegisterUserWindow internal_this;
    
    public RegisterUserWindow() {
        internal_this = this;
	setCaption("Register User");
	setModal(true);
	setDraggable(false);
	setResizable(false);
	center();
	setWidth("25%");

	addComponent(createRegisterUserWindow());

	HorizontalLayout buttonLayout = new HorizontalLayout();

	Button register = new Button("Register");
	register.addListener(this);
	buttonLayout.addComponent(register);

	Button close = new Button("Close");
	close.addListener(GUIMacros.createWindowCloseButtonListener(this));
	buttonLayout.addComponent(close);

	addComponent(buttonLayout);
    }
    TextField nameField;
    PasswordField passField;
    CaptchaComponent captcha;

    /**
     *
     * @return
     */
    private Layout createRegisterUserWindow() {

	VerticalLayout layout = new VerticalLayout();

	nameField = new TextField("Username");
	nameField.addValidator(new NullValidator("You must provide a username!", false));
	layout.addComponent(nameField);

	passField = new PasswordField("Password");
	passField.addValidator(new NullValidator("You must provide a password!", false));
	layout.addComponent(passField);

	captcha = new CaptchaComponent();
	layout.addComponent(captcha);

	return layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {

	if (!captcha.passedValidation()) {
	    DifferApplication.getCurrentApplication().getMainWindow().showNotification("Captcha Problem", "<br/>You did not enter the correct captcha.", Window.Notification.TYPE_WARNING_MESSAGE);
	    return;
	}

	String nameValue = (String) nameField.getValue();
	String passValue = (String) passField.getValue();

	User user = new User();
	user.setUserName(nameValue);
	if (nameValue != null && passValue != null) {
	    try {
		user = UserManager.getInstance().registerUser(user, passValue);
                DifferApplication.getCurrentApplication().getMainWindow().showNotification("Success", "You have successfully registered as " + nameField);
                Map<String, Object> CloseVariableMap = new HashMap<String,Object>(1);
                CloseVariableMap.put("close", true);
                internal_this.changeVariables(null, CloseVariableMap); 
	    } catch (Exception ex) {
		DifferApplication.getCurrentApplication().getMainWindow().showNotification("Error when registering user", "<br/>Error when registering user.", Window.Notification.TYPE_ERROR_MESSAGE);
		captcha.reset();
	    }

	}//TODO:password strength checking etc
    }
}
