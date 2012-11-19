package cz.nkp.differ.util;

public class GeneralMacros {
	
	/**
	 * Used to check for null values in a given set of <code>Object</code>s. Useful for safely validating
	 * function arguments.
	 * @param objects
	 * @return
	 */
	public static final boolean containsNull(Object...objects){
		for(Object o : objects){
			if(o == null){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks the objects list for null values, and throws an IllegalArgumentException if a null is present.
	 * @param objects
	 */
	public static final void errorIfContainsNull(Object... objects){
		if(GeneralMacros.containsNull(objects)){
			throw new IllegalArgumentException("Arguments given to a method were null when that method disallows null arguements");
		}
	}
}
