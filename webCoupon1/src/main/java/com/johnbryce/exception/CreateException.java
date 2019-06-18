package com.johnbryce.exception;

/**
 * @author Eivy & Michal
 * The exception messages mechanism
 */

public class CreateException extends Exception {
	private static final long serialVersionUID = 1L;
	/**
	 * CreateException to handle with problems of table creation.
	 * @param message
	 */
	public CreateException(String message) {
		super(message);
	}

}

