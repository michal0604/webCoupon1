package com.johnbryce.exception;

/**
 * @author Eivy & Michal
 * The exception messages mechanism
 */
public class CompanyException extends Exception {
	private static final long serialVersionUID = 1L;
	/**
	 * CompanyException mechanism method.
	 * @param message
	 */
	public CompanyException(String message) {
		super(message);
	}

}

