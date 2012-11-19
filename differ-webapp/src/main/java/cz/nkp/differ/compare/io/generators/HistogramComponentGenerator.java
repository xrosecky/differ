package cz.nkp.differ.compare.io.generators;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.vaadin.addon.JFreeChartWrapper;

import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import cz.nkp.differ.compare.io.ImageDatasetProcessor;

import cz.nkp.differ.plugins.tools.DelayedComponentGenerator;

public class HistogramComponentGenerator extends DelayedComponentGenerator.ComponentGenerator {

    private ImageDatasetProcessor imageProcessor;
    private int scaleFactor;

    public HistogramComponentGenerator(ImageDatasetProcessor imageProcessor, int scaleFactor) {
	this.imageProcessor = imageProcessor;
	this.scaleFactor = scaleFactor;
    }

    public Component generateComponent(Component c) {
	JFreeChart histogram = ChartFactory.createXYLineChart(
		"",
		"",
		"",
		imageProcessor.getHistogramDataset(),
		PlotOrientation.VERTICAL,
		false,
		false,
		false);

	histogram.setBackgroundPaint(Color.WHITE);

	// get a reference to the plot for further customization...
	XYPlot plot = histogram.getXYPlot();
	plot.setBackgroundPaint(Color.WHITE);
	plot.setDomainGridlinesVisible(true);
	plot.setRangeGridlinesVisible(true);
	plot.setRangeGridlinePaint(Color.GRAY);
	plot.setDomainGridlinePaint(Color.GRAY);


	JFreeChartWrapper chartComponent = new JFreeChartWrapper(histogram, JFreeChartWrapper.RenderingMode.PNG);

	chartComponent.setGraphHeight(scaleFactor);
	chartComponent.setGraphWidth(scaleFactor);

	chartComponent.setWidth(scaleFactor, Component.UNITS_PIXELS);
	chartComponent.setHeight(scaleFactor, Component.UNITS_PIXELS);

	Layout l = (Layout) c;
	l.addComponent(chartComponent);

	return l;
    }
}
