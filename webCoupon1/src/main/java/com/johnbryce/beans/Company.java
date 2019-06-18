package com.johnbryce.beans;

/**
 * this is the object representing Company data
 * 
 * @author Eivy & Michal
 *
 */
public class Company {

	private long companyId;
	private String compName;
	private String password;
	private String email;

	/**
	 * empty cTor for Company object instantiation 
	 */
	public Company() {
	}

	/**
	 * full cTor for the company object
	 * 
	 * @param id  company id
	 * @param compName  company name
	 * @param password  company password
	 * @param email  company email
	 */
	
	public Company(long companyId, String compName, String password, String email) {
		this(compName, password, email);
		setCompanyId(companyId);
		
		
	}

	public Company(String compName, String password, String email) {
		setCompName(compName);
		setPassword(password);
		setEmail(email);
	}

	/**
	 * @return the id of the company.
	 */
	public long getCompanyId() {
		if (companyId<0) {
		System.out.println("id is valid");
	}else {
		System.out.println("id is valid");	
	}
		return companyId;	
	}

	/**
	 * @param id Sets the id of company.
	 */
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	/**
	 * @return the company name.
	 */
	public String getCompName() {
		return compName;
	}

	/**
	 * @param compName set the company name.
	 */
	public void setCompName(String compName) {
		this.compName = compName;
	}

	/**
	 * @return the company password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password sets the password for the company.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the company email.
	 */
	public String getEmail() {
		return email;
	}


	/**
	 * @param email set the companey's email
	 */
	public void setEmail(String email) {
		this.email = email;
		
	}

	
	/**
	 * prints the content of company as a String 
	 */
	public String toString() {
		return "Company (companyId=" + companyId + ", compName=" + compName + ", password=" + password + ", email="	+ email + ")";
	}
	//
}
