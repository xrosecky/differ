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
import cz.nkp.differ.exceptions.UserDifferException;
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
        
        layout.setSpacing(true);
        
	return layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {

	String nameValue = (String) nameField.getValue();
	String passValue = (String) passField.getValue();
        
        try {
            if (captcha.passedValidation()) { //if captcha succeeded
                
                if (nameValue.length() > 0 && passValue.length() > 0) { //if name & pass aren't null
                    
                    //FIXME: ensure no special characters are used in username
                    
                    if (UserManager.getInstance().getUserDAO().getUserByUserName(nameValue) == null) { // if username doesnt exist

                        User user = new User();
                        user.setUserName(nameValue);
                        UserManager.getInstance().registerUser(user, passValue);
                        DifferApplication.getCurrentApplication().getMainWindow().showNotification("Success", "<br/>You have successfully registered as " + nameField + ", you may now login");
                        Map<String, Object> CloseVariableMap = new HashMap<String,Object>(1);
                        CloseVariableMap.put("close", true);
                        internal_this.changeVariables(null, CloseVariableMap);
           
                    } else { //else username already exists
                        throw new Exception("The username you have chosen already exists");
                    }
                } else { //else user or pass was not entered
                    throw new Exception("The Username and Password fields cannot be empty");
                }
            } else { //else captcha was incorrect
                throw new Exception("You did not enter the correct captcha, please try again");
            }            
        } catch (Exception ex) {
            DifferApplication.getCurrentApplication().getMainWindow().showNotification("Error when registering user", "<br/>" + ex.getMessage(), Window.Notification.TYPE_ERROR_MESSAGE);
            captcha.reset();
        }

        //TODO:password strength checking etc
    }
}
