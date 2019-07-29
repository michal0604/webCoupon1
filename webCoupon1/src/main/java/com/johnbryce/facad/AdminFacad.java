package com.johnbryce.facad;

import java.sql.SQLException;
import java.util.Set;

import com.johnbryce.beans.Company;
import com.johnbryce.beans.Customer;
import com.johnbryce.dao.CompanyDAO;
import com.johnbryce.dao.Company_CouponDAO;
import com.johnbryce.dao.CustomerDAO;
import com.johnbryce.dao.Customer_CouponDAO;
import com.johnbryce.dbdao.CompanyDBDAO;
import com.johnbryce.dbdao.Company_CouponDBDAO;
import com.johnbryce.dbdao.CustomerDBDAO;
import com.johnbryce.dbdao.Customer_CouponDBDAO;
import com.johnbryce.exception.CompanyException;
import com.johnbryce.exception.CouponException;
import com.johnbryce.exception.CreateException;
import com.johnbryce.exception.CustomerException;
import com.johnbryce.exception.RemoveException;
import com.johnbryce.exception.UpdateException;
import com.johnbryce.utils.DataBase;

/**
 * @author Eivy & Michal
 * 
 *         Administrator Facade
 *
 */
public class AdminFacad implements CouponClientFacade {
	private static final String ADMIN_USER_NAME = "admin";
	private static final String ADMIN_PASSWORD = "1234";
	private CompanyDAO companyDAO;
	private CustomerDAO customerDAO;
	private Company_CouponDAO company_CouponDAO;
	private Customer_CouponDAO customer_CouponDAO;
	private boolean isLogedIn = false;

	/**
	 * Constructor
	 * 
	 * @throws CouponException
	 */
	public AdminFacad() throws CouponException {
		this.companyDAO = new CompanyDBDAO();
		this.customerDAO = new CustomerDBDAO();
		this.customer_CouponDAO = new Customer_CouponDBDAO();
		this.company_CouponDAO = new Company_CouponDBDAO();
	}

	/**
	 * this method check password of admin, if true return admin.
	 * 
	 * @param name
	 *            String
	 * @param password
	 *            String
	 *
	 */

	public CouponClientFacade login(String name, String password) {
		if (name.equals(AdminFacad.ADMIN_USER_NAME) && password.equals(AdminFacad.ADMIN_PASSWORD)) {
			this.isLogedIn = true;
			return this;
		}

		return null;
	}

	/**
	 * this method create a company, at first need to check if company exist.
	 * 
	 * @param company
	 * @throws CouponException
	 */
	public Company createCompany(Company company) throws CouponException {
		if (!isLogedIn) {
			throw new CouponException("the operation was canceled due to not being loged in");
		}
		System.out.println(company.toString());
		if (company != null) {
			String compName = company.getCompName();
			if (compName != null) {
				if (company.getPassword() != null) {
					try {
						if (!companyDAO.isCompanyNameExists(compName)) {
							long id = companyDAO.insertCompany(company);

							if (id != 0) {
								return companyDAO.getCompany(id);
							} else {
								throw new CouponException("create company failed by admin id =0");
							}

						} else {
							throw new CouponException("create company failed by company name");
						}
					} catch (SQLException e) {
						throw new CouponException("create company failed by admin " + e.getMessage());
					}
				}
			}
		}
		return company;
	}

	/**
	 * This method get Company as object and take all its coupons and delete all the
	 * coupons from Company_Coupon table & Customer_Coupon table & Coupon table. in
	 * addition, we delete the company in the Company table.
	 * 
	 * @param company
	 * @throws CouponException
	 */
	public void removeCompany(Company company) throws CouponException {
		if (!isLogedIn) {
			throw new CouponException("the operation was canceled due to not being loged in");
		}
		try {
			Set<Long> coupoIdList = company_CouponDAO.getCouponsByCompanyId(company.getCompanyId());
			company_CouponDAO.removeCompany_Coupon(company);
			for (Long couponId : coupoIdList) {
				customer_CouponDAO.removeCustomer_CouponByCoupId(couponId);
			}
			companyDAO.removeCompany(company);
		} catch (RemoveException e) {
			throw new CouponException("remove company_coupon failed");
		} catch (SQLException e) {
			throw new CouponException("remove company_coupon failed");
		}

	}

	/**
	 * This method get Company as object and send it to method that update line in
	 * the Company table. The method don't update the name.
	 * 
	 * @param company
	 * @param newpassword
	 * @param newEmail
	 * @throws CouponException
	 */
	public Company updateCompany(Company company, String newpassword, String newEmail) throws CouponException {
		if (!isLogedIn) {
			throw new CouponException("the operation was canceled due to not being loged in");
		}

		company.setPassword(newpassword);
		company.setEmail(newEmail);
		try {
			companyDAO.updateCompany(company);
			return company;

		} catch (CompanyException e) {
			throw new CouponException("update company by admin failed");
		}

	}

