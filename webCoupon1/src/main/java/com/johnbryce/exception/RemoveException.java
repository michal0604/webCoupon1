package com.johnbryce.exception;

/**
 * @author Eivy & Michal
 * The exception messages mechanism
 */

public class RemoveException extends Exception{
	private static final long serialVersionUID = 1L;
	/**
	 * RemoveException for handle with problems of remove table.
	 * @param message
	 */
	public RemoveException(String message) {
		super(message);
	}

}


