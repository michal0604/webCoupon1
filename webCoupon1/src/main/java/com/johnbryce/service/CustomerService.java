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

import com.johnbryce.beans.Coupon;
import com.johnbryce.beans.CouponType;
import com.johnbryce.exception.CouponException;
import com.johnbryce.facad.CompanyFacade;
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
		CustomerFacad customer=(CustomerFacad)request.getSession(false).getAttribute("facade");
		return customer;
	}
	
	@GET
	@Path("/purchaseCoupon/{couponId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String purchaseCoupon(@PathParam ("couponId")long coupId) throws Exception
	{
		CustomerFacad customer = (CustomerFacad) CouponSystem.login("customer", "customer", ClientType.CUSTOMER);
		//CustomerFacad customer= getFacad();
		Coupon coupon= new Coupon();
		try {
			coupon = customer.purchaseCoupon(coupId);
			
		}catch (CouponException e) {
			return "failed to purchase coupon"+e;
		}
		return "coupon purchase "+ coupon.getTitle();
	}

   
@GET
@Path("/getAllCouponsByType/{couponType}")
@Produces(MediaType.APPLICATION_JSON)
public Set<Coupon> getAllCouponsByType(@PathParam("couponType") CouponType couponType) throws Exception{
	CustomerFacad customer = (CustomerFacad) CouponSystem.login("customer", "customer", ClientType.CUSTOMER);
	Set<Coupon>couponByType=new HashSet<>();
	//CustomerFacad customer=getFacad();
	try {
		couponByType=customer.getAllCouponsByType(couponType);
		
	} catch (Exception e) {
		System.out.println(e);
	}
	return couponByType;
	
}

	
@GET
@Produces(MediaType.APPLICATION_JSON)
@Path("/getAllPurchasedCoupons")
public Set<Coupon> getAllPurchasedCoupons () throws Exception {
	Set<Coupon>allCoupons=new HashSet<>();
	CustomerFacad customer = (CustomerFacad) CouponSystem.login("customer", "customer", ClientType.CUSTOMER);
//	CustomerFacad customer= getFacad();
	try {
		allCoupons=customer.getAllPurchasedCoupons();
	} catch (Exception e) {
	System.out.println(e);
	}
	return allCoupons;
}

@GET
@Produces(MediaType.APPLICATION_JSON)
@Path("/getAllPurchasedCouponsByType/{type}")
public Set<Coupon> getAllPurchasedCouponsByType(@PathParam("type")CouponType type) throws Exception {
	Set<Coupon>allPurchaseCouponByType=new HashSet<>();
	CustomerFacad customer = (CustomerFacad) CouponSystem.login("customer", "customer", ClientType.CUSTOMER);
	//CustomerFacad customer=getFacad();
	try {
		allPurchaseCouponByType=customer.getAllPurchasedCouponsByType(type);
	} catch (Exception e) {
		System.out.println(e);	
	}
	return allPurchaseCouponByType;
}

@GET
@Produces(MediaType.APPLICATION_JSON)
@Path("/getAllPurchasedCouponsByPrice/{price}")
public Set<Coupon> getAllPurchasedCouponsByPrice(@PathParam("price")long price) throws Exception {
	CustomerFacad customer = (CustomerFacad) CouponSystem.login("customer", "customer", ClientType.CUSTOMER);
	//CustomerFacad customer=getFacad();
	Set<Coupon>allPurchasedCouponsByPrice=new HashSet<>();
	try {
		allPurchasedCouponsByPrice=customer.getAllPurchasedCouponsByPrice(price);
	} catch (Exception e) {
		System.out.println(e);
		
	}
	return allPurchasedCouponsByPrice;
}
}

