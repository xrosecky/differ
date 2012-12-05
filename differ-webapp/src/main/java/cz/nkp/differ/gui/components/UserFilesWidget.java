package cz.nkp.differ.gui.components;

import com.vaadin.data.Container;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;

import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.io.ImageManager;
import cz.nkp.differ.model.Image;
import cz.nkp.differ.model.User;
import java.util.Collections;
import java.util.Set;

public class UserFilesWidget extends CustomComponent {

    private static final long serialVersionUID = 4241885952194067796L;
    private static Logger LOGGER = Logger.getLogger(UserFilesWidget.class);
    private static int MB_UNIT = 1024 * 1024;
    private static List<UserFilesWidget> userFileWidgets = new ArrayList<UserFilesWidget>();
    private boolean isShort = false;
    private Table userFilesTable;
    private BeanItemContainer<Image> fileContainer = null;

    public UserFilesWidget(boolean isShort) {
	this.isShort = isShort;
	userFileWidgets.add(this);
	setCompositionRoot(createUserFilesWidget());
    }

    protected Layout createUserFilesWidget() {
	final User loggedUser = DifferApplication.getCurrentApplication().getLoggedUser();
	VerticalLayout layout = new VerticalLayout();
	DifferApplication app = (DifferApplication) DifferApplication.getCurrentApplication();
	List<Image> images = DifferApplication.getImageManager().getImages(app.getLoggedUser());
	fileContainer = new BeanItemContainer<Image>(Image.class, images);
	userFilesTable = new Table("files", fileContainer);
        userFilesTable.addGeneratedColumn("size", new ColumnGenerator() {

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                final Image image = (Image) itemId;
                return String.format("%.2f", ((float) image.getSize()) / MB_UNIT);
            }

        });
	if (isShort) {
	    userFilesTable.setVisibleColumns(new Object[]{"fileName", "size"});
	    userFilesTable.setColumnHeaders(new String[]{"file", "size (MB)"});
	} else {
	    userFilesTable.setVisibleColumns(new Object[]{"fileName", "size", "shared"});
	    userFilesTable.setColumnHeaders(new String[]{"file", "size (MB)", "shared"});
	}

	userFilesTable.setTableFieldFactory(new DefaultFieldFactory() {

	    @Override
	    public Field createField(Container container, Object itemId,
		    Object propertyId, Component uiContext) {
		Field field = super.createField(container, itemId, propertyId,
			uiContext);
		final Image image = (Image) itemId;
		boolean readOnly = (image.getOwnerId() != loggedUser.getId());
		if (propertyId.equals("shared")) {
		    CheckBox checkBox = new CheckBox();
		    checkBox.setReadOnly(readOnly);
		    checkBox.setDescription(null);
		    checkBox.setImmediate(true);
		    checkBox.addListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
			    DifferApplication.getImageManager().updateImage(image);
			}
		    });
		    return checkBox;
		}
		field.setHeight(1.5f, UNITS_EM);
		field.setReadOnly(true);
		return field;
	    }
	});
	userFilesTable.setSizeUndefined();
	userFilesTable.setWidth(450, Component.UNITS_PIXELS);
	userFilesTable.setColumnExpandRatio("filename", 1.0f);
	userFilesTable.setPageLength(10);
	userFilesTable.setSelectable(true);
	userFilesTable.setImmediate(true);
	userFilesTable.setNullSelectionAllowed(true);
	userFilesTable.setMultiSelect(true);
	userFilesTable.setEditable(true);
	layout.addComponent(userFilesTable);
	layout.setMargin(false, true, false, true);
	layout.setSizeUndefined();
	return layout;
    }

    public void refresh() {
	DifferApplication app = DifferApplication.getCurrentApplication();
	List<Image> images = DifferApplication.getImageManager().getImages(app.getLoggedUser());
	fileContainer.removeAllItems();
	fileContainer.addAll(images);
	userFilesTable.setValue(Collections.emptySet());
    }

    public int removeSelectedImages() {
	int count = 0;
	DifferApplication app = DifferApplication.getCurrentApplication();
	ImageManager imageManager = DifferApplication.getImageManager();
	Set<Image> selectedImages = (Set<Image>) userFilesTable.getValue();
	for (Image image : selectedImages) {
	    if (imageManager.deleteImage(app.getLoggedUser(), image)) {
		count++;
	    }
	}
	return count;
    }

    public Set<Image> getSelectedImages() {
	return (Set<Image>) userFilesTable.getValue();
    }

    public void addSelectionListener(ValueChangeListener listener) {
	userFilesTable.addListener(listener);
    }
}
