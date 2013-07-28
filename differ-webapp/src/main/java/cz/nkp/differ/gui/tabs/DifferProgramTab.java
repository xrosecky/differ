package cz.nkp.differ.gui.tabs;

import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.terminal.FileResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.compare.io.CompareComponent;
import cz.nkp.differ.gui.components.DifferProgramTabButtonPanel;
import cz.nkp.differ.gui.components.PluginDisplayComponent;
import cz.nkp.differ.gui.components.RemoteFile;
import cz.nkp.differ.gui.components.UploadReceiver;
import cz.nkp.differ.gui.components.UserFilesWidget;
import cz.nkp.differ.gui.windows.MainDifferWindow;
import cz.nkp.differ.model.Image;
import cz.nkp.differ.model.User;
import java.io.File;
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

            AbsoluteLayout innerUploadSection = new AbsoluteLayout();
            innerUploadSection.setWidth("500px");
            innerUploadSection.setHeight("500px");
            
            VerticalLayout innerCompareSection = new VerticalLayout();
            innerCompareSection.setHeight("100%");
            
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
            
            innerUploadSection.addComponent(addFileUploadComponent(), "left: 10px; top: 10px;");
            innerUploadSection.addComponent(addFileUploadComponent(), "left: 10px; top: 250px;");
            
            innerCompareSection.addComponent(compareButton);
            innerCompareSection.addComponent(resetButton);
            innerCompareSection.setComponentAlignment(compareButton, Alignment.BOTTOM_LEFT);
            innerCompareSection.setComponentAlignment(resetButton, Alignment.TOP_LEFT);
            
            loggedOutView.addComponent(innerUploadSection);
            loggedOutView.addComponent(innerCompareSection);
            
            //Label lbl = new Label("You are currently using DIFFER anonymously. " + 
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

        final Embedded embedded = new Embedded();
        embedded.addStyleName("v-preview");
        
        final TextField urlPaste = new TextField("Select Remote File");
        urlPaste.setInputPrompt("Paste URL here...");
        final UploadReceiver receiver = new UploadReceiver();
        final Upload uploadInstance = new Upload("Select Local File", receiver);
        final Button uploadBtn = new Button("Upload");
        
        
        uploadInstance.setButtonCaption("Browse...");
        
        uploadInstance.addListener(new StartedListener() {
            @Override
            public void uploadStarted(Upload.StartedEvent event) {
                urlPaste.setEnabled(false);
                uploadBtn.setEnabled(false);
            }
        });
        uploadInstance.addListener(new FailedListener() {
            @Override
            public void uploadFailed(Upload.FailedEvent event) {
                urlPaste.setEnabled(true);
                uploadBtn.setEnabled(true);
            }
        });
        uploadInstance.addListener(new SucceededListener() {
            @Override
            public void uploadSucceeded(Upload.SucceededEvent event) {
                embedded.setVisible(true);
                embedded.setSource(new FileResource(receiver.getFile(), DifferApplication.getCurrentApplication()));
                compareButton.setEnabled(true);
            }           
        });
        uploadInstance.setImmediate(true);
        inner.addComponent(uploadInstance);
        
        upload.add(uploadInstance);

        final Label lbl = new Label("OR");
        lbl.addStyleName("v-labelspacer");
        inner.addComponent(lbl);

        urlPaste.addListener(new TextChangeListener() {
            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                if (event.getText().length() > 0) {
                    uploadInstance.setEnabled(false);
                } else {
                    uploadInstance.setEnabled(true);
                }
            }
        });
        urlPaste.setImmediate(true);
        //urlPaste.setInvalidAllowed(true);
        urlPaste.setTextChangeEventMode(TextChangeEventMode.EAGER);
        
        inner.addComponent(urlPaste);
        
        
        uploadBtn.addListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                embedded.setVisible(true);
                RemoteFile remoteFile = new RemoteFile((String) (urlPaste.getValue()));
                if (remoteFile.isValid()) {
                    File file = remoteFile.getFile();
                    embedded.setSource(new FileResource(file, DifferApplication.getCurrentApplication()));
                    compareButton.setEnabled(true);
                }
            }        
        });
        inner.addComponent(uploadBtn);
        
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
