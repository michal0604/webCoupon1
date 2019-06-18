package com.johnbryce.facad;

public interface CouponClientFacade {

	/**
	 * in this class we have method that check by name & password and we use it
	 *  at classes to enable login by clientType.
	 * @param name
	 * @param password
	 * @return
	 * @throws Exception 
	 */
	public CouponClientFacade login(String name, String password) throws Exception;

}
