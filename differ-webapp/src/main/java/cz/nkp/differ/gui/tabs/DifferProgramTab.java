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
    static private String BTN_TXT_COMPARE = "<div class=\"compare-button-caption\">Compare</div>";
    static private String BTN_TXT_PROCEED = "<div class=\"compare-button-caption\">Proceed</div>";
    private UserFilesWidget fileSelector1, fileSelector2;
    //private ArrayList<Upload> upload;
    private File uploadA;
    private File uploadB;
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
            uploadA = null;
            uploadB = null;
            
            loggedOutView = new HorizontalLayout();

            ((HorizontalLayout) loggedOutView).setSpacing(true);

            AbsoluteLayout innerUploadSection = new AbsoluteLayout();
            innerUploadSection.setWidth("450px");
            innerUploadSection.setHeight("460px");
            
            VerticalLayout innerCompareSection = new VerticalLayout();
            innerCompareSection.setHeight("100%");
            innerCompareSection.setWidth("45%");
            
            compareButton = new Button(BTN_TXT_COMPARE);
            compareButton.setHtmlContentAllowed(true);
            compareButton.addStyleName("v-bigbutton");
            compareButton.setEnabled(false);
            
            compareButton.addListener(new ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    try {
                        Image[] selectedImages = null;
                        if (uploadA == null || uploadB == null) {
                            selectedImages = new Image[1];
                        } else {
                            selectedImages = new Image[2];
                        }
                        
                        //uploadA
                        if (uploadA != null) {
                            selectedImages[0] = new Image();
                            selectedImages[0].setFile(uploadA);
                            selectedImages[0].setFileName(uploadA.getName());
                            selectedImages[0].setUniqueName(uploadA.getName());
                            selectedImages[0].setId(0);
                            selectedImages[0].setShared(false);
                            selectedImages[0].setOwnerId(-1);
                            selectedImages[0].setSize((int)uploadA.length());
                        }
                        
                        //uploadB
                        if (uploadB != null) {
                            selectedImages[1] = new Image();
                            selectedImages[1].setFile(uploadB);
                            selectedImages[1].setFileName(uploadB.getName());
                            selectedImages[1].setUniqueName(uploadB.getName());
                            selectedImages[1].setId(1);
                            selectedImages[1].setShared(false);
                            selectedImages[1].setOwnerId(-1);
                            selectedImages[1].setSize((int)uploadB.length());
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
            resetButton.addStyleName("v-longbutton");
            resetButton.addListener(new ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    this_internal.loggedOutView = null;
                    this_internal.setLoggedOutView();
                }
            });
            
            innerUploadSection.addComponent(addFileUploadComponent(0), "left: 10px; top: 10px;");
            innerUploadSection.addComponent(addFileUploadComponent(1), "left: 10px; top: 250px;");
            
            innerCompareSection.addComponent(compareButton);
            innerCompareSection.addComponent(resetButton);
            innerCompareSection.setComponentAlignment(compareButton, Alignment.BOTTOM_CENTER);
            innerCompareSection.setComponentAlignment(resetButton, Alignment.TOP_CENTER);
            
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
    
    private Component addFileUploadComponent(final int index) {       
        HorizontalLayout outer = new HorizontalLayout();
        outer.setWidth("400px");
        outer.setHeight("165px");
        outer.addStyleName("v-upload");
        outer.setSpacing(true);

        VerticalLayout inner = new VerticalLayout();
        inner.setSpacing(true);
        inner.setWidth("200px");
        
        final Embedded embedded = new Embedded();
        embedded.addStyleName("v-preview-anon");
        
        final TextField urlPaste = new TextField("Select Remote File");
        urlPaste.setWidth("100%");
        urlPaste.setInputPrompt("Paste URL here...");
        final UploadReceiver receiver = new UploadReceiver();
        final Upload uploadInstance = new Upload("Select Local File", receiver);
        uploadInstance.addStyleName("v-override");
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
                if (index == 0) {
                    uploadA = receiver.getFile();
                } else {
                    uploadB = receiver.getFile();
                }
                if (uploadA == null || uploadB == null) {
                    compareButton.setCaption(BTN_TXT_PROCEED);
                } else {
                    compareButton.setCaption(BTN_TXT_COMPARE);
                }
            }           
        });
        uploadInstance.setImmediate(true);
        inner.addComponent(uploadInstance);

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
                    if (index == 0) {
                        uploadA = file;
                    } else {
                        uploadB = file;
                    }
                    if (uploadA == null || uploadB == null) {
                        compareButton.setCaption(BTN_TXT_PROCEED);
                    } else {
                        compareButton.setCaption(BTN_TXT_COMPARE);
                    }
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
