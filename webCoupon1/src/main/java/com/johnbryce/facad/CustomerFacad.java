package com.johnbryce.facad;
import java.util.HashSet;
import java.util.Set;

import com.johnbryce.beans.Coupon;
import com.johnbryce.beans.CouponType;
import com.johnbryce.beans.Customer;
import com.johnbryce.dao.CouponDAO;
import com.johnbryce.dao.Customer_CouponDAO;
import com.johnbryce.dbdao.CouponDBDAO;
import com.johnbryce.dbdao.CustomerDBDAO;
import com.johnbryce.dbdao.Customer_CouponDBDAO;
import com.johnbryce.exception.CouponException;
import com.johnbryce.exception.CreateException;
import com.johnbryce.exception.UpdateException;
import com.johnbryce.utils.Utile;

/**
 * @author Eivy & Michal
 * 
 * Customer Facade
 *
 */
public class CustomerFacad implements CouponClientFacade {

	private CouponDAO couponDAO;
	private Customer_CouponDAO customer_CouponDAO;
	private long custId = 0;
	private Customer customer;

	/**
	 * Constructor
	 * @throws CouponException
	 */
	public CustomerFacad() throws CouponException {
		couponDAO = new CouponDBDAO();
		customer_CouponDAO = new Customer_CouponDBDAO();
	}


	/**
	 * Buy Coupon by customer method.
	 * this method update amount of coupons left in coupon table
	 * Coupon cannot be purchased more than once by same customer.
	 * customer can't buy same coupon, only once.
	 * Customer can't buy if coupon expired.
	 * Coupon cannot be purchased if out of stock
	 * @param coupId
	 * @throws CouponException
	 */
	public void purchaseCoupon(long coupId) throws CouponException { 
		if(custId == 0) {
			throw new CouponException("the operation was canceled due to not being loged in");
		}
		Coupon coupon = new Coupon();
		try {
			coupon = couponDAO.getCoupon(coupId);
		} catch (CreateException e) {
			throw new CouponException("purchase coupon by customer failed");
		}
		if (coupon != null) {
			if (coupon.getAmount() > 0) {
				if (coupon.getEnd_date().getTime() >= Utile.getCurrentDate().getTime()) {
					if (!customer_CouponDAO.isCouponPurchasedByCustomer(custId, coupId)){
						coupon.setAmount(coupon.getAmount() - 1);
						try {
							couponDAO.updateCoupon(coupon);
						} catch (UpdateException e) {
							throw new CouponException("update coupon by customer failed");
						} catch (CreateException e) {
							throw new CouponException("update coupon by customer failed");
						}
						try {
							customer_CouponDAO.insertCustomer_Coupon(custId, coupId);
						} catch (CreateException e) {
							throw new CouponException("insert coupon by customer failed");
						}
						
					}
				}
			}
		}
	}




	/**
	 * View all customer coupons by type 
	 * @param couponType
	 * @return
	 * @throws CouponException
	 */
	public Set<Coupon> getAllCouponsByType(CouponType couponType) throws CouponException {
		if(custId == 0) {
			throw new CouponException("the operation was canceled due to not being loged in");
		}
		return couponDAO.getAllCouponsByType(couponType);
	}

	/**
	 * view all coupons that purchased by customer
	 * @return
	 * @throws CouponException
	 */
	public Set<Coupon> getAllPurchasedCoupons() throws CouponException {
		if(custId == 0) {
			throw new CouponException("the operation was canceled due to not being loged in");
		}
		Set<Long> customersCoupons;
		try {
			customersCoupons = customer_CouponDAO.getCouponsByCustomerId(custId);
		} catch (CreateException e) {
			throw new CouponException("get all purchas coupons by customer failed");
		}
		Set<Coupon> purchasedCoupons = new HashSet<Coupon>();
		for (Long id : customersCoupons) {
			try {
				purchasedCoupons.add(couponDAO.getCoupon(id));
			} catch (CreateException e) {
				throw new CouponException("add coupon by customer failed");
			}
		}
		return purchasedCoupons;
	}

	/**
	 * View all customer coupon purchases history by Coupon Type
	 * @param type
	 * @return
	 * @throws CouponException
	 */
	public Set<Coupon> getAllPurchasedCouponsByType(CouponType type) throws CouponException {
		if(custId == 0) {
			throw new CouponException("the operation was canceled due to not being loged in");
		}
		Set<Coupon> coupons = getAllPurchasedCoupons();
		Set<Coupon> couponByType = new HashSet<Coupon>();
		for (Coupon coupon : coupons) {
			if (coupon.getType().name().equals(type.name())) {
				couponByType.add(coupon);
			}
		}
		return couponByType;
	}

	/**
	 * View all customer coupon purchases history be specific argument - max price
	 * requested
	 * 
	 * @param price
	 *            double
	 * @return Coupon collection
	 * @throws CouponException
	 */
	
	public Set<Coupon> getAllPurchasedCouponsByPrice(long price) throws CouponException {
		if(custId == 0) {
			throw new CouponException("the operation was canceled due to not being loged in");
		}
		Set<Coupon> coupons = getAllPurchasedCoupons();
		Set<Coupon> couponByType = new HashSet<Coupon>();
		for (Coupon coupon : coupons) {
			if (coupon.getPrice() <= price) {
				couponByType.add(coupon);
			}
		}
		return couponByType;
	}


	/**
	 * this method get possibility of customer
	 * @return Customer
	 * @throws CouponException 
	 */
	
	public Customer getCustomerInstance() throws CouponException {
		if(custId == 0) {
			throw new CouponException("the operation was canceled due to not being loged in");
		}
		return customer;
	}

	


	/**
	 * First Customer Login check
	 * @param name String 
	 * @param Password String 
	 * @throws Exception
	 **/
	
	public CouponClientFacade login(String name, String password) throws Exception {
		Customer customer = new CustomerDBDAO().login(name, password);
		if (customer != null) {
			this.custId = customer.getCustomerId();
			this.customer = customer;
			return this;
		} else {
			return null;
		}

	}
}
