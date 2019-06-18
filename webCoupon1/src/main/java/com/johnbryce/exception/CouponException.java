package com.johnbryce.exception;

/**
 * @author Eivy & Michal
 * The exception messages mechanism
 */

public class CouponException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * CouponException mechanism method.
	 * @param message
	 */
	public CouponException(String message) {
		super(message);
	}

}
