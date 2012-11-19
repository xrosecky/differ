package cz.nkp.differ.gui.components;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Select;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class XYResolutionField extends CustomComponent {

    public XYResolutionField(String name) {

	Xres = new Select();
	Xres.setNullSelectionAllowed(false);
	Xres.setNewItemsAllowed(false);
	Xres.setWidth(75, UNITS_PIXELS);

	Yres = new Select();
	Yres.setNullSelectionAllowed(false);
	Yres.setNewItemsAllowed(false);
	Yres.setWidth(75, UNITS_PIXELS);

	setCompositionRoot(createXYResolutionField(name));
    }

    public void setValues(Integer[] validXValues, Integer[] validYValues) {
	for (Integer i : validXValues) {
	    Xres.addItem(i);
	}

	for (Integer i : validYValues) {
	    Yres.addItem(i);
	}
    }

    public void setValues(Integer... values) {
	setValues(values, values);
    }

    private Layout createXYResolutionField(String name) {
	HorizontalLayout resolutionSelectorsLayout = new HorizontalLayout();
	resolutionSelectorsLayout.addComponent(Xres);
	resolutionSelectorsLayout.addComponent(new Label("  X  "));
	resolutionSelectorsLayout.addComponent(Yres);
	resolutionSelectorsLayout.setSpacing(false);

	VerticalLayout vertLayout = new VerticalLayout();
	vertLayout.addComponent(new Label(name));
	vertLayout.addComponent(resolutionSelectorsLayout);
	vertLayout.setSpacing(false);

	return vertLayout;
    }

    public void setDefaultXValue(int value) {
	Xres.setValue(value);
    }

    public void setDefaultYValue(int value) {
	Yres.setValue(value);
    }

    public String getResolution() {
	return Xres.getValue() + "x" + Yres.getValue();
    }

    public int[] getRange() {
	return new int [] {Integer.parseInt((String) Xres.getValue()), Integer.parseInt((String) Yres.getValue())};
    }
    private Select Xres, Yres;
}
