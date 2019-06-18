package com.johnbryce.beans;

/**
 * this is the object representing the association of a coupon with a company
 * 
 * @author Michal & Eivy
 *
 */
public class Company_Coupon {
	private long companyId;
	private long couponId;
	
	
	/**
	 *  empty cTor to initiate an instance. 
	 */
	public Company_Coupon() {}


	/**
	 * full cTor for the representation of the link between a company and coupon
	 * 
	 * @param companyId the id of the company
	 * @param couponId the id of the coupon
	 */
	public Company_Coupon(long companyId, long couponId) {
	    setCompanyId(companyId);
		setCouponId(couponId);
		
	}


	/**
	 * @return the id of the company
	 */
	public long getCompanyId() {
		if (companyId<0) {
			System.out.println("id is invalid");
		}else {
			System.out.println("id is valid");
		}
		return companyId;
	}


	/**
	 * @param companyId id of the company to be set.
	 */
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
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
	 *  @return a String  with the id of the company and the associated coupon id
	 */
	@Override
	public String toString() {
		return "Company_Coupon [companyId=" + companyId + ", couponId=" + couponId + "]";
	}
	

}