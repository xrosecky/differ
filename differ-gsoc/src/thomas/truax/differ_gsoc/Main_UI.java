package thomas.truax.differ_gsoc;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.AbstractSelect.ItemDescriptionGenerator;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This sample generates graphical tooltips for a table that references image files. (Like the
 * one used on the DIFFER project page.) The tooltips show a preview of the current image item 
 * being pointed to in the table.
 * 
 * This project was built in ~ Eclipse JEE Juno SR2 + Tomcat 7.0 + Vaadin 7.0.5
 * 
 * Important Note: This sample references images directly within the project folder. So if you
 * are going to alter the project structure, make sure those changes are reflected in the code. 
 * (The image folder path is set on Line 56.) 
 * 
 * @author Thomas Truax
 * @since May 9, 2013
 */
@SuppressWarnings("serial")
public class Main_UI extends UI {
	
	@Override
	protected void init(VaadinRequest request) {
		//initial layout setup
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout);
		
		
		//let's build a small sample table for this example, with a single column
		Table table = new Table("Table Example");
		table.setWidth("200");
		table.setSelectable(true);
		table.addContainerProperty("Image Files", String.class, null);

		
		//this array holds our image filenames, we need to pass this to our addGraphicTooltip function
		//we will also these strings for our row items (for simplicity)
		final String [] fileArray = {"image_001.png", "image_020.png", "image_300.png"};
		
		
		//next we run a for-loop to quickly add the image names as row items, using the filename array
		for (int i = 0; i < fileArray.length; i++) {
			table.addItem(new Object[] {fileArray[i]}, i);
		}
		
		
		//here we designate the folder path which holds our image files
		//this doesn't necessarily need to be an absolute filesystem path, it could also be a URL for example
		String imgFolderPath = VaadinService.getCurrent().getBaseDirectory().getParent() + "/img/";
		
		
		//now we call our custom function, which will construct graphic tooltips from our image files
		addGraphicTooltips(table, imgFolderPath, fileArray);
		
		
		//finally, add the table to our layout
		layout.addComponent(table);
	}

	
	/**
	 * This function utilizes ItemDescriptionGenerator() to create graphic tooltips for each table item. 
	 * The indexes of the string array items (images) should directly correspond to the table item indexes.
	 * 
	 * @param table - Table which will be given the graphic tooltips.
	 * @param basePath - Fully qualified path to a directory containing the desired image files.
	 *                   <i><b>Examples:</b> file:///C:/test/images/</i> or <i>http://www.mysite.com/img/</i>
	 * @param fileNameArr - String array which holds the filenames for each tooltip image.
	 * @author Thomas Truax
	 */
	public void addGraphicTooltips(final Table table, final String basePath, final String [] fileNameArr) {
		
		table.setItemDescriptionGenerator(new ItemDescriptionGenerator() {				
			public String generateDescription(Component source, Object itemId, Object propertyId) {
				
				//ensure we are pointing to a valid item, if so, we generate graphic tooltip for table item
				if (itemId != null) {
					int i = (Integer) itemId; //using (Integer) to reference the id number contained in the Object
					return "<img src=\"" + basePath + fileNameArr[i] + "\">"; //proper img <tag> is constructed here
				}
				
				return null;
			}
		});
	}
}