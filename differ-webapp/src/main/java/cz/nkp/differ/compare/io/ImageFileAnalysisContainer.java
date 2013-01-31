package cz.nkp.differ.compare.io;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import cz.nkp.differ.compare.metadata.ImageMetadata;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;

import com.vaadin.ui.Label;
import java.io.File;

import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.BaseTheme;
import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.gui.windows.FullSizeImageWindow;
import cz.nkp.differ.gui.windows.HistogramSettingsWindow;
import cz.nkp.differ.gui.windows.RawDataWindow;
import java.awt.Color;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.vaadin.addon.JFreeChartWrapper;

public class ImageFileAnalysisContainer {

    private ImageProcessorResult result;
    private CompareComponent parent;
    private XYPlot plot;
    private final int scaleFactor = 400;
    private JFreeChartWrapper chartComponent = null;
    private List<String> nonConflictMetadata = Arrays.asList("exit-code");

    public ImageFileAnalysisContainer(ImageProcessorResult result, CompareComponent parent) {
        this.result = result;
        this.parent = parent;
    }

    public Image getImage() {
        return result.getPreview();
    }

    public Layout getComponent() {
        final VerticalLayout layout = new VerticalLayout();
        generateComponent(layout);
        return layout;
    }

    private void generateComponent(final Layout layout) {
        // Image preview
        final Resource imageFullResource = imageToResource(result.getFullImage());
        final Resource imageScaledResource = imageToResource(result.getPreview());
        Button imageButton = new Button();
        imageButton.setStyleName(BaseTheme.BUTTON_LINK);
        imageButton.setIcon(imageScaledResource);
        if (imageFullResource != null) {
            imageButton.addListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {

                    Embedded img = new Embedded(null, imageFullResource);
                    img.setType(Embedded.TYPE_IMAGE);
                    DifferApplication.getCurrentApplication().getMainWindow().addWindow(new FullSizeImageWindow(img));
                }
            });
        }
        layout.addComponent(imageButton);
        // Image checksum
        Label hashLabel = new Label();
        hashLabel.setCaption(String.format("Hash: %s", result.getMD5Checksum()));
        layout.addComponent(hashLabel);
        // Histogram
        if (result.getType() == ImageProcessorResult.Type.IMAGE) {
            generateHistogramComponent(layout, false);
        } else {
            generateHistogramComponent(layout, true);
        }
        // Metadata table
        BeanItemContainer metadataContainer = new BeanItemContainer<ImageMetadata>(ImageMetadata.class, result.getMetadata());
        metadataContainer.sort(new String[]{"key"}, new boolean[]{true});
        final Table metadataTable = new Table("Metadata", metadataContainer);
        metadataTable.setSelectable(true);
        metadataTable.setMultiSelect(false);
        metadataTable.setImmediate(true);
        metadataTable.setVisibleColumns(new Object[]{"key", "source", "value", "unit"});
        metadataTable.setCellStyleGenerator(new Table.CellStyleGenerator() {
            @Override
            public String getStyle(Object itemId, Object propertyId) {
                ImageMetadata metadata = (ImageMetadata) itemId;
                if (result.getType() == ImageProcessorResult.Type.COMPARISON) {
                    String key = metadata.getKey();
                    if (Arrays.asList("red", "blue", "green").contains(key)) {
                        return key;
                    }
                } else {
                    if (!nonConflictMetadata.contains(metadata.getKey())) {
                        if (metadata.isConflict()) {
                            return "red";
                        } else {
                            return "green";
                        }
                    }
                }
                return "";
            }
        });
        layout.addComponent(metadataTable);
        final Button rawData = new Button();
        rawData.setCaption("Raw data");
        rawData.addListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    ImageMetadata metadata = (ImageMetadata) metadataTable.getValue();
                    DifferApplication.getCurrentApplication().getMainWindow().addWindow(new RawDataWindow(parent, metadata.getSource()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        rawData.setImmediate(true);
        rawData.setEnabled(false); //FIXME
        layout.addComponent(rawData);
        metadataTable.addListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                rawData.setEnabled(true);
            }
        });
    }

    private void generateHistogramComponent(final Layout mainLayout, boolean logarithmic) {
        HorizontalLayout histogramLayout = new HorizontalLayout();
        histogramLayout.setMargin(true);
        histogramLayout.setWidth("100%");
        histogramLayout.setSpacing(true);
        this.chartComponent = createHistogramComponent(result.getHistogram(), logarithmic);
        mainLayout.addComponent(chartComponent);
        Button downloadButton = new Button();
        downloadButton.setCaption("Download as CSV");
        downloadButton.addListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    StringBuilder sb = new StringBuilder();
                    int[][] bins = result.getHistogram();
                    for (int i = 0; i < 256; i++) {
                        sb.append(i).append(',').append(bins[0][i]).append(',').append(bins[1][i]).append(',').append(bins[2][i]).append("\n");
                    }
                    File tmpFile = File.createTempFile("histogram", ".csv");
                    tmpFile.deleteOnExit();
                    FileUtils.writeByteArrayToFile(tmpFile, sb.toString().getBytes());
                    FileResource resource = new FileResource(tmpFile, parent.getApplication());
                    parent.getApplication().getMainWindow().open(resource);
                } catch (Exception ex) {
                    parent.getApplication().getMainWindow().showNotification("Error when creating CSV file for upload", "",
                            Notification.TYPE_ERROR_MESSAGE);
                }
            }
        });
        histogramLayout.addComponent(downloadButton);

        if (result.getType() == ImageProcessorResult.Type.COMPARISON) {
            Button zoomButton = new Button();
            zoomButton.setImmediate(true);
            zoomButton.setCaption("Setting");
            zoomButton.addListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    double startX = plot.getDomainAxis().getLowerBound();
                    double endX = plot.getDomainAxis().getUpperBound();
                    double startY = plot.getRangeAxis().getLowerBound();
                    double endY = plot.getRangeAxis().getUpperBound();
                    final HistogramSettingsWindow zoomSettings = new HistogramSettingsWindow(new double[]{startX, endX, startY, endY}, true);
                    ClickListener onSubmit = new ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent event) {
                            try {
                                double[] xRange = zoomSettings.getXRange();
                                double[] yRange = zoomSettings.getYRange();
                                boolean logarithmic = zoomSettings.isLogarithmic();
                                JFreeChartWrapper newChartComponent = createHistogramComponent(result.getHistogram(),
                                        logarithmic, xRange[0], xRange[1], yRange[0], yRange[1]);
                                mainLayout.replaceComponent(chartComponent, newChartComponent);
                                chartComponent = newChartComponent;
                            } catch (NumberFormatException nfe) {
                                parent.getApplication().getMainWindow().showNotification("Invalid value", "", Notification.TYPE_ERROR_MESSAGE);
                            }
                        }
                    };
                    zoomSettings.setOnSubmit(onSubmit);
                    parent.getApplication().getMainWindow().addWindow(zoomSettings);
                }
            });
            histogramLayout.addComponent(zoomButton);
        }
        mainLayout.addComponent(histogramLayout);
    }

    private JFreeChartWrapper createHistogramComponent(int[][] bins, boolean logarithmic) {
        return createHistogramComponent(bins, logarithmic, -1.0, -1.0, -1.0, -1.0);
    }

    private JFreeChartWrapper createHistogramComponent(int[][] bins, boolean logarithmic, double xStart, double xEnd, double yStart, double yEnd) {
        XYSeries redChannel = new XYSeries("Red");
        XYSeries greenChannel = new XYSeries("Green");
        XYSeries blueChannel = new XYSeries("Blue");
        for (int i = 0; i < 256; i++) {
            if (logarithmic) {
                redChannel.add((double) i, (bins[0][i] > 0) ? Math.log10(bins[0][i]) : 0);
                greenChannel.add((double) i, (bins[1][i] > 0) ? Math.log10(bins[1][i]) : 0);
                blueChannel.add((double) i, (bins[2][i] > 0) ? Math.log10(bins[2][i]) : 0);
            } else {
                redChannel.add(i, bins[0][i]);
                greenChannel.add(i, bins[1][i]);
                blueChannel.add(i, bins[2][i]);
            }
        }
        XYSeriesCollection rgb = new XYSeriesCollection();
        rgb.addSeries(redChannel);
        rgb.addSeries(greenChannel);
        rgb.addSeries(blueChannel);
        JFreeChart histogram = ChartFactory.createXYLineChart(
                "",
                "",
                "",
                rgb, //imageProcessor.getHistogramDataset(),
                PlotOrientation.VERTICAL,
                false,
                false,
                false);
        histogram.setBackgroundPaint(Color.WHITE);
        // get a reference to the plot for further customization...
        plot = histogram.getXYPlot();
        if (xStart >= 0 && xEnd >= 0 && yStart >= 0 && yEnd >= 0) {
            plot.getDomainAxis(0).setLowerBound(xStart);
            plot.getDomainAxis(0).setUpperBound(xEnd);
            plot.getRangeAxis().setLowerBound(yStart);
            plot.getRangeAxis().setUpperBound(yEnd);
        }
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.setDomainGridlinePaint(Color.GRAY);
        JFreeChartWrapper chart = new JFreeChartWrapper(histogram, JFreeChartWrapper.RenderingMode.PNG);
        chart.setGraphHeight(scaleFactor);
        chart.setGraphWidth(scaleFactor);
        chart.setWidth(scaleFactor, Component.UNITS_PIXELS);
        chart.setHeight(scaleFactor, Component.UNITS_PIXELS);
        return chart;
    }

    public Resource imageToResource(Image img) {
        if (img == null) {
            return null;
        }
        if (img instanceof SerializableImage) {
            img = ((SerializableImage) img).getBufferedImage();
        }
        try {
            String FILE_EXT = "png";
            File temp = File.createTempFile("image", "." + FILE_EXT);
            OutputStream stream = new BufferedOutputStream(new FileOutputStream(temp));
            /* Write the image to a buffer. */
            BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics g = bimage.getGraphics();
            g.drawImage(img, 0, 0, null);
            g.dispose();
            ImageIO.setUseCache(false);
            ImageIO.write(bimage, FILE_EXT, stream);
            bimage = null;
            stream.flush();
            stream.close();
            return new FileResource(temp, parent.getApplication());
        } catch (IOException e) {
            return null;
        }
    }
}
