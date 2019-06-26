package com.johnbryce.service;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.johnbryce.beans.Coupon;
import com.johnbryce.beans.CouponType;
import com.johnbryce.exception.CouponException;
import com.johnbryce.facad.CouponClientFacade;
import com.johnbryce.facad.CustomerFacad;
import com.johnbryce.utils.ClientType;
import com.johnbryce.utils.CouponSystem;

@Path("customer")
public class CustomerService {
	@Context
	private HttpServletRequest request;
	@Context
	private HttpServletResponse response;

	private CustomerFacad getFacad() {
		CustomerFacad customer = (CustomerFacad) request.getSession(false).getAttribute("facade");
		return customer;
	}

	@GET
	@Path("/login")
	@Produces(MediaType.TEXT_PLAIN)
	public String login(@QueryParam("name") String name, @QueryParam("pass") String password) {
		try {
			CouponClientFacade facad = CouponSystem.login(name, password, ClientType.CUSTOMER);
			if (facad != null) {
				request.getSession(true).setAttribute("facade", facad);
				return "succesfull login";
			} else {
				return "faild login - wrong user or password";
			}
		} catch (Exception e) {
			return "faild login " + e.getMessage();
		}

	}

	@GET
	@Path("purchaseCoupon/{coupid}")
	@Produces(MediaType.APPLICATION_JSON)
	public String purchaseCoupon(@PathParam("coupId") long coupId) {
		CustomerFacad customer = getFacad();
		Coupon coupon = new Coupon();
		try {
			customer.purchaseCoupon(coupId);

		} catch (CouponException e) {
			return "failed to purchase coupon" + e;
		}
		return new Gson().toJson("coupon purchase" + coupon.getTitle());
	}

	@GET
	@Path("/getAllCouponsByType/{couponType}")
	@Consumes(MediaType.APPLICATION_JSON)
	public String getAllCouponsByType(@PathParam("couponType") CouponType couponType) {
		try {
			Set<Coupon> couponByType = new HashSet<>();
			CustomerFacad customer = getFacad();

			return new Gson().toJson(customer.getAllCouponsByType(couponType));

		} catch (Exception e) {
			return "faild "+e.getMessage();
		}

	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getAllPurchasedCoupons")
	public Set<Coupon> getAllPurchasedCoupons() {
		Set<Coupon> allCoupons = new HashSet<>();
		CustomerFacad customer = getFacad();
		try {
			allCoupons = customer.getAllPurchasedCoupons();
		} catch (Exception e) {
			System.out.println(e);
		}
		return allCoupons;
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getAllPurchasedCouponsByType")
	public Set<Coupon> getAllPurchasedCouponsByType(@PathParam("type") CouponType type) {
		Set<Coupon> allPurchaseCouponByType = new HashSet<>();
		CustomerFacad customer = getFacad();
		try {
			allPurchaseCouponByType = customer.getAllCouponsByType(type);
		} catch (Exception e) {
			System.out.println(e);
		}
		return allPurchaseCouponByType;
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getAllPurchasedCouponsByPrice")
	public Set<Coupon> getAllPurchasedCouponsByPrice(@PathParam("price") long price) {
		CustomerFacad customer = getFacad();
		Set<Coupon> allPurchasedCouponsByPrice = new HashSet<>();
		try {
			allPurchasedCouponsByPrice = customer.getAllPurchasedCouponsByPrice(price);
		} catch (Exception e) {
			System.out.println(e);

		}
		return allPurchasedCouponsByPrice;
	}
}
