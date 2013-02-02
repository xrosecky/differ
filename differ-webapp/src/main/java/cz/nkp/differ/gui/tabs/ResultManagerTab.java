package cz.nkp.differ.gui.tabs;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.compare.io.CompareComponent;
import cz.nkp.differ.compare.io.ImageFileAnalysisContainer;
import cz.nkp.differ.compare.io.SerializableImageProcessorResult;
import cz.nkp.differ.compare.io.SerializableImageProcessorResults;
import cz.nkp.differ.gui.windows.MainDifferWindow;
import cz.nkp.differ.io.ResultManager;
import cz.nkp.differ.model.Result;

/**
 *
 * @author xrosecky
 */
public class ResultManagerTab extends HorizontalLayout {

    private MainDifferWindow mainWindow;
    private Table resultTable;
    private BeanItemContainer<Result> resultContainer = null;
    private Button showButton;
    private Button reloadButton;
    private ResultManager resultManager;
    private Layout customViewWrapper;
    private Button customLayoutBackButton;
    private ResultManagerTab this_internal = this;

    public ResultManagerTab(MainDifferWindow window) {
        this.mainWindow = window;
        resultManager = DifferApplication.getResultManager();
        this.setDefaultView();
    }

    public void setDefaultView() {
	HorizontalLayout mainLayout = new HorizontalLayout();
	this.addComponent(mainLayout);
        resultContainer = new BeanItemContainer<Result>(Result.class, resultManager.getResults());
        resultTable = new Table("results", resultContainer);
        resultTable.setSelectable(true);
        resultTable.setImmediate(true);
        resultTable.setMultiSelect(false);
        mainLayout.addComponent(resultTable);
	VerticalLayout buttonPanelRoot = new VerticalLayout();
	Panel panel = new Panel();
	panel.addComponent(buttonPanelRoot);
	mainLayout.addComponent(panel);
        showButton = new Button();
        showButton.setCaption("show");
        showButton.addListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                if (resultTable.getValue() != null) {
                    Result result = (Result) resultTable.getValue();
                    if (result != null) {
                        try {
                            HorizontalLayout layout = new HorizontalLayout();
                            SerializableImageProcessorResults resultsToShow = resultManager.getResult(result);
                            CompareComponent compareComponent = new CompareComponent();
                            compareComponent.setApplication(DifferApplication.getCurrentApplication());
			    for (SerializableImageProcessorResult resultToShow : resultsToShow.getResults()) {
				ImageFileAnalysisContainer ifac = new ImageFileAnalysisContainer(resultToShow, compareComponent);
				layout.addComponent(ifac.getComponent());
			    }
                            this_internal.setCustomView(layout);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        });
	panel.addComponent(showButton);
	reloadButton = new Button();
	reloadButton.setCaption("reload");
	reloadButton.addListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                this_internal.removeAllComponents();
		this_internal.setDefaultView();
            }
        });
	panel.addComponent(reloadButton);
	this.setSizeUndefined();
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

    private Button.ClickListener customViewWrapperBackButtonListener = new Button.ClickListener() {
        @Override
        public void buttonClick(ClickEvent event) {
            this_internal.removeAllComponents();
            this_internal.setDefaultView();
        }
    };
}
