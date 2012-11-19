package cz.nkp.differ.plugins;

import com.vaadin.ui.Component;

public interface PluginComponentReadyCallback {
	public void ready(Component c);
	public void setCompleted(int percentage);
}
