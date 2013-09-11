package cz.nkp.differ.gui.components;

import com.vaadin.event.FieldEvents;
import com.vaadin.event.MouseEvents;
import com.vaadin.terminal.FileResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.compare.io.CompareComponent;
import cz.nkp.differ.gui.tabs.DifferProgramTab;
import cz.nkp.differ.gui.windows.FullSizeImageWindow;
import java.io.File;

/**
 *
 * @author Thomas Truax
 */
public class AnonymousUserView extends HorizontalLayout {
    
    private DifferProgramTab parent;
    private AnonymousUserView internal_this = this;
    private Button compareButton;
    private File uploadA;
    private File uploadB;
    
    static private long MAX_FILE_SIZE = 5242880; //size in bytes (5MB)
    static private String BTN_TXT_COMPARE = "<div class=\"compare-button-caption\">Compare</div>";
    static private String BTN_TXT_PROCEED = "<div class=\"compare-button-caption\">Proceed</div>";
    
    public AnonymousUserView(DifferProgramTab parent) {
        this.parent = parent;
    }
   
    public HorizontalLayout getComponent() {
        uploadA = null;
        uploadB = null;

        setSpacing(true);

        AbsoluteLayout innerUploadSection = new AbsoluteLayout();
        innerUploadSection.setWidth("450px");
        innerUploadSection.setHeight("615px");

        VerticalLayout innerCompareSection = new VerticalLayout();
        innerCompareSection.setHeight("100%");
        innerCompareSection.setWidth("45%");

        compareButton = new Button(BTN_TXT_COMPARE);
        compareButton.setHtmlContentAllowed(true);
        compareButton.addStyleName("v-bigbutton");
        compareButton.setEnabled(false);

        compareButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    cz.nkp.differ.model.Image[] selectedImages = null;
                    if (uploadA == null || uploadB == null) {
                        selectedImages = new cz.nkp.differ.model.Image[1];
                    } else {
                        selectedImages = new cz.nkp.differ.model.Image[2];
                    }
                    int indx = 0;

                    //uploadA
                    if (uploadA != null) {
                        selectedImages[indx] = new cz.nkp.differ.model.Image();
                        selectedImages[indx].setFile(uploadA);
                        selectedImages[indx].setFileName(uploadA.getName());
                        selectedImages[indx].setUniqueName(uploadA.getName());
                        selectedImages[indx].setId(indx);
                        selectedImages[indx].setShared(false);
                        selectedImages[indx].setOwnerId(-1);
                        selectedImages[indx].setSize((int)uploadA.length());
                        indx++;
                    }

                    //uploadB
                    if (uploadB != null) {
                        selectedImages[indx] = new cz.nkp.differ.model.Image();
                        selectedImages[indx].setFile(uploadB);
                        selectedImages[indx].setFileName(uploadB.getName());
                        selectedImages[indx].setUniqueName(uploadB.getName());
                        selectedImages[indx].setId(indx);
                        selectedImages[indx].setShared(false);
                        selectedImages[indx].setOwnerId(-1);
                        selectedImages[indx].setSize((int)uploadB.length());
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
        resetButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                internal_this.removeAllComponents();
                internal_this.getComponent();
            }
        });

        innerUploadSection.addComponent(addFileUploadComponent(0), "left: 10px; top: 10px;");
        innerUploadSection.addComponent(addFileUploadComponent(1), "left: 10px; top: 250px;");

        Label lbl = new Label("<b>Supported file types:</b> JPEG/JFIF (jpe, jpg, jpeg), JPEG2000 (jp2, jpf, jpx)," +
            "TIFF (tif, tiff), DjVu, sDjVu (djv, djvu), PNG, (png), PDF (pdf), FITS (fits, fit, fts)<br><br>" +
            "<b>File size limits:</b> Anonymous users - 5MB, Registered users - 15MB.<br>" +
            "Registered users may store up to 100MB of images.<br><br>" +
            "<i>You are currently using DIFFER anonymously.</i>",
            Label.CONTENT_XHTML);

        innerUploadSection.addComponent(lbl, "left: 10px; top: 470px;");
        innerCompareSection.addComponent(compareButton);
        innerCompareSection.addComponent(resetButton);
        innerCompareSection.setComponentAlignment(compareButton, Alignment.BOTTOM_CENTER);
        innerCompareSection.setComponentAlignment(resetButton, Alignment.TOP_CENTER);

        addComponent(innerUploadSection);
        addComponent(innerCompareSection);

        return this;
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
            inner.addStyleName("v-preview-anon");

            final Embedded embedded = new Embedded();
            embedded.addStyleName("v-preview-anon");
            embedded.addStyleName("v-showhand");

            final TextField urlPaste = new TextField("Select Remote File");
            urlPaste.setWidth("100%");
            urlPaste.setInputPrompt("Paste URL here...");
            final UploadReceiver receiver = new UploadReceiver();
            final Upload uploadInstance = new Upload("Select Local File", receiver);
            uploadInstance.addStyleName("v-override");
            final Button uploadBtn = new Button("Upload");

            uploadInstance.setButtonCaption("Browse...");

            uploadInstance.addListener(new Upload.StartedListener() {
                @Override
                public void uploadStarted(Upload.StartedEvent event) {
                    try {
                        if (uploadInstance.getUploadSize() > MAX_FILE_SIZE) {
                            uploadInstance.interruptUpload();
                                        DifferApplication.getCurrentApplication().getMainWindow().showNotification("File failed to upload",
                            "<br/>" + "File must not exceed 5MB for anonymous users", Window.Notification.TYPE_WARNING_MESSAGE);
                        }
                    } catch (Exception ex) {

                    }
                    urlPaste.setEnabled(false);
                    uploadBtn.setEnabled(false);
                }
            });
            uploadInstance.addListener(new Upload.FailedListener() {
                @Override
                public void uploadFailed(Upload.FailedEvent event) {
                    urlPaste.setEnabled(true);
                    uploadBtn.setEnabled(true);
                }
            });
            uploadInstance.addListener(new Upload.SucceededListener() {
                @Override
                public void uploadSucceeded(Upload.SucceededEvent event) {
                    embedded.setVisible(true);
                    embedded.setSource(new FileResource(receiver.getFile(), DifferApplication.getCurrentApplication()));              
                    embedded.addListener(new MouseEvents.ClickListener() {
                        @Override
                        public void click(MouseEvents.ClickEvent event) {
                            Embedded fullview = new Embedded();
                            fullview.setSource(new FileResource(receiver.getFile(), DifferApplication.getCurrentApplication()));
                            DifferApplication.getMainApplicationWindow().addWindow(new FullSizeImageWindow(fullview));
                        }
                    });
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

            urlPaste.addListener(new FieldEvents.TextChangeListener() {
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
            urlPaste.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);

            inner.addComponent(urlPaste);

            uploadBtn.addListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    RemoteFile remoteFile = new RemoteFile((String) (urlPaste.getValue()));
                    if (remoteFile.isValid()) {
                        final File file = remoteFile.getFile();
                        embedded.setVisible(true);
                        embedded.setSource(new FileResource(file, DifferApplication.getCurrentApplication()));              
                        embedded.addListener(new MouseEvents.ClickListener() {
                            @Override
                            public void click(MouseEvents.ClickEvent event) {
                                Embedded fullview = new Embedded();
                                fullview.setSource(new FileResource(receiver.getFile(), DifferApplication.getCurrentApplication()));
                                DifferApplication.getMainApplicationWindow().addWindow(new FullSizeImageWindow(fullview));
                            }
                        });                    
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
}
