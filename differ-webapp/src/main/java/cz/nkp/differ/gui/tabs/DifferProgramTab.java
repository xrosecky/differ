package cz.nkp.differ.gui.tabs;

import com.google.gwt.user.client.ui.TextBox;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.LoginForm.LoginEvent;
import com.vaadin.ui.LoginForm.LoginListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.compare.io.CompareComponent;
import cz.nkp.differ.exceptions.ImageDifferException;
import cz.nkp.differ.exceptions.UserDifferException;
import cz.nkp.differ.gui.components.DifferProgramTabButtonPanel;
import cz.nkp.differ.gui.components.LoginRegisterComponent;
import cz.nkp.differ.gui.components.PluginDisplayComponent;
import cz.nkp.differ.gui.components.UploadReceiver;
import cz.nkp.differ.gui.components.UserFilesWidget;
import cz.nkp.differ.gui.windows.MainDifferWindow;
import cz.nkp.differ.model.Image;
import cz.nkp.differ.model.User;
import cz.nkp.differ.util.GUIMacros;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.vaadin.easyuploads.MultiFileUpload;

/**
 * The main application view.
 * @author Joshua Mabrey
 * @author Vaclav Rosecky
 *
 * Mar 30, 2012
 */
@SuppressWarnings("serial")
public class DifferProgramTab extends HorizontalLayout {

    //private static final String GUEST_USERNAME = "guest";
    //private CustomComponent loginPanel;
    private Layout loggedInView, loggedOutView, customViewWrapper;
    private Button customLayoutBackButton;
    private Button compareButton;
    private UserFilesWidget fileSelector1, fileSelector2;
    private ArrayList<Upload> upload;
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
        if (loggedOutView == null) {
            final DifferProgramTab parent = this;
            loggedOutView = new HorizontalLayout();
            upload = new ArrayList<Upload>();
            ((HorizontalLayout) loggedOutView).setSpacing(true);

            AbsoluteLayout loggedOutViewInner = new AbsoluteLayout();
            loggedOutViewInner.setWidth("400px");
            loggedOutViewInner.setHeight("400px");

            compareButton = new Button("Compare");
            compareButton.setEnabled(false);
            compareButton.addListener(new ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    try {
                        Image[] selectedImages = new Image[upload.size()];
                        for (int i = 0; i < upload.size(); i++) {
                            File file = ((UploadReceiver) upload.get(i).getReceiver()).getFile();
                            if (file == null) {
                                break;
                            }
                            selectedImages[i] = new Image();
                            selectedImages[i].setFile(file);
                            selectedImages[i].setFileName(file.getName());
                            selectedImages[i].setUniqueName(file.getName());
                            selectedImages[i].setId(i);
                            selectedImages[i].setShared(false);
                            selectedImages[i].setOwnerId(-1);
                            selectedImages[i].setSize((int)file.length());
                        }
                        HorizontalLayout layout = new HorizontalLayout();
                        CompareComponent cp = new CompareComponent();
                        cp.setApplication(DifferApplication.getCurrentApplication());
                        layout.addComponent(new PluginDisplayComponent(cp, selectedImages));
                        parent.setCustomView(layout);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            
            Button resetButton = new Button("Reset");
            resetButton.addListener(new ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    this_internal.loggedOutView = null;
                    this_internal.setLoggedOutView();
                }
            });
            
            loggedOutViewInner.addComponent(addFileUploadComponent(), "left: 10px; top: 10px;");
            loggedOutViewInner.addComponent(addFileUploadComponent(), "left: 10px; top: 200px;");
            
            loggedOutView.addComponent(loggedOutViewInner);
            loggedOutView.addComponent(compareButton);
            loggedOutView.addComponent(resetButton);
            
            //Label lbl = new Label("You are currently using DIFFER anonymously." + 
            //"Anonymous users are restricted to uploads of 5MB in size.");
            //loggedOutView.addComponent(lbl);
        }
        this.removeAllComponents();
        this.addComponent(loggedOutView);
        this.setSizeUndefined();
        this.loggedInView = null;
    }
    

    
    private Component addFileUploadComponent() {       
        HorizontalLayout outer = new HorizontalLayout();
        outer.addStyleName("v-upload");
        outer.setSpacing(true);

        VerticalLayout inner = new VerticalLayout();
        inner.setSpacing(true);

        Embedded embedded = new Embedded();
        embedded.setHeight("100%");
        embedded.setWidth("100%");
        
        UploadReceiver receiver = new UploadReceiver(embedded, compareButton);
        Upload uploadInstance = new Upload("Select Local File", receiver);
        uploadInstance.setButtonCaption("Browse...");
        uploadInstance.addListener(receiver);
        uploadInstance.setImmediate(true);
        inner.addComponent(uploadInstance);
        
        upload.add(uploadInstance);

        Label lbl = new Label("OR");
        inner.addComponent(lbl);
        
        TextField urlPaste = new TextField("Select Remote File");
        urlPaste.setInputPrompt("Paste URL here...");
        inner.addComponent(urlPaste);
        
        outer.addComponent(inner);
        outer.addComponent(embedded);

        return outer;
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
            if (DifferApplication.getCurrentApplication().getLoggedUser() == null) {
                this_internal.loggedOutView = null;
                this_internal.setLoggedOutView();
            } else {
                this_internal.setLoggedInView();
            }
        }
    };
    
}
