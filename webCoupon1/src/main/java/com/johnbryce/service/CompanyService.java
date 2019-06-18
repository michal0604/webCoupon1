package com.johnbryce.service;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.johnbryce.beans.Company;
import com.johnbryce.beans.Coupon;
import com.johnbryce.beans.CouponType;
import com.johnbryce.facad.CompanyFacade;

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
	public String createCoupon(Coupon coupon) {
		CompanyFacade companyFacade = getFacade();
		try {
			coupon = companyFacade.createCoupon(coupon);
			return new Gson().toJson(coupon);
		} catch (Exception e) {
			return "Failed to Add a new coupon:" + e.getMessage();
		}

	}

	@GET
	@Path("removeCompany")
	@Produces(MediaType.TEXT_PLAIN)
	public String removeCompany(@PathParam("compId") long id) {

		CompanyFacade companyFacade = getFacade();
		try {
			companyFacade.removeCouponID(id);
			return "Succeded to remove a coupon:  id = " + id;
		} catch (Exception e) {
			return "Failed to remove a coupon: " + e.getMessage();
		}

	}

	@POST
	@Path("updateCoupon")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String updateCoupon(Coupon coupon) {

		CompanyFacade companyFacade = getFacade();
		try {
			if (coupon != null) {
				Coupon oldCoupon = companyFacade.getCoupon(coupon.getCouponId());
				oldCoupon.setTitle(coupon.getTitle());
				oldCoupon.setStart_date(coupon.getStart_date());
				oldCoupon.setEnd_date(coupon.getEnd_date());
				oldCoupon.setAmount(coupon.getAmount());
				oldCoupon.setType(coupon.getType());
				oldCoupon.setMessage(coupon.getMessage());
				oldCoupon.setPrice(coupon.getPrice());
				oldCoupon.setImage(coupon.getImage());
				oldCoupon = companyFacade.updateCoupon(oldCoupon);
				return new Gson().toJson(coupon);
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
	@Path("getCoupon")
	@Produces(MediaType.APPLICATION_JSON)
	public String getCoupon(@PathParam("compId") long id) {
		CompanyFacade companyFacade = getFacade();
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
	public String getCoupons() {
		CompanyFacade companyFacade = getFacade();
		Set<Coupon> coupons;
		try {
			coupons = companyFacade.getCoupons();
		} catch (Exception e) {
			System.err.println("Get Coupons failed: " + e.getMessage());
			coupons = new HashSet<Coupon>();
		}
		return new Gson().toJson(coupons);
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getAllCouponsByType")
	public String getAllCouponsByType(@PathParam("type")CouponType type) {
		CompanyFacade companyFacade = getFacade();
		Set<Coupon>  allCouponsByType=new HashSet<>();
		try {
			allCouponsByType=companyFacade.getAllCouponsByType(type);
		} catch (Exception e) {
			System.err.println("Get Coupons by type failed: " + e.getMessage());
			allCouponsByType = new HashSet<Coupon>();
		}
		return new Gson().toJson(allCouponsByType);
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getCouponsByMaxCouponPrice")
	public String getCouponsByMaxCouponPrice(@PathParam("price") double price) {
		CompanyFacade companyFacade = getFacade();
		Set<Coupon>  allCouponsByType=new HashSet<>();
		try {
			allCouponsByType=companyFacade.getCouponsByMaxCouponPrice(price);
		} catch (Exception e) {
			System.err.println("Get Coupons by max price failed: " + e.getMessage());
			allCouponsByType = new HashSet<Coupon>();
		}
		return new Gson().toJson(allCouponsByType);
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getCouponsByMaxCouponDate")
	public String getCouponsByMaxCouponDate(@PathParam("date") Date date) {
		CompanyFacade companyFacade = getFacade();
		Set<Coupon>  allCouponsByType=new HashSet<>();
		try {
			allCouponsByType=companyFacade.getCouponsByMaxCouponDate(date);
		} catch (Exception e) {
			System.err.println("Get Coupons by max dare failed: " + e.getMessage());
			allCouponsByType = new HashSet<Coupon>();
		}
		return new Gson().toJson(allCouponsByType);
	}


}