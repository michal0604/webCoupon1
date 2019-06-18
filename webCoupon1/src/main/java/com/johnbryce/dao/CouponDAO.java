package com.johnbryce.dao;

import java.sql.SQLException;
import java.util.Set;

import com.johnbryce.beans.Coupon;
import com.johnbryce.beans.CouponType;
import com.johnbryce.exception.CouponException;
import com.johnbryce.exception.CreateException;
import com.johnbryce.exception.RemoveException;
import com.johnbryce.exception.UpdateException;

/**
 * this interface lists the data access object operations Coupon's requirements.
 * 
 *  @author Eivy & Michal
 *
 */
public interface CouponDAO {

	/**
	 * Inserts a coupon data set to the Database
	 * 
	 * @param coupon coupon to be inserted
	 * @return 
	 * @throws CreateException for problems in inserting the coupon to the DB 
	 * @throws SQLException for DB related failures 
	 */
	long insertCoupon(Coupon coupon) throws CreateException, SQLException;
	
	/**
	 * updates a coupon into the Database
	 * 
	 * @param coupon coupon to update
	 * @return 
	 * @throws UpdateException for problems in updating the coupon to the DB
	 * @throws CreateException for problems in inserting the coupon to the DB 
	 */
	long updateCoupon(Coupon Coupon) throws UpdateException, CreateException;


	/**
	 * get a coupon data set by the coupon's id.
	 * 
	 * @param couponId representing the id of the required coupon
	 * @return  a coupon which her id is  couponId.
	 * @throws CouponException for error related to the retrieval of the coupon 
	 * @throws CreateException 
	 */
	Coupon getCoupon(long couponId) throws CouponException, CreateException;

	/**
	 * get a list of all available coupons
	 * 
	 * @return  a list of available coupons
	 * @throws CouponException for error related to the retrieval of the coupon 
	 */
	Set<Coupon> getAllCoupons() throws CouponException;

	/**
	 * this function returns all available coupons by specific type 
	 * @param coupType the required coupon type
	 * @return  all available coupons that much the type
	 * @throws CouponException for error related to the retrieval of the coupon 
	 * @throws Exception 
	 */
	Set<Coupon> getAllCouponsByType(CouponType coupType) throws CouponException;

	/**
	 * this function returns all available coupons by specific max coupon price
	 * 
	 * @param priceMax the price that a coupon's price cannot exceed  
	 * @return a list of available coupon that are lower or equal to PriceMax
	 * @throws CouponException for error related to the retrieval of the coupon 
	 */
	Set<Coupon> getAllCouponsByPrice(double priceMax) throws CouponException;

	/**
	 * this function returns all available coupons by specific max coupon exploration Date
	 * 
	 * @param untilDate the date that a coupon's expire Date cannot exceed 
	 * @return a list of available coupon that expire before or the same date as untilDate
	 * @throws CouponException for error related to the retrieval of the coupon
	 */
	Set<Coupon> getAllCouponsByDate(String untilDate) throws CouponException;

	/**
	 * this function removed a given coupon from the coupon data
	 * 
	 * @param Coupon to be removed.
	 * 
	 * @throws RemoveException
	 * @throws CreateException
	 */
	void removeCoupon(Coupon Coupon) throws RemoveException, CreateException;

	/**
	 * this function search if a given string exist as a title of one of the coupons.
	 * 
	 * @param coupTitle a string representing a coupon title
	 * 
	 * @return true if there is such a coupon title, false otherwise. 
	 * @throws CouponException
	 */
	public boolean isCouponTitleExists(String coupTitle) throws CouponException;

	/**
	 * this function remove a coupon by it's id 
	 * 
	 * @param couponId of the coupon to be deleted
	 * 
	 * @throws CouponException
	 */
	void removeCouponID(long couponId) throws CouponException;

	

}
