package com.johnbryce.facad;

import java.sql.Date;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.johnbryce.beans.Company;
import com.johnbryce.beans.Coupon;
import com.johnbryce.beans.CouponType;
import com.johnbryce.dao.CompanyDAO;
import com.johnbryce.dao.Company_CouponDAO;
import com.johnbryce.dao.CouponDAO;
import com.johnbryce.dao.Customer_CouponDAO;
import com.johnbryce.dbdao.CompanyDBDAO;
import com.johnbryce.dbdao.Company_CouponDBDAO;
import com.johnbryce.dbdao.CouponDBDAO;
import com.johnbryce.dbdao.Customer_CouponDBDAO;
import com.johnbryce.exception.CompanyException;
import com.johnbryce.exception.CouponException;
import com.johnbryce.exception.CreateException;
import com.johnbryce.exception.RemoveException;
import com.johnbryce.exception.UpdateException;
import com.johnbryce.utils.Utile;

public class CompanyFacade implements CouponClientFacade {

	private CompanyDAO companyDAO;
	private CouponDAO couponDAO;
	private Company_CouponDAO company_CouponDAO;
	private Customer_CouponDAO customer_CouponDAO;
	// private long companyId = 0;
	private long companyId;
	private Company company;
	// private long customerId;

	/**
	 * cTor for company handling system
	 * 
	 * @throws CouponException
	 *             for errors in creation of the resources needed for the system
	 */
	public CompanyFacade() throws CouponException {
		companyDAO = new CompanyDBDAO();
		couponDAO = new CouponDBDAO();
		company_CouponDAO = new Company_CouponDBDAO();
		customer_CouponDAO = new Customer_CouponDBDAO();
	}

	/**
	 * this method returns a company iff the user password is correct.
	 * 
	 * @param name
	 *            name for login
	 * @param password
	 *            password for login
	 * @throws CouponException
	 *             for problem retrieving the company data.
	 * @throws SQLException
	 *             for DB related failures
	 * @throws CompanyException
	 * @throws ConnectionException
	 *
	 */

	@Override
	public CouponClientFacade login(String name, String password) throws Exception {
		Company company = new Company();
		company = new CompanyDBDAO().login(name, password);
		if (company != null) {
			this.companyId = company.getCompanyId();
			this.company = company;
			return this;
		} else {
			return null;
		}
	}

	/**
	 * this method adds a coupon to the system
	 * 
	 * @param coupon
	 *            the coupon to be added.
	 * @throws ConnectionException
	 *             errors due to connection problems
	 * @throws SQLException
	 *             errors due to SQL problems
	 * @throws CreateException
	 *             errors in creation
	 * @throws CouponException
	 */
	public Coupon createCoupon(Coupon coupon) throws Exception {
		if (companyId == 0) {
			System.out.println("the operation was canceled due to not being loged in");
		}
		if (coupon != null) {
			String CoupTitle = coupon.getTitle();
			if (CoupTitle != null) {
				Date startDate = (Date) coupon.getStart_date();
				Date endDate = (Date) coupon.getEnd_date();
				if (startDate.getTime() <= endDate.getTime()) {
					if (startDate.getTime() >= Utile.getCurrentDate().getTime()) {
						if (!couponDAO.isCouponTitleExists(CoupTitle)) {
							couponDAO.insertCoupon(coupon);
							long id = couponDAO.insertCoupon(coupon);
							if(id != 0) {
								coupon = couponDAO.getCoupon(id);
							}else {
								throw new CouponException("create coupon failed by company");
							}
							company_CouponDAO.insertCompany_Coupon(companyId, coupon.getCouponId());
							System.out.println("create coupon by company success!!");
							return coupon;
						} else {
							System.out.println("Coupon Title is Already Exists! Create New Coupon is Canceled!");
						}
					}
				}
			}
		}
		return coupon;
	}

	/**
	 * @param coupId
	 * @param customerId
	 * @throws CouponException
	 * @throws RemoveException
	 * @throws Exception
	 */
	public void removeCouponID(long coupId) throws Exception {
		if (companyId == 0) {
			System.out.println("the operation was canceled due to not being loged in");
		}
		if (coupId > 0) {
			if (company_CouponDAO.isCouponExistsForCompany(companyId, coupId)) {

				customer_CouponDAO.removeCustomer_CouponByCoupId(coupId);
				company_CouponDAO.removeCompany_Coupon(companyId, coupId);

				couponDAO.removeCouponID(coupId);
				System.out.println("company succsess to remove coupon!");
			} else {
				System.out.println("remove coupon failed");
			}

		}
	}

