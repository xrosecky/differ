package cz.nkp.differ.model;

import java.io.File;

/**
 *
 * @author xrosecky
 */
public class Image {

    private int id;
    private String fileName;
    private String uniqueName;
    private int ownerId;
    private int size;
    private boolean shared = false;
    private File file;

    public Image() {
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public String getUniqueName() {
	return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
	this.uniqueName = uniqueName;
    }

    public int getOwnerId() {
	return ownerId;
    }

    public void setOwnerId(int ownerId) {
	this.ownerId = ownerId;
    }

    public boolean isShared() {
	return shared;
    }

    public void setShared(boolean shared) {
	this.shared = shared;
    }

    public int getSize() {
	return size;
    }

    public void setSize(int size) {
	this.size = size;
    }

    public File getFile() {
	return file;
    }

    public void setFile(File file) {
	this.file = file;
    }
    
}
