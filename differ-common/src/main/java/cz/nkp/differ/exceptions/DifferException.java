package cz.nkp.differ.exceptions;

/**
 *
 * @author xrosecky
 */
public class DifferException extends Exception {

    public DifferException(Exception ex) {
	super(ex);
    }

    public DifferException(String message, Exception ex) {
	super(message, ex);
    }

    public DifferException(String message) {
	super(message);
    }

}
