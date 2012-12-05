package cz.nkp.differ.gui.windows;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Select;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import cz.nkp.differ.DifferApplication;

import cz.nkp.differ.io.ProfileManager;
import cz.nkp.differ.util.GUIMacros;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings("serial")
public class ProfileCreationWindow extends Window {

    private Map<String, Select> fields = new HashMap<String, Select>();
    private ProfileManager profileManager;
    private ComboBox profileName;

    public ProfileCreationWindow() {
        profileManager = (ProfileManager) DifferApplication.getApplicationContext().getBean("profileManager");
        setCaption("Create Profile");
        setModal(true);
        setDraggable(false);
        setResizable(false);
        center();
        setWidth(335, Window.UNITS_PIXELS);
        VerticalLayout windowLayout = new VerticalLayout();
        windowLayout.setSpacing(true);

        windowLayout.addComponent(createProfileCreationWindowForm());

        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button create = new Button("Create/Save");
        create.setImmediate(true);
        create.addListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                String name = (String) profileName.getValue();
                if (name != null) {
                    Map<String, String> profile = new HashMap<String, String>();
                    for (Entry<String, Select> entry : fields.entrySet()) {
                        String property = entry.getKey();
                        Select select = entry.getValue();
                        if (select.getValue() != null) {
                            profile.put(property, (String) select.getValue());
                        }
                    }
                    profileManager.saveProfile(name, profile);
                }
            }
        });
        buttonLayout.addComponent(create);

        Button delete = new Button("Delete");
        delete.setImmediate(true);
        delete.addListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if (profileName.getValue() != null) {
                    String profile = (String) profileName.getValue();
                    if (!profile.isEmpty()) {
                        profileManager.deleteProfile(profile);
                        refreshProfiles();
                    }
                }
            }
        });
        windowLayout.addComponent(delete);

        Button close = new Button("Close");
        buttonLayout.addComponent(close);
        close.addListener(GUIMacros.createWindowCloseButtonListener(this));
        windowLayout.addComponent(buttonLayout);
        addComponent(windowLayout);
    }

    private Layout createProfileCreationWindowForm() {
        VerticalLayout layout = new VerticalLayout();
        layout.setWidth(this.getWidth(), this.getWidthUnits());
        float childWidth = this.getWidth() * 0.8f;
        int childWidthUnits = this.getWidthUnits();
        profileName = new ComboBox("Profile name");
        for (String name : profileManager.getProfiles()) {
            profileName.addItem(name);
        }
        profileName.setNewItemsAllowed(true);
        profileName.setImmediate(true);
        profileName.setWidth(childWidth, childWidthUnits);
        layout.addComponent(profileName);
        profileName.addListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (event.getProperty().getValue() != null) {
                    String profileName = (String) event.getProperty().getValue();
                    System.out.println("profileName: " + profileName);
                    Map<String, String> profile = profileManager.getProfileByName(profileName);
                    if (profile != null && profileName != null) {
                        for (Entry<String, String> property : profile.entrySet()) {
                            String key = property.getKey();
                            String val = property.getValue();
                            Select select = fields.get(key);
                            if (select != null) {
                                System.out.println("Setting " + key + " to " + val);
                                select.setValue(val);
                            } else {
                                System.out.println("Not setting " + key + " to " + val);
                            }
                        }
                    }
                } else {
                    System.out.println("property is null!");
                }
            }
        });
        for (Entry<String, List<String>> entry : profileManager.getProfileTemplate().entrySet()) {
            String name = entry.getKey();
            List<String> values = entry.getValue();
            Select select = new Select(name);

            for (String value : values) {
                select.addItem(value);
            }
            select.setValue(values.get(0));
            select.setImmediate(true);
            fields.put(name, select);
            layout.addComponent(select);
        }
        return layout;
    }

    private void refreshProfiles() {
        profileName.removeAllItems();
        for (String name : profileManager.getProfiles()) {
            profileName.addItem(name);
        }
    }
}
