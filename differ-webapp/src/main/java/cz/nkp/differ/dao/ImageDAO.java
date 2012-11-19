package cz.nkp.differ.dao;

import cz.nkp.differ.model.Image;
import cz.nkp.differ.model.User;
import java.util.List;

/**
 *
 * @author xrosecky
 */
public interface ImageDAO {

    public void addImage(Image image);
    public void deleteImage(Image image);
    public void updateImage(Image image);
    public List<Image> getImagesForUser(User user);
    public List<Image> getSharedImages();

}