	/**
	 * @param coupon
	 * @throws CouponException
	 * @throws CreateException
	 * @throws UpdateException
	 * @throws Exception
	 */
	public Coupon updateCoupon(Coupon coupon) throws Exception {
		if (companyId == 0) {
			System.out.println("the operation was canceled due to not being loged in");
		}
		if (coupon != null) {
			long couponId = coupon.getCouponId();
			if (company_CouponDAO.isCouponExistsForCompany(companyId, couponId)) {
				Double CoupPrice = coupon.getPrice();
				if (CoupPrice > 0) {
					Date startDate = (Date) couponDAO.getCoupon(couponId).getStart_date();
					Date endDate = (Date) coupon.getEnd_date();
					if (startDate.getTime() <= endDate.getTime()) {
						couponDAO.updateCoupon(coupon);
						long id = couponDAO.insertCoupon(coupon);
						if(id != 0) {
							System.out.println("update coupon by company succsess!!");
							return couponDAO.getCoupon(id);
						}else {
							throw new CouponException("Update Coupon failed by company!");
						}

					} else {
						System.out.println(" Update Coupon failed by company!");
					}

				}
			}
		}
		return coupon;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public Company getCompany() throws Exception {
		return companyDAO.getCompany(companyId);

	}

	/**
	 * @param coupId
	 * @return
	 * @throws CouponException
	 * @throws CreateException
	 * @throws Exception
	 */
	public Coupon getCoupon(long coupId) throws Exception {
		if (companyId == 0) {
			System.out.println("the operation was canceled due to not being loged in");
		}
		return couponDAO.getCoupon(coupId);
	}

	/**
	 * @return
	 * @throws CouponException
	 * @throws Exception
	 */
	public Set<Coupon> getCoupons() throws Exception {
		if (companyId == 0) {
			System.out.println("the operation was canceled due to not being loged in");
		}
		Set<Coupon> allCoupons = new HashSet<Coupon>();
		allCoupons = couponDAO.getAllCoupons();
		return allCoupons;
	}

	/**
	 * @param coupType
	 * @return
	 * @throws CouponException
	 * @throws CompanyException
	 * @throws CreateException
	 * @throws Exception
	 */

	public Set<Coupon> getAllCouponsByType(CouponType coupType) throws Exception {
		if (companyId == 0) {
			System.out.println("the operation was canceled due to not being loged in");
		}
		Set<Coupon> list = new HashSet<Coupon>();
		Set<Coupon> allCouponsList = companyDAO.getAllCoupons(companyId);
		for (Coupon coupon : allCouponsList) {
			if (coupon.getType().equals(coupType)) {
				list.add(coupon);
			}
		}
		return list;
	}

	/**
	 * @param price
	 * @return
	 * @throws CouponException
	 * @throws CreateException
	 * @throws CompanyException
	 * @throws Exception
	 */

	public Set<Coupon> getCouponsByMaxCouponPrice(double price) throws Exception {
		if (companyId == 0) {
			System.out.println("the operation was canceled due to not being loged in");
		}
		Set<Coupon> list = new HashSet<Coupon>();
		Set<Coupon> allCouponsList = companyDAO.getAllCoupons(companyId);
		for (Coupon coupon : allCouponsList) {

			if (coupon.getPrice() <= price) {
				list.add(coupon);
			}
		}
		return list;
	}

	/**
	 * @param endDate
	 * @return
	 * @throws CouponException
	 * @throws CreateException
	 * @throws CompanyException
	 * @throws Exception
	 */

	public Set<Coupon> getCouponsByMaxCouponDate(Date endDate) throws Exception {
		if (companyId == 0) {
			System.out.println("the operation was canceled due to not being loged in");
		}
		Set<Coupon> list = new HashSet<Coupon>();
		Set<Coupon> allCouponsList = companyDAO.getAllCoupons(companyId);
		for (Coupon coupon : allCouponsList) {
			if (coupon.getEnd_date().equals(endDate) || coupon.getEnd_date().before(endDate)) {
				list.add(coupon);
			}
		}
		return list;
	}

	/**
	 * @return
	 * @throws CouponException
	 */
	public Company getCompanyInstance() throws CouponException {
		if (companyId == 0) {
			throw new CouponException("the operation was canceled due to not being loged in");
		}
		return company;
	}

}
