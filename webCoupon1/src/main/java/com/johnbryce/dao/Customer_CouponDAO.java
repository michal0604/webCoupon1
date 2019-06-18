package com.johnbryce.dao;

import java.util.Set;

import com.johnbryce.beans.Customer;
import com.johnbryce.beans.Customer_Coupon;
import com.johnbryce.exception.CouponException;
import com.johnbryce.exception.CreateException;
import com.johnbryce.exception.RemoveException;
import com.johnbryce.exception.UpdateException;

/**
 *  this interface lists the data access object operations the association of a coupon to a customer requires
 * @author Eivy & Michal
 *
 */
public interface Customer_CouponDAO {
	
		
		/**Inserts a customer_coupon data set to the Database
		 * @param customerId
		 * @param couponId
		 * @throws CreateException
		 */
		void insertCustomer_Coupon(long customerId, long couponId) throws CreateException;
		
		/**
		 * this method get customer_coupon by customerId&  couponId and delete it in customer_coupon table.
		 * @param customerId of cus
		 * @param couponId
		 * @throws RemoveException
		 */
		void removeCustomer_Coupon(long customerId, long couponId) throws RemoveException;
		
		/**
		 * remove by customer from database/customer_coupon table.
		 * @param customer
		 * @throws RemoveException
		 */
		void removeCustomer_Coupon(Customer customer) throws RemoveException;
		
		/**
		 * remove by couponId from database/customer_coupon table.
		 * @param couponId
		 * @throws RemoveException
		 */
		void removeCustomer_CouponByCoupId(long couponId) throws RemoveException;
	
		/**
		 * this method return all customers by coupon ID from customer_coupon table.
		 * @param couponId
		 * @return
		 * @throws CouponException
		 * @throws CreateException
		 */
		Set<Long> getCustomersByCouponId(long couponId) throws CouponException, CreateException;
		
		/**
		 * this method return all coupons by specific customerID
		 * @param customerId
		 * @return
		 * @throws CouponException
		 * @throws CreateException
		 */
		Set<Long> getCouponsByCustomerId(long customerId) throws CouponException, CreateException;
		
		/**
		 * this method return a list of all coupons of customer.
		 * @return
		 * @throws CouponException
		 * @throws CreateException
		 */
		Set<Customer_Coupon> getAllCustomer_Coupon() throws CouponException, CreateException;	
		
		/**
		 * this method update coupon and customer by ID in DataBase.
		 * @param customerId
		 * @param couponId
		 * @throws UpdateException
		 */
		void updateCustomer_Coupon(long customerId, long couponId) throws UpdateException;

		/**
		 * this method check if specific coupon purchased by  specific customer and return true or false.
		 * @param customerId
		 * @param couponId
		 * @return
		 * @throws CouponException
		 */
		boolean isCouponPurchasedByCustomer(long customerId, long couponId) throws CouponException;
		
}