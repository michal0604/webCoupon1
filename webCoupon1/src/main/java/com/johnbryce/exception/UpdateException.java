package com.johnbryce.exception;

/**
 * @author Eivy & Michal
 * The exception messages mechanism
 */

public class UpdateException extends Exception {
	private static final long serialVersionUID = 1L;
	/**
	 * UpdateException to handle with problems of update table.
	 * @param message
	 */
	public UpdateException(String message) {
		super(message);
	}

}


