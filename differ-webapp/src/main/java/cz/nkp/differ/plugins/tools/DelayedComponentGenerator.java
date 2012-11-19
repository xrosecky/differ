package cz.nkp.differ.plugins.tools;

import com.vaadin.ui.Component;

public class DelayedComponentGenerator{
	public static abstract class ComponentGenerator{
		public abstract Component generateComponent(Component c);
	};
	
	public static Component handleComponent(final Component c, final ComponentGenerator cg){
		Thread componentGeneratorThread = new Thread(){
			public void run(){
				cg.generateComponent(c);
			}
		};
		
		componentGeneratorThread.start();
		
		return c;
	}
}
