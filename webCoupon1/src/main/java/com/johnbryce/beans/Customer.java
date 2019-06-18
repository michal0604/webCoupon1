package com.johnbryce.beans;

/**
 * this is the object representing customer data
 * 
 * @author Eivy & Michal
 *
 */
public class Customer {

	private long customerId;
	private String customerName;
	private String password;

	/**
	 * empty cTor for customer object instantiation 
	 */
	public Customer() {}

	/**
	 * full cTor for the customer object
	 * 
	 * @param customerId the id of the customer
	 * @param customerName customer user name
	 * @param password cusyomer password
	 */
	public Customer(long customerId, String customerName, String password) {
		setCustomerId(customerId);
		setCustomerName(customerName);
		setPassword(password);
	}

	public Customer(String custName, String password) {
		setCustomerName(customerName);
		setPassword(password);
	}

	/**
	 * @return the customer id
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
	 * @return the customer user name
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * @param customerName sets the customer's user name
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * @return customer's password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password sets the password for the customer
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * prints the content of customer as a String 
	 */
	@Override
	public String toString() {
		return "Customer [customerID=" + customerId + ", customerName=" + customerName + ", password=" + password + "]";
	}

	/**
	 * @param customerId set's the customer's id
	 */
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

}
