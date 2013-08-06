package cz.nkp.differ.gui.windows;

import com.vaadin.terminal.FileResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import cz.nkp.differ.compare.io.CompareComponent;
import cz.nkp.differ.compare.metadata.MetadataSource;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author xrosecky
 */
public class RawDataWindow extends Window {

    private CompareComponent parent;

    public RawDataWindow(CompareComponent parent, MetadataSource source) {
	this.parent = parent;
	setCaption(String.format("Raw metadata for %s tool", source.getSourceName()));
	setModal(true);
	setDraggable(false);
	setResizable(false);
	center();
	setWidth("800px");
	setHeight("100%");
	VerticalLayout windowLayout = new VerticalLayout();
	Label exitCodeLabel = new Label();
	exitCodeLabel.setCaption("Exit code : "+source.getExitCode());
	windowLayout.addComponent(exitCodeLabel);
	createPanel(windowLayout, "Standard output", source.getStdout());
	createPanel(windowLayout, "Standard error output", source.getStderr());
	addComponent(windowLayout);
    }

    private void createPanel(Layout layout, String description, final String content) {
	Label desc = new Label();
	desc.setCaption(description);
	layout.addComponent(desc);
	Panel panel = new Panel();
	Label text = new Label(content, Label.CONTENT_TEXT);
	text.setWidth("800px");
	panel.addComponent(text);
	layout.addComponent(panel);
	Button downloadRaw = new Button();
	downloadRaw.setCaption("Download");
	downloadRaw.setEnabled(true);
	downloadRaw.setImmediate(true);
	downloadRaw.addListener(new ClickListener() {

	    @Override
	    public void buttonClick(ClickEvent event) {
		try {
                    File tmpFile = File.createTempFile("output", ".txt");
		    tmpFile.deleteOnExit();
		    FileUtils.writeByteArrayToFile(tmpFile, content.getBytes());
		    FileResource resource = new FileResource(tmpFile, parent.getApplication());
		    parent.getApplication().getMainWindow().open(resource);
		} catch (IOException ioe) {
		    ioe.printStackTrace();
		}
	    }

	});
	layout.addComponent(downloadRaw);
    }
}
