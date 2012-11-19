package cz.nkp.differ;

import cz.nkp.differ.exceptions.ImageDifferException;
import cz.nkp.differ.io.ImageManager;
import cz.nkp.differ.model.Image;
import cz.nkp.differ.model.User;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author xrosecky
 */
public class ImageManagerTest extends Helper {

    protected ImageManager imageManager;

    public ImageManagerTest()throws SQLException {
	this.imageManager = Helper.getImageManager();
    }

    @Test
    public void uploadImage() throws ImageDifferException {
	User user = new User();
	user.setId(25);
	user.setUserName("test");
	List<Image> imagesBeforeInsert = imageManager.getImages(user);
	assert(imagesBeforeInsert.isEmpty());
	File file = new File("/home/xrosecky/6-20-16.jpg");
	Image image = imageManager.uploadImage(user, file);
	assert(image.getOwnerId() == user.getId());
	assert(image.getId() != 0);
	List<Image> imagesAfterInsert = imageManager.getImages(user);
	assert(!imagesAfterInsert.isEmpty());
	assert(image.getFile().exists());
	imageManager.deleteImage(user, image);
	List<Image> imagesAfterDelete = imageManager.getImages(user);
	assert(imagesAfterDelete.isEmpty());
	assert(!image.getFile().exists());
    }

    @Test
    public void updateImage() throws ImageDifferException {
	User user = new User();
	user.setId(26);
	user.setUserName("test2");
	File file = new File("/home/xrosecky/6-20-16.jpg");
	Image image = imageManager.uploadImage(user, file);
	image.setShared(true);
	imageManager.updateImage(image);
	for (Image img : imageManager.getImages(user)) {
	    assert(img.isShared());
	}
	image.setShared(false);
	imageManager.updateImage(image);
	for (Image img : imageManager.getImages(user)) {
	    assert(!img.isShared());
	}
    }

}