package com.johnbryce.dao;

import java.util.Set;

import com.johnbryce.beans.Company;
import com.johnbryce.beans.Company_Coupon;
import com.johnbryce.exception.CouponException;
import com.johnbryce.exception.CreateException;
import com.johnbryce.exception.RemoveException;
import com.johnbryce.exception.UpdateException;

/**
 * this interface lists the data access object operations the association of a coupon to a company requires
 * 
 *  @author Eivy & Michal
 *
 */
public interface Company_CouponDAO {
	
	/**
	 * Insert a connection between a company and a coupon (represented by there IDs)
	 * 
	 * @param companyId the id of the company
	 * @param couponId the id of the coupon
	 * @throws CreateException if there was an error during the creation of the link in Data object
	 */
	void insertCompany_Coupon(long companyId, long couponId) throws CreateException;
	
	/**
	 * remove an entry representing the connection between a coupon and 
 
	 * @param companyId the id of the company
	 * @param couponId  the id of the coupon
	 * @throws RemoveException if the process of removal failed for any reason
	 */
	void removeCompany_Coupon(long companyId, long couponId) throws RemoveException;
	
    /**
     * this method removes all compnay's connection to different coupons.
     * 
     * @param company that would be removed from all connection with coupons
     * @throws RemoveException  if the process of removal failed for any reason
     */
    void removeCompany_Coupon(Company company) throws RemoveException;
	
	/**
	 * the method removes all the entries of companies linked with a given coupon id.
	 * 
	 * @param couponId the id of the coupon
	 * @throws RemoveException if the process of removal failed for any reason
	 * @throws CouponException if the process of retrieving the information failed
	 */
	void removeCompany_CouponByCouponId(long couponId) throws RemoveException, CouponException;

	/**
	 * this method return the companies link to the provided coupon
	 * 
	 * @param couponId  the id of the coupon
	 * @return a list of companies associated to a coupon.
	 * @throws CouponException if the process of data retrieval has failed.
	 */
	Set<Long> getCompanysByCouponId(long couponId) throws CouponException;
	
	/**
	 * this method return the coupons link to the provided company
	 * 
	 * @param companyId the id of the company
	 * @return a list of coupons associated to a company.
	 * @throws CouponException if the process of data retrieval has failed.
	 */
	Set<Long> getCouponsByCompanyId(long companyId) throws CouponException;
	
	/**
	 * this method get all the available pairing of company to coupon there is in a system.
	 * 
	 * @return return all the pairs of company and coupon (represented by id)  paring  a company to it's respected coupon
	 * @throws CouponException if the process of retrieval has failed.
	 */
	Set<Company_Coupon> getAllCompany_Coupons() throws CouponException;
	
	/**
	 * this method updates a link between company and coupon.
	 * 
	 * @param companyId the id of the company
	 * @param couponId the id of the coupon
	 * @throws UpdateException if the updaye process has failed 
	 */
	void updateCompany_Coupon(long companyId, long couponId) throws UpdateException;

	/**
	 * this method verifies if a coupon is already asosiated with the company.
	 * 
	 * @param companyId the id of the company
	 * @param couponId the id of the coupon
	 * @return if the coupon  and company are linked already
	 * @throws CouponException if there was a failure in the validation process
	 */
	boolean isCouponExistsForCompany(long companyId, long couponId) throws CouponException;
	
}