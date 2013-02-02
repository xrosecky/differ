package cz.nkp.differ.gui.tabs;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.compare.io.CompareComponent;
import cz.nkp.differ.compare.io.ImageFileAnalysisContainer;
import cz.nkp.differ.compare.io.SerializableImage;
import cz.nkp.differ.compare.io.SerializableImageProcessorResult;
import cz.nkp.differ.gui.windows.MainDifferWindow;
import cz.nkp.differ.io.ResultManager;
import cz.nkp.differ.model.Image;
import cz.nkp.differ.model.Result;

/**
 *
 * @author xrosecky
 */
public class ResultManagerTab extends HorizontalLayout {

    private MainDifferWindow mainWindow;
    private Table resultTable;
    private BeanItemContainer<Result> resultContainer = null;
    private Button button;
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
        resultContainer = new BeanItemContainer<Result>(Result.class, resultManager.getResults());
        resultTable = new Table("results", resultContainer);
        for (Result result : resultManager.getResults()) {
            resultTable.addItem(result);
        }
        resultTable.setSelectable(true);
        resultTable.setImmediate(true);
        resultTable.setMultiSelect(false);
        this.addComponent(resultTable);
        button = new Button();
        button.setCaption("show");
        button.addListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                if (resultTable.getValue() != null) {
                    Result result = (Result) resultTable.getValue();
                    if (result != null) {
                        try {
                            HorizontalLayout layout = new HorizontalLayout();
                            SerializableImageProcessorResult resultToShow = resultManager.getResult(result);
                            CompareComponent compareComponent = new CompareComponent();
                            compareComponent.setApplication(DifferApplication.getCurrentApplication());
                            ImageFileAnalysisContainer ifac = new ImageFileAnalysisContainer(resultToShow, compareComponent);
                            layout.addComponent(ifac.getComponent());
                            this_internal.setCustomView(layout);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        });
        this.addComponent(button);
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
