package cz.nkp.differ.gui.tabs;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.gui.components.AnonymousUserView;
import cz.nkp.differ.gui.components.DifferProgramTabButtonPanel;
import cz.nkp.differ.gui.components.UserFilesWidget;
import cz.nkp.differ.gui.windows.MainDifferWindow;

import cz.nkp.differ.model.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * The main application view.
 * @author Joshua Mabrey
 * @author Vaclav Rosecky
 * @author Thomas Truax
 * 
 * Sept. 2013
 */
@SuppressWarnings("serial")
public class DifferProgramTab extends HorizontalLayout {

    private Layout loggedInView, loggedOutView, customViewWrapper;
    private Button customLayoutBackButton;
    private UserFilesWidget fileSelector1, fileSelector2;
    private final DifferProgramTab this_internal = this;
    private MainDifferWindow mainWindow = null;

    public DifferProgramTab(MainDifferWindow window) {
        this.mainWindow = window;
        if (DifferApplication.getCurrentApplication().getLoggedUser() == null) {
            setLoggedOutView();//Start the program logged out
        } else {
            setLoggedInView();
        }
    }

    public void setLoggedInView() {
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
        loggedOutView = new AnonymousUserView(this);
        removeAllComponents();
        addComponent(((AnonymousUserView)loggedOutView).getComponent());
        setSizeUndefined();
        loggedInView = null;
    }

    public boolean login(String username, String password) {
        try {
            User user = null;
            DifferApplication app = DifferApplication.getCurrentApplication();
            user = DifferApplication.getUserManager().attemptLogin(username, password);
            app.setLoggedUser(user);
            setLoggedInView();
        } catch (Exception ex) {
            DifferApplication.getCurrentApplication().getMainWindow().showNotification("Login Problem",
                    "<br/>" + ex.getMessage(), Window.Notification.TYPE_WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    public void setCustomView(Layout layout) {
        if (customViewWrapper == null) {
            customViewWrapper = new VerticalLayout();
            customLayoutBackButton = new Button("<b>↵</b> Back");
            customLayoutBackButton.setHtmlContentAllowed(true);
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

    public cz.nkp.differ.model.Image[] getSelectedImages() {
        Set<cz.nkp.differ.model.Image> images1 = fileSelector1.getSelectedImages();
        Set<cz.nkp.differ.model.Image> images2 = fileSelector2.getSelectedImages();
        if (images1 == null) {
            images1 = Collections.emptySet();
        }
        if (images2 == null) {
            images2 = Collections.emptySet();
        }
        cz.nkp.differ.model.Image[] result = new cz.nkp.differ.model.Image[images1.size() + images2.size()];
        int index = 0;
        for (cz.nkp.differ.model.Image image : images1) {
            result[index] = image;
            index++;
        }
        for (cz.nkp.differ.model.Image image : images2) {
            result[index] = image;
            index++;
        }
        return result;
    }
    private Button.ClickListener customViewWrapperBackButtonListener = new Button.ClickListener() {

        @Override
        public void buttonClick(ClickEvent event) {
            if (DifferApplication.getCurrentApplication().getLoggedUser() == null) {
                this_internal.loggedOutView = null;
                this_internal.setLoggedOutView();
            } else {
                this_internal.setLoggedInView();
            }
        }
    };
    
}
