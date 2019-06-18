package com.johnbryce.dao;

import java.util.Set;

import com.johnbryce.beans.Coupon;
import com.johnbryce.beans.Customer;
import com.johnbryce.exception.CouponException;
import com.johnbryce.exception.CreateException;
import com.johnbryce.exception.CustomerException;
import com.johnbryce.exception.RemoveException;
import com.johnbryce.exception.UpdateException;

/**
 * this interface lists the data access object operations Customer's requirements.
 * @author Eivy & Michal.
 *
 */
public interface CustomerDAO {
	/**
	 * this method get customer object and put it on customer table.
	 * @param Customer
	 * @return 
	 * @throws CreateException
	 */
	long insertCustomer(Customer Customer) throws CreateException;

	/**
	 * 
	 * This method get Customer object and update the line in the Customer table by customer ID.
	 * @param Customer
	 * @return 
	 * @throws UpdateException
	 */
	long updateCustomer(Customer Customer) throws UpdateException;

	/**
	 * this method get customer and remove it from customer table.
	 * @param Customer
	 * @throws RemoveException
	 */
	void removeCustomer(Customer Customer) throws RemoveException;

	/**
	 * this method return all customers from DataBase.
	 * @return
	 * @throws CustomerException
	 */
	Set<Customer> getAllCustomers() throws CustomerException;

	/**
	 * this method get name of customer and password, check if name exists in customer table,
	 * and check if password fitting to password we got.if everything ok return true,else 
	 * return customerException.
	 * @param customerName
	 * @param password
	 * @return
	 * @throws CustomerException
	 */
	public Customer login(String customerName, String password) throws CustomerException;

	/**
	 * this method return specific customer by name from customer table.
	 * @param customerName
	 * @return
	 * @throws CustomerException
	 */
	Customer getCustomer(String customerName) throws CustomerException;

	/**
	 * this method return specific customer by customerId from customer table.
	 * @param customerId
	 * @return
	 * @throws CustomerException
	 */
	Customer getCustomer(long customerId) throws CustomerException;

	/**
	 * this method check if name of customer exist in customer table, if it does , return true,
	 * else return exception .
	 * @param customerName
	 * @return
	 * @throws CouponException
	 * @throws CustomerException
	 */
	boolean isCustomerNameExists(String customerName) throws CouponException, CustomerException;
	
	/**
	 * This method get Customer id and return all the Coupons of this Customer from Customer table.
	 * @param customerId
	 * @return
	 * @throws CouponException
	 */
	Set<Coupon> getAllCoupons(long customerId) throws CouponException;
	

}
