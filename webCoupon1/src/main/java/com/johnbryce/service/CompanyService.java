package com.johnbryce.service;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.johnbryce.beans.Company;
import com.johnbryce.beans.Coupon;
import com.johnbryce.beans.CouponType;
import com.johnbryce.facad.AdminFacad;
import com.johnbryce.facad.CompanyFacade;
import com.johnbryce.utils.ClientType;
import com.johnbryce.utils.CouponSystem;

@Path("company")
public class CompanyService {

	@Context
	private HttpServletRequest request;
	@Context
	private HttpServletResponse response;

	private CompanyFacade getFacade() {

		CompanyFacade company = (CompanyFacade) request.getSession(false).getAttribute("facade");
		return company;
	}

	@POST
	@Path("createCoupon")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String createCoupon(Coupon coupon) throws Exception {
		CompanyFacade company = (CompanyFacade) CouponSystem.login("company", "company", ClientType.COMPANY);
		// CompanyFacade companyFacade = getFacade();
		try {
			System.out.println("the coupon is ");
			coupon = company.createCoupon(coupon);
			System.out.println(coupon);
			return new Gson().toJson(coupon);
		} catch (Exception e) {
			return "Failed to Add a new coupon:" + e.getMessage();
		}

	}

	@DELETE
	@Path("removeCoupon/{couponID}")
	@Produces(MediaType.TEXT_PLAIN)
	public String removeCompany(@PathParam("couponID") long id) throws Exception {
		CompanyFacade companyFacade = (CompanyFacade) CouponSystem.login("company", "company", ClientType.COMPANY);
		//CompanyFacade companyFacade = getFacade();
		try {
			companyFacade.removeCouponID(id);
			return "Succeded to remove a coupon:  id = " + id;
		} catch (Exception e) {
			return "Failed to remove a coupon: " + e.getMessage();
		}

	}

	@PUT
	@Path("updateCoupon")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String updateCoupon(Coupon coupon) throws Exception {
		CompanyFacade companyFacade = (CompanyFacade) CouponSystem.login("company", "company", ClientType.COMPANY);
		//CompanyFacade companyFacade = getFacade();
		try {
			if (coupon != null) {
				Coupon oldCoupon = companyFacade.getCoupon(coupon.getCouponId());
				oldCoupon.setEnd_date(coupon.getEnd_date());
				oldCoupon.setPrice(coupon.getPrice());
				oldCoupon = companyFacade.updateCoupon(oldCoupon);
				return new Gson().toJson(oldCoupon);
			} else {
				return "Failed to update a company: the provided company id is invalid";
			}
		} catch (Exception e) {
			return "Failed to update a company: " + e.getMessage();
		}

	}

	@GET
	@Path("getCompany")
	@Produces(MediaType.APPLICATION_JSON)
	public String getCompany() {
		CompanyFacade companyFacade = getFacade();
		Company company;
		try {
			company = companyFacade.getCompany();
		} catch (Exception e) {
			System.err.println("Get Company failed: " + e.getMessage());
			company = new Company();
		}

		return new Gson().toJson(company);
	}

	@GET
	@Path("getCoupon/{couponId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getCoupon(@PathParam("couponId") long id) throws Exception {
		CompanyFacade companyFacade = (CompanyFacade) CouponSystem.login("company", "company", ClientType.COMPANY);
		//CompanyFacade companyFacade = getFacade();
		try {
			Coupon coupon = companyFacade.getCoupon(id);
			if (coupon != null) {
				return new Gson().toJson(coupon);
			} else {
				return null;
			}
		} catch (Exception e) {
			System.err.println("get coupon by id failed " + e.getMessage());
			return null;
		}
	}

	@GET
	@Path("getCoupons")
	@Produces(MediaType.APPLICATION_JSON)
	public String getCoupons() throws Exception {
		CompanyFacade company = (CompanyFacade) CouponSystem.login("company", "company", ClientType.COMPANY);
		// CompanyFacade companyFacade = getFacade();
		Set<Coupon> coupons;

		try {
			coupons = company.getCoupons();
			System.out.println(coupons);
		} catch (Exception e) {
			System.err.println("Get Coupons failed: " + e.getMessage());
			coupons = new HashSet<Coupon>();
		}
		return new Gson().toJson(coupons);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getAllCouponsByType/{type}")
	public String getAllCouponsByType(@PathParam("type") CouponType type) throws Exception {
		CompanyFacade company = (CompanyFacade) CouponSystem.login("company", "company", ClientType.COMPANY);
		// CompanyFacade companyFacade = getFacade();
		Set<Coupon> allCouponsByType = new HashSet<>();
		try {
			allCouponsByType = company.getAllCouponsByType(type);
		} catch (Exception e) {
			System.err.println("Get Coupons by type failed: " + e.getMessage());
			allCouponsByType = new HashSet<Coupon>();
		}
		return new Gson().toJson(allCouponsByType);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getCouponsByMaxCouponPrice/{price}")
	public String getCouponsByMaxCouponPrice(@PathParam("price") double price) throws Exception {
		CompanyFacade companyFacade = (CompanyFacade) CouponSystem.login("company", "company", ClientType.COMPANY);
		//CompanyFacade companyFacade = getFacade();
		Set<Coupon> allCouponsByType = new HashSet<>();
		try {
			allCouponsByType = companyFacade.getCouponsByMaxCouponPrice(price);
		} catch (Exception e) {
			System.err.println("Get Coupons by max price failed: " + e.getMessage());
			allCouponsByType = new HashSet<Coupon>();
		}
		return new Gson().toJson(allCouponsByType);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getCouponsByMaxCouponDate")
	public String getCouponsByMaxCouponDate(Coupon date) throws Exception {
		CompanyFacade companyFacade = (CompanyFacade) CouponSystem.login("company", "company", ClientType.COMPANY);
		//CompanyFacade companyFacade = getFacade();
		Set<Coupon> allCouponsByType = new HashSet<>();
		try {
			allCouponsByType = companyFacade.getCouponsByMaxCouponDate(date.getEnd_date());
		} catch (Exception e) {
			System.err.println("Get Coupons by max date failed: " + e.getMessage());
			allCouponsByType = new HashSet<Coupon>();
		}
		return new Gson().toJson(allCouponsByType);
	}

}