package com.johnbryce.exception;

/**
 * @author Eivy & Michal
 * The exception messages mechanism
 */

public class CustomerException extends Exception{
	private static final long serialVersionUID = 1L;
	/**
	 * CustomerException mechanism method.
	 * @param message
	 */
	public CustomerException(String message) {
		super(message);
	}

}
