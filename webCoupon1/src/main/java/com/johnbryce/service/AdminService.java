package com.johnbryce.service;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.johnbryce.beans.Company;
import com.johnbryce.beans.Customer;
import com.johnbryce.exception.CouponException;
import com.johnbryce.facad.AdminFacad;
import com.johnbryce.facad.CouponClientFacade;
import com.johnbryce.utils.ClientType;
import com.johnbryce.utils.CouponSystem;

@Path("/admin")
public class AdminService {

	@Context
	private HttpServletRequest request;
	@Context
	private HttpServletResponse response;

	private AdminFacad getFacade() {
		AdminFacad admin = (AdminFacad) request.getSession(false).getAttribute("facade");
		return admin;
	}

	@GET
	@Path("/login")
	@Produces(MediaType.TEXT_PLAIN)
	public String login(@QueryParam("name") String name, @QueryParam("pass") String password) {
		try {
			
			CouponClientFacade facad = CouponSystem.login(name, password,ClientType.ADMIN);
			if(facad != null) {
				request.getSession(true).setAttribute("facade", facad);
				return "succesfull login";
			}
			else {
				return "faild login - wrong user or password";
			}
		} catch (Exception e) {
			return "faild login "+ e.getMessage();
		}

	}

	@POST
	@Path("createCompany")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String createCompany(Company company) {

		AdminFacad admin = getFacade();
		try {
			company = admin.createCompany(company);
			return new Gson().toJson(company);
		} catch (CouponException e) {
			return "Failed to Add a new Company:" + e.getMessage();
		}

	}

	@DELETE
	@Path("removeCompany")
	@Produces(MediaType.TEXT_PLAIN)
	public String removeCompany(@PathParam("compId") long id) {

		AdminFacad admin = getFacade();
		try {
			Company company = admin.getCompany(id);
			if (company != null) {
				admin.removeCompany(company);
				return "Succeded to remove a company: name = " + company.getCompName() + ", id = " + id;
			} else {
				return "Failed to remove a company: the provided company id is invalid";
			}
		} catch (CouponException e) {
			return "Failed to remove a company: " + e.getMessage();
		}

	}

	@PUT
	@Path("updateCompany")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String updateCompany(Company newCompany) {

		AdminFacad admin = getFacade();
		try {
			Company oldCompany = admin.getCompany(newCompany.getCompanyId());
			if (oldCompany != null) {
				newCompany = admin.updateCompany(oldCompany, newCompany.getPassword(), newCompany.getEmail());
				return new Gson().toJson(newCompany);
			} else {
				return "Failed to update a company: the provided company id is invalid";
			}
		} catch (CouponException e) {
			return "Failed to update a company: " + e.getMessage();
		}

	}

	@GET
	@Path("getAllCompanies")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllCompanies() {
		AdminFacad admin = getFacade();
		Set<Company> companies;
		try {
			companies = admin.getAllCompanies();
		} catch (CouponException e) {
			System.err.println("Get all Companies failed: " + e.getMessage());
			companies = new HashSet<Company>();
		}
		return new Gson().toJson(companies);
	}

	@GET
	@Path("getCompany")
	@Produces(MediaType.APPLICATION_JSON)
	public String getCompany(@PathParam("compId") long id) {
		AdminFacad admin = getFacade();
		try {
			Company company = admin.getCompany(id);
			if (company != null) {
				return new Gson().toJson(company);
			} else {
				return null;
			}
		} catch (CouponException e) {
			System.err.println("get company by id failed " + e.getMessage());
			return null;
		}
	}

	@POST
	@Path("createCustomer")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String Customer(Customer customer) {

		AdminFacad admin = getFacade();
		try {
			customer = admin.createCustomer(customer);
			return new Gson().toJson(customer);
		} catch (CouponException e) {
			return "Failed to Add a new customer:" + e.getMessage();
		}
	}

	@GET
	@Path("removeCustomer")
	@Produces(MediaType.TEXT_PLAIN)
	public String removeCustomer(@QueryParam("custId") long id) {

		AdminFacad admin = getFacade();

		try {
			Customer customer = admin.getCustomer(id);
			if (customer != null) {
				admin.removeCustomer(customer);
				return "Succeded to remove a customer: name = " + customer.getCustomerName();
			} else {
				return "Failed to remove a customer: the provided customer id is invalid";
			}
		} catch (CouponException e) {
			return "Failed to remove a customer: " + e.getMessage();
		}

	}

	@PUT
	@Path("updateCustomer")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String updateCustomer(Customer newCostomer) {
		AdminFacad admin = getFacade();
		try {
			Customer oldCustomer = admin.getCustomer(newCostomer.getCustomerId());
			if (oldCustomer != null) {
				newCostomer = admin.updateCustomer(oldCustomer, newCostomer.getPassword());
				return new Gson().toJson(newCostomer);
			} else {
				return "Failed to update a customer: the provided customer id is invalid";
			}
		} catch (CouponException e) {
			return "Failed to update a customer: " + e.getMessage();
		}
	}

	@GET
	@Path("getAllCustomers")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllCustomers() {
		AdminFacad admin = getFacade();
		Set<Customer> customers;
		try {
			customers = admin.getAllCustomers();
		} catch (CouponException e) {
			System.err.println("Get all customers failed: " + e.getMessage());
			customers = new HashSet<Customer>();
		}
		return new Gson().toJson(customers);
	}

	@GET
	@Path("getCustomer")
	@Produces(MediaType.APPLICATION_JSON)
	public String getCustomer(@QueryParam("custId") long id) {
		AdminFacad admin = getFacade();
		try {
			Customer customer = admin.getCustomer(id);
			if (customer != null) {
				return new Gson().toJson(customer);
			} else {
				return null;
			}
		} catch (CouponException e) {
			System.err.println("get customer by id failed " + e.getMessage());
			return null;
		}
	}

}