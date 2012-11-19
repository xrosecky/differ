package cz.nkp.differ.exceptions;

/**
 *
 * @author xrosecky
 */
public class ImageDifferException extends DifferException {

    public enum ErrorCode {
	IMAGE_UPLOAD_ERROR,
	IMAGE_ALREADY_EXISTS,
	UNSUPPORTED_EXTENSION,
	FILE_SIZE_EXCEEDED,
	IMAGE_READ_ERROR
    }

    protected ErrorCode code;

    public ImageDifferException(ErrorCode err, String msg) {
	super(msg);
	this.code = err;
    }

    public ImageDifferException(ErrorCode err, String msg, Exception ex) {
	super(msg, ex);
	this.code = err;
    }

    public ErrorCode getCode() {
	return code;
    }

    public void setCode(ErrorCode code) {
	this.code = code;
    }

}