	/**
	 * This method get company id and return the line from Company table as Company
	 * object.
	 * 
	 * @param id
	 * @return
	 * @throws CouponException
	 */
	public Company getCompany(long id) throws CouponException {
		if (!isLogedIn) {
			throw new CouponException("the operation was canceled due to not being loged in");
		}

		try {
			return companyDAO.getCompany(id);
		} catch (SQLException e) {
			throw new CouponException("get company by admin failed");
		}

	}

	/**
	 * This method return all the Companies as a list.
	 * 
	 * @return
	 * @throws CouponException
	 */
	public Set<Company> getAllCompanies() throws CouponException {
		if (!isLogedIn) {
			throw new CouponException("the operation was canceled due to not being loged in");
		}
		try {
			return companyDAO.getAllCompanys();
		} catch (SQLException e) {
			throw new CouponException("get all company by admin failed");

		}
	}

	/**
	 * This method get Customer as object and send it to create line in Customer
	 * table. first we check that the name not exist.
	 * 
	 * @param customer
	 * @throws CouponException
	 */
	public Customer createCustomer(Customer customer) throws CouponException {
		if (!isLogedIn) {
			throw new CouponException("the operation was canceled due to not being loged in");
		}
		if (customer != null) {
			String custName = customer.getCustomerName();
			if (custName != null) {
				if (customer.getPassword() != null) {
					try {
						if (!customerDAO.isCustomerNameExists(custName)) {
							long id = customerDAO.insertCustomer(customer);
							if (id != 0) {
								return customerDAO.getCustomer(id);
							} else {
								throw new CouponException("create customer failed by admin");
							}

						}else {
							throw new CouponException("A customer with the same name already exist");
						}
					} catch (CustomerException e) {
						throw new CouponException(e.getMessage());

					} catch (CreateException e) {
						throw new CouponException(e.getMessage());
					}
				}else {
					throw new CouponException("the customer has a null password");
				}
			}else {
				throw new CouponException("the customer user name is empty");
			}
		}
		return customer;
	}

	/**
	 * This method get Customer as object and take all its coupons and delete all
	 * the lines with those coupons from Customer_Coupon table and the line from
	 * Customer table
	 * 
	 * @param customer
	 * @throws CouponException
	 */
	public void removeCustomer(Customer customer) throws CouponException {
		if (!isLogedIn) {
			throw new CouponException("the operation was canceled due to not being loged in");
		}
		try {
			customer_CouponDAO.removeCustomer_Coupon(customer);
		} catch (RemoveException e) {
			throw new CouponException("remove customer_coupon by admin failed");
		}
		try {
			customerDAO.removeCustomer(customer);
		} catch (RemoveException e) {
			throw new CouponException("remove customer by admin failed");
		}

	}

	/**
	 * This method get Customer,and new password as object and send it to method
	 * that update line in the Customer table. The method don't update the name
	 * 
	 * @param customer
	 * @param newpassword
	 * @throws CouponException
	 */
	public Customer updateCustomer(Customer customer, String newpassword) throws CouponException {
		if (!isLogedIn) {
			throw new CouponException("the operation was canceled due to not being loged in");
		}
		customer.setPassword(newpassword);
		try {
			customerDAO.updateCustomer(customer);
			return customer;
		} catch (UpdateException e) {
			throw new CouponException("update customer by admin failed");
		}

	}

	/**
	 * This method return all the Customers as a list.
	 * 
	 * @return
	 * @throws CouponException
	 */
	public Set<Customer> getAllCustomers() throws CouponException {
		if (!isLogedIn) {
			throw new CouponException("the operation was canceled due to not being loged in");
		}
		try {
			return customerDAO.getAllCustomers();
		} catch (CustomerException e) {
			throw new CouponException("get all customers by admin failed");
		}
	}

	/**
	 * This method get Customer id and return the line from Customer table as
	 * Customer object.
	 * 
	 * @param id
	 * @return
	 * @throws CouponException
	 */
	public Customer getCustomer(long id) throws CouponException {
		if (!isLogedIn) {
			throw new CouponException("the operation was canceled due to not being loged in");
		}
		try {
			return customerDAO.getCustomer(id);
		} catch (CustomerException e) {
			throw new CouponException("get customer by admin failed");
		}
	}

	public void rebuildDb() throws CouponException, RemoveException, SQLException, CreateException {
		if (!isLogedIn) {
			throw new CouponException("the operation was canceled due to not being loged in");
		}
		DataBase.dropTableifNeeded();
		DataBase.createTables();
	}

}