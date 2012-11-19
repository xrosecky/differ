package cz.nkp.differ.io;

import cz.nkp.differ.dao.ImageDAO;
import cz.nkp.differ.exceptions.FatalDifferException;
import cz.nkp.differ.exceptions.ImageDifferException;
import cz.nkp.differ.model.Image;
import cz.nkp.differ.model.User;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class ImageManager {

    private ImageDAO imageDAO;
    private File uploadDirectory;
    private List<String> supportedExtensions;
    private int maxSize = -1;

    public ImageManager() {
    }

    public ImageManager(ImageDAO imageDAO, File directory) {
	if (!directory.exists()) {
	    throw new FatalDifferException(directory.getAbsolutePath() + " does not exists!");
	}
	this.imageDAO = imageDAO;
	this.uploadDirectory = directory;
    }

    public ImageDAO getImageDAO() {
	return imageDAO;
    }

    public void setImageDAO(ImageDAO imageDAO) {
	this.imageDAO = imageDAO;
    }

    public File getUploadDirectory() {
	return uploadDirectory;
    }

    public void setUploadDirectory(File uploadDirectory) {
	this.uploadDirectory = uploadDirectory;
    }

    public List<String> getSupportedExtensions() {
	return supportedExtensions;
    }

    public void setSupportedExtensions(List<String> supportedExtensions) {
	this.supportedExtensions = supportedExtensions;
    }

    public int getMaxSize() {
	return maxSize;
    }

    public void setMaxSize(int maxSize) {
	this.maxSize = maxSize;
    }

    public boolean isSupportedByExtension(String fileName) {
	int dotAt = fileName.lastIndexOf('.');
	if (dotAt != -1) {
	    String extension = fileName.substring(dotAt + 1);
	    if (supportedExtensions == null || supportedExtensions.isEmpty()) {
		return true;
	    } else {
		return supportedExtensions.contains(extension);
	    }
	} else {
	    return false;
	}
    }

    public boolean checkIfFileSizeIsOK(long size) {
	if (maxSize > 0) {
	    return (size < maxSize);
	} else {
	    return true;
	}
    }

    public Image uploadImage(User user, File source) throws ImageDifferException {
	return this.uploadImage(user, source, null);
    }

    public Image uploadImage(User user, File source, String fileName) throws ImageDifferException {
	if (fileName != null) {
	    if (!isSupportedByExtension(fileName)) {
		StringBuilder extensions = new StringBuilder();
		String sep = "";
		for (String ext : supportedExtensions) {
		    extensions.append(sep).append(ext);
		    sep = ", ";
		}
		throw new ImageDifferException(ImageDifferException.ErrorCode.UNSUPPORTED_EXTENSION,
			String.format("Image extension is not supported, supported extensions are: %s.", extensions.toString()));
	    }
	}
	if (!checkIfFileSizeIsOK(source.length())) {
	    throw new ImageDifferException(ImageDifferException.ErrorCode.FILE_SIZE_EXCEEDED,
		    String.format("File size limit exceeded, limit is %s bytes.", this.getMaxSize()));
	}
	File destination = null;
	try {
	    File userDirectory = this.getUserDirectory(user.getId());
	    if (fileName == null) {
		destination = new File(userDirectory, source.getName());
	    } else {
		destination = new File(userDirectory, fileName);
	    }
	    FileUtils.copyFile(source, destination, true);
	} catch (IOException ioe) {
	    throw new ImageDifferException(ImageDifferException.ErrorCode.IMAGE_UPLOAD_ERROR, "Error when uploading file!", ioe);
	}
	Image image = new Image();
	image.setFileName(destination.getName());
	image.setUniqueName(destination.getName());
	image.setOwnerId(user.getId());
	image.setSize((int) destination.length());
	image.setFile(destination);
	imageDAO.addImage(image);
	return image;
    }

    public List<Image> getImages(User user) {
	List<Image> images = imageDAO.getImagesForUser(user);
	List<Image> sharedImages = imageDAO.getSharedImages();
	for (Image img : sharedImages) {
	    if (img.getOwnerId() != user.getId()) {
		images.add(img);
	    }
	}
	for (Image image : images) {
	    File userDirectory = this.getUserDirectory(image.getOwnerId());
	    image.setFile(new File(userDirectory, image.getUniqueName()));
	}
	return images;
    }

    public boolean deleteImage(User user, Image image) {
	if (image.getOwnerId() == user.getId()) {
	    if (image.getFile().delete() || !image.getFile().exists()) {
		imageDAO.deleteImage(image);
	    }
	    return true;
	} else {
	    return false;
	}
    }

    public void updateImage(Image image) {
	imageDAO.updateImage(image);
    }

    protected File getUserDirectory(int userId) {
	return new File(uploadDirectory, Integer.toString(userId));
    }
}
