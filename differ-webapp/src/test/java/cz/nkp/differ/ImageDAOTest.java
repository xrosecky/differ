package cz.nkp.differ;

import cz.nkp.differ.dao.ImageDAO;
import cz.nkp.differ.model.Image;
import cz.nkp.differ.model.User;
import java.sql.SQLException;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author xrosecky
 */
public class ImageDAOTest extends Helper {

    public ImageDAO imageDAO;

    public ImageDAOTest() throws SQLException {
	this.imageDAO = Helper.getImageDAO();
    }

    @Test
    public void addAndDeleteImageTest() {
	Image image = new Image();
	image.setFileName("test.jp2");
	image.setOwnerId(1);
	image.setShared(true);
	image.setSize(1000);
	imageDAO.addImage(image);
	assert (image.getId() != 0);
	User user = new User();
	user.setId(1);
	List<Image> images1 = imageDAO.getImagesForUser(user);
	assert(images1.size() > 0);
	List<Image> images2 = imageDAO.getSharedImages();
	assert(images2.size() > 0);
	imageDAO.deleteImage(image);
	List<Image> images3 = imageDAO.getSharedImages();
	assert(images3.isEmpty());
    }
}
