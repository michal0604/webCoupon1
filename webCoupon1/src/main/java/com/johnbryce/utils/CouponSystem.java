package com.johnbryce.utils;

import java.sql.Connection;

import com.johnbryce.exception.CouponException;
import com.johnbryce.exception.DailyCouponException;
import com.johnbryce.facad.AdminFacad;
import com.johnbryce.facad.CompanyFacade;
import com.johnbryce.facad.CouponClientFacade;
import com.johnbryce.facad.CustomerFacad;

/**
 * @author Eivy & MICHAL
 * 
 * coupon system is actually Authorization system. it is a singleton class
 * she knows what facade to return
 * according to login details.
 * 
 *
 */
public class CouponSystem {

	private static CouponSystem instance;
	DailyCouponExpirationTask DailyTask;
	Thread thread;
	Connection connection;

	public enum clientType {
		Admin, Customer, Company
	};

	/**
	 * Thread timer - 1000*3600*24 is every 24 hours
	 */
	private static final int DAY = 1000 * 3600 * 24;
	private static final int SLEEPTIME = 1 * DAY;

	private CouponSystem() throws CouponException {
		// Activate the daily Coupons Deletion Demon (Thread)
		DailyTask = new DailyCouponExpirationTask(SLEEPTIME);
		thread = new Thread(DailyTask);
		thread.start();
	}

	/**
	 * this method return instance of couponSystem
	 * @return
	 * @throws CouponException
	 */
	public static CouponSystem getInstance() throws CouponException {
		if (instance == null)
			instance = new CouponSystem();
		return instance;
	}

	/**
	 * The login method enable access to the system	
	 * @param name -name of using the system
	 * @param password-password to the system
	 * @param clientType-customer, admin, company return facade
	 * @return
	 * @throws Exception
	 */
	public static CouponClientFacade login(String name, String password, ClientType clientType) throws Exception {
		CouponClientFacade couponClientFacade = null;

		switch (clientType) {
		case ADMIN:
			couponClientFacade = new AdminFacad();
			break;
		case COMPANY:
			couponClientFacade = new CompanyFacade();
			break;
		case CUSTOMER:
			couponClientFacade = new CustomerFacad();
			break;
		default:
			throw new CouponException("login failed");
		}

		couponClientFacade = couponClientFacade.login(name, password);
		if (couponClientFacade != null) {
			return couponClientFacade;
		} else {
			return null;
		}

	}

	/**
	 * Shutdown system. close all the connectionPool. 
	   daily task stop working.
	 **/
	public void shutdown() throws DailyCouponException {

		try {
			ConnectionPool connectionPool = ConnectionPool.getInstance();
			try {
				connectionPool.closeAllConnections(connection);
			} catch (Exception e) {
				throw new DailyCouponException("connection failed");
			}
		} catch (Exception e) {
			throw new DailyCouponException("shut down failed");
		}
		DailyTask.stop();
	}

	/**
	 * @param debugMode
	 */
	public void setDebugMode(boolean debugMode) {
		DailyTask.setDebugMode(debugMode);
		
	}
}
