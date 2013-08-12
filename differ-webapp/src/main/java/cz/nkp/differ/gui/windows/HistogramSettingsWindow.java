package cz.nkp.differ.gui.windows;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import cz.nkp.differ.util.GUIMacros;
import java.text.DecimalFormat;

/**
 *
 * @author xrosecky
 */
public class HistogramSettingsWindow extends Window {

    private TextField[] xRange = null;
    private TextField[] yRange = null;
    private CheckBox checkBox = null;
    private Button okButton = null;

    public HistogramSettingsWindow(double[] settings, boolean isLogarithmic) {
	setCaption("Create Profile");
	setModal(true);
	setDraggable(false);
	setResizable(false);
	center();
	setWidth(335, Window.UNITS_PIXELS);
	VerticalLayout windowLayout = new VerticalLayout();
	windowLayout.setSpacing(true);
	windowLayout.addComponent(createHistogramSettingsWindow(settings));
	HorizontalLayout buttonLayout = new HorizontalLayout();
	okButton = new Button("OK");
	buttonLayout.addComponent(okButton);
	Button close = new Button("Cancel");
	buttonLayout.addComponent(close);
	close.addListener(GUIMacros.createWindowCloseButtonListener(this));
	windowLayout.addComponent(buttonLayout);
	addComponent(windowLayout);
    }

    public Layout createHistogramSettingsWindow(double[] settings) {
	VerticalLayout layout = new VerticalLayout();
	layout.setWidth(this.getWidth(), this.getWidthUnits());
	float childWidth = this.getWidth() * 0.8f;
	int childWidthUnits = this.getWidthUnits();
	layout.addComponent(new Label("X axis"));
	xRange = createRangeField(layout, new double[]{settings[0], settings[1]});
	layout.addComponent(new Label("Y axis"));
	yRange = createRangeField(layout, new double[]{settings[2], settings[3]});
	checkBox = new CheckBox();
	checkBox.setCaption("logarithmic");
	layout.addComponent(checkBox);
	return layout;
    }

    private TextField[] createRangeField(Layout mainLayout, double[] range) {
	TextField start = new TextField();
	DecimalFormat df = new DecimalFormat("#.###");
	start.setValue(df.format(range[0]));
	TextField end = new TextField();
	end.setValue(df.format(range[1]));
	HorizontalLayout layout = new HorizontalLayout();
	layout.addComponent(start);
	layout.addComponent(new Label("    -    "));
	layout.addComponent(end);
	mainLayout.addComponent(layout);
	return new TextField[]{start, end};
    }

    public void setOnSubmit(ClickListener listener) {
	okButton.addListener(listener);
    }

    public double[] getXRange() {
	return new double[]{Double.parseDouble((String) xRange[0].getValue()), Double.parseDouble((String) xRange[1].getValue())};
    }

    public double[] getYRange() {
	return new double[]{Double.parseDouble((String) yRange[0].getValue()), Double.parseDouble((String) yRange[1].getValue())};
    }

    public boolean isLogarithmic() {
	return checkBox.booleanValue();
    }
}
