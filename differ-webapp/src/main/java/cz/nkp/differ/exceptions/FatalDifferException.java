package cz.nkp.differ.exceptions;

/**
 *
 * @author xrosecky
 */
public class FatalDifferException extends RuntimeException {

    public FatalDifferException(String message, Exception ex) {
	super(message, ex);
    }

    public FatalDifferException(String message) {
	super(message);
    }

}
