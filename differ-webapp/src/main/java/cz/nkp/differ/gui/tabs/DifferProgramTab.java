package cz.nkp.differ.gui.tabs;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.LoginForm.LoginEvent;
import com.vaadin.ui.LoginForm.LoginListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.gui.components.DifferProgramTabButtonPanel;
import cz.nkp.differ.gui.components.LoginRegisterComponent;
import cz.nkp.differ.gui.components.UserFilesWidget;
import cz.nkp.differ.gui.windows.MainDifferWindow;
import cz.nkp.differ.model.Image;
import cz.nkp.differ.model.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * The main application view.
 * @author Joshua Mabrey
 * @author Vaclav Rosecky
 *
 * Mar 30, 2012
 */
@SuppressWarnings("serial")
public class DifferProgramTab extends HorizontalLayout {

    private static final String GUEST_USERNAME = "guest";
    private CustomComponent loginPanel;
    private Layout loggedInView, loggedOutView, customViewWrapper;
    private Button customLayoutBackButton;
    private UserFilesWidget fileSelector1, fileSelector2;
    private final DifferProgramTab this_internal = this;
    private MainDifferWindow mainWindow = null;
    //Used by button listener to reference DifferProgramTab object indirectly

    public DifferProgramTab(MainDifferWindow window) {
	this.mainWindow = window;
	if (DifferApplication.getCurrentApplication().getLoggedUser() == null) {
	    setLoggedOutView();//Start the program logged out
	} else {
	    setLoggedInView();
	}
    }

    private void setLoggedInView() {

	if (loggedInView == null) {
	    loggedInView = new HorizontalLayout();
	    fileSelector1 = new UserFilesWidget(false);
	    fileSelector2 = new UserFilesWidget(true);
	    List<UserFilesWidget> widgets = new ArrayList<UserFilesWidget>();
	    widgets.add(fileSelector1);
	    widgets.add(fileSelector2);
	    mainWindow.setUserFilesWidgets(widgets);
	    loggedInView.addComponent(fileSelector1);
	    loggedInView.addComponent(fileSelector2);
	    loggedInView.addComponent(new DifferProgramTabButtonPanel(this, mainWindow));
	    loggedInView.setSizeUndefined();
	}

	this.removeAllComponents();
	this.addComponent(loggedInView);
	this.setSizeUndefined();
    }

    public void setLoggedOutView() {

	if (loggedOutView == null) {
	    final DifferProgramTab parent = this;
	    loggedOutView = new VerticalLayout();
	    loginPanel = new LoginRegisterComponent(mainWindow, new LoginListener() {

		@Override
		public void onLogin(LoginEvent event) {
		    parent.login(event.getLoginParameter("username"), event.getLoginParameter("password"));
		}
	    });
	    Button guestLogin = new Button("Guest Login");
	    guestLogin.addListener(new Listener() {

		@Override
		public void componentEvent(Event event) {
		    parent.login(GUEST_USERNAME, null);
		}
	    });
	    loggedOutView.addComponent(loginPanel);
	    loggedOutView.addComponent(guestLogin);
	}

	this.removeAllComponents();
	this.addComponent(loggedOutView);
	this.setSizeUndefined();
	this.loggedInView = null;
    }

    private void login(String username, String password) {
	try {
	    User user = null;
	    DifferApplication app = DifferApplication.getCurrentApplication();
	    if (username.equals(GUEST_USERNAME)) {
		user = DifferApplication.getUserManager().getUserDAO().getUserByUserName("guest");
	    } else {
		user = DifferApplication.getUserManager().attemptLogin(username, password);
	    }
	    app.setLoggedUser(user);
	    //User user = UserManager.getInstance().attemptLogin(event.getLoginParameter("username"), event.getLoginParameter("password"));
	    setLoggedInView();
	} catch (Exception ex) {
	    DifferApplication.getCurrentApplication().getMainWindow().showNotification("Login Problem",
		    "<br/>" + ex.getMessage(), Window.Notification.TYPE_WARNING_MESSAGE);

	}
    }

    public void setCustomView(Layout layout) {
	if (customViewWrapper == null) {
	    customViewWrapper = new VerticalLayout();
	    customLayoutBackButton = new Button("Back");
	    customLayoutBackButton.addListener(customViewWrapperBackButtonListener);
	}

	customViewWrapper.removeAllComponents();
	customViewWrapper.addComponent(customLayoutBackButton);
	customViewWrapper.addComponent(layout);
	customViewWrapper.setSizeUndefined();

	this.removeAllComponents();
	this.addComponent(customViewWrapper);
	this.setSizeUndefined();
    }

    public Image[] getSelectedImages() {
	Set<Image> images1 = fileSelector1.getSelectedImages();
	Set<Image> images2 = fileSelector2.getSelectedImages();
	if (images1 == null) {
	    images1 = Collections.emptySet();
	}
	if (images2 == null) {
	    images2 = Collections.emptySet();
	}
	Image[] result = new Image[images1.size() + images2.size()];
	int index = 0;
	for (Image image : images1) {
	    result[index] = image;
	    index++;
	}
	for (Image image : images2) {
	    result[index] = image;
	    index++;
	}
	return result;
    }
    private Button.ClickListener customViewWrapperBackButtonListener = new Button.ClickListener() {

	@Override
	public void buttonClick(ClickEvent event) {
	    this_internal.removeAllComponents();
	    this_internal.setLoggedInView();
	}
    };
}
