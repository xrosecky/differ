package cz.nkp.differ.exceptions;

/**
 *
 * @author xrosecky
 */
public class UserDifferException extends DifferException {

    public enum ErrorCode {
	USER_ALREADY_EXISTS,
	BAD_PASSWORD_OR_USERNAME,
	USER_NOT_FOUND,
    }

    protected ErrorCode code;

    public UserDifferException(ErrorCode err, String msg) {
	super(msg);
	this.code = err;
    }

    public UserDifferException(ErrorCode err, String msg, Exception ex) {
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