package cz.nkp.differ.gui.tabs;

import com.vaadin.ui.AbstractSelect.MultiSelectMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.gui.windows.MainDifferWindow;
import cz.nkp.differ.io.ResultManager;

/**
 *
 * @author xrosecky
 */
public class ResultManagerTab extends HorizontalLayout {

    private MainDifferWindow mainWindow;
    private Table resultTable;
    private Button button;
    private ResultManager resultManager;

    public ResultManagerTab(MainDifferWindow window) {
	this.mainWindow = window;
	resultManager = DifferApplication.getResultManager();
	resultTable = new Table();
	for (String result : resultManager.getResults()) {
	    resultTable.addItem(result);
	}
	resultTable.setMultiSelect(false);
	this.addComponent(resultTable);
	button = new Button();
	button.addListener(new ClickListener() {

	    @Override
	    public void buttonClick(ClickEvent event) {
		if (resultTable.getValue() != null) {
		    String result = (String) resultTable.getValue();
		    try {
			resultManager.getResult(result);
		    } catch (Exception ex) {
			throw new RuntimeException(ex);
		    }
		}
	    }

	});
	this.addComponent(button);
    }

}
