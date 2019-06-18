package com.johnbryce.exception;

/**
 * @author Eivy & Michal
 * The exception messages mechanism
 */

public class DailyCouponException extends Exception {
	private static final long serialVersionUID = 1L;
	/**
	 * daily coupon exception is to handle with problems with his tasks.
	 * @param message
	 */
	public DailyCouponException(String message) {
		super(message);
	}


}
