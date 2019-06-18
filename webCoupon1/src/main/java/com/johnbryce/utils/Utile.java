package com.johnbryce.utils;

import java.sql.Date;
import java.time.LocalDate;

/**
 * @author Eivy & Michal.
 *
 */
public class Utile {

	/**
	 * This method return current date .
	 * @return
	 */
	public static Date getCurrentDate() {
		LocalDate localDate = LocalDate.now();
		Date date = Date.valueOf(localDate);
		return date;
	}

	/**
	 * This method get the expiration days and return the end date .
	 * @param numOfDays
	 * @return
	 */
	public static Date getDateAfter(int numOfDays) {

		LocalDate localDate = LocalDate.now();
		localDate = localDate.plusDays(numOfDays);
		Date date = Date.valueOf(localDate);

		return date;
	}

	/**
	 * This method return the driver of the derby JDBC
	 * @return
	 */
	public static String getDriverData() {
		return "org.apache.derby.jdbc.ClientDriver";
	}

	/**
	 * This method return the DB local host URL and the port
	 * @return
	 */
	public static String getDBUrl() {
		return "jdbc:derby://localhost:3301/MyDB;create=true";
	}

}
