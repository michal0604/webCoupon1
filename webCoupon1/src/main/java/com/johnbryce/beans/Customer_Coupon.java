package com.johnbryce.beans;

/**
 * this is the object representing the association of a coupon with a customer
 * 
 * @author Eivy & Michal
 *
 */
public class Customer_Coupon {
	private long customerId;
	private long couponId;
	

	/**
	 *  empty cTor to initiate an instance. 
	 */
	public Customer_Coupon() {}

	/**
	 * full cTor for the representation of the link between a customer and coupon
	 * 
	 * @param customerId the id of the customer
	 * @param couponId the id of the coupon
	 */
	public Customer_Coupon(long customerId, long couponId) {
		setCustomerId(customerId);
		setCouponId(couponId);
	}

	/**
	 * @return the id of the customer
	 */
	public long getCustomerId() {
		if (customerId<0) {
			System.out.println("id is invalid");
		}else {
			System.out.println("id is valid");
		}
		return customerId;
	}

	/**
	 * @param customerId id of the customer to be set.
	 */
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the id of the coupon
	 */
	public long getCouponId() {
		return couponId;
	}


	/**
	 * @param couponId id of the coupon to be set.
	 */
	public void setCouponId(long couponId) {
		this.couponId = couponId;
	}


	/**
	 *  @return a String  with the id of the customer and the associated coupon id
	 */
	@Override
	public String toString() {
		return "Customer_Coupon [customerId=" + customerId + ", couponId=" + couponId + "]";
	}

}