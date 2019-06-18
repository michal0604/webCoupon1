package com.johnbryce.utils;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.johnbryce.exception.CouponException;
import com.johnbryce.exception.CreateException;
import com.johnbryce.exception.RemoveException;


/**
 * @author Eivy & Michal
 * 
 *   Get driver & URL  and Connect to DB
 *
 */
public class DataBase {

	public static String getDriverData() {
		return "org.apache.derby.jdbc.ClientDriver";
	}

	public static String getDBUrl() {
		return "jdbc:derby://localhost:3301/JBDB;create=true";
	}

	private static ConnectionPool Pool;

	public DataBase() throws CouponException, SQLException {
		Pool = ConnectionPool.getInstance();
	}

	/**
	 * this method drops all tables :company,coupon,customer,company_coupon,cutomer_coupon.
	 * @throws RemoveException
	 * @throws SQLException
	 */
	public static void dropTableifNeeded() throws RemoveException, SQLException{
		String sql;

		try {
			Pool=ConnectionPool.getInstance();
		} catch (Exception e2) {
			throw new RemoveException("connection failed");
		}
		Connection connection;
		try {
			connection = Pool.getConnection();
		} catch (Exception e2) {
			throw new RemoveException("connection failed");
		}
	
		sql = "DROP TABLE Customer_Coupon";
		try {
			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.executeUpdate();
			System.out.println("Droped Customer_Coupon Table");
		} catch (Exception e) {
			//throw new RemoveException("Customer_Coupon Table did not drop: " + e.getMessage());
		}
		sql = "DROP TABLE Company_Coupon";
		try {
			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.executeUpdate();
			System.out.println("Droped Company_Coupon Table");
		} catch (Exception e) {
			//throw new RemoveException("Company_Coupon Table did not drop: " + e.getMessage());
		}

		sql = "DROP TABLE Company";
		
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.executeUpdate();
			System.out.println("Droped Company Table");
		} catch (Exception e) {
			//throw new RemoveException("Company Table did not drop: " + e.getMessage());
		}
		sql = "DROP TABLE CUSTOMER";
		try {	
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.executeUpdate();
			System.out.println("Droped Customer Table");
		} catch (Exception e) {
			//throw new RemoveException("Customer Table did not drop: " + e.getMessage());
		}
		sql = "DROP TABLE Coupon";
		
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.executeUpdate();
			System.out.println("Droped COUPON Table");
		} catch (Exception e) {
			//throw new RemoveException("COUPON Table did not exist");
			
		}
        finally {
			connection.close();
			try {
				Pool.returnConnection(connection);
			} catch (CouponException e) {
				throw new RemoveException("connection failed");

			}
        }
	}

		
	

	/**this method create all tables:company,coupon,customer,company_coupon,cutomer_coupon.
	 * @throws SQLException
	 * @throws CreateException
	 */

	public static void createTables() throws SQLException, CreateException {

		String sql;
		
		try {
			Pool=ConnectionPool.getInstance();
		} catch (CouponException e2) {
			throw new CreateException("connection failed");
		}
		Connection connection;
		try {
			connection = Pool.getConnection();
		} catch (Exception e2) {
			throw new CreateException("connection failed");
		}
		
		// create Company table
		 sql = "create table Company (" + "ID bigint not null primary key generated always as identity(start with 1, increment by 1), " + "COMP_NAME varchar(50) not null, "
				+ "PASSWORD varchar(50) not null, " + "EMAIL varchar(50) not null)";
		
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.executeUpdate();
			System.out.println("success:" + sql);
		} catch (SQLException e) {
			throw new CreateException("create company didn't succeed");
		}
		sql = "create table Customer (" + "ID bigint not null primary key generated always as identity(start with 1, increment by 1), " + "CUST_NAME varchar(50) not null, "
				+ "PASSWORD varchar(50) not null)";
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);	 
			pstmt.executeUpdate();
			System.out.println("success:" + sql);
		} catch (SQLException e) {
			throw new CreateException("create customer didn't succeed");
		}

			 sql = "create table Coupon (" + "ID bigint not null primary key generated always as identity(start with 1, increment by 1), " + "TITLE varchar(50) not null, "
					+ "START_DATE date not null, " + "END_DATE date not null, " + "AMOUNT integer not null, "
					+ "TYPE varchar(50) not null, " + "MESSAGE varchar(50) not null, " + " PRICE float not null, "
					+ "IMAGE varchar(200) not null)";
			 try {
				 PreparedStatement pstmt = connection.prepareStatement(sql); 
				 pstmt.executeUpdate();
			System.out.println("success:" + sql);
		} catch (SQLException e) {
			throw new CreateException("create coupon didn't succeed");
		}

		// create join table Customer_Coupon
		
			 sql = "create table Customer_Coupon (" + "Customer_ID bigint, " + "Coupon_ID bigint, "
					+ "primary key (Customer_ID, Coupon_ID),"
					+ "FOREIGN KEY(Customer_ID) REFERENCES Customer(ID),"
					+ "FOREIGN KEY(Coupon_ID) REFERENCES Coupon(ID))";
					
			 try {
				 PreparedStatement pstmt = connection.prepareStatement(sql);
				 pstmt.executeUpdate();
		    	System.out.println("success:" + sql);

		} catch (SQLException e) {
			throw new CreateException("create customer_coupon didn't succeed");

		}

		// create join table Company_Coupon
		
			 sql = "create table Company_Coupon (" + "Company_ID bigint, " + "Coupon_ID bigint, "
					+ "primary key (Company_ID, Coupon_ID),"
					+ "FOREIGN KEY(Company_ID) REFERENCES Company(ID),"
					+ "FOREIGN KEY(Coupon_ID) REFERENCES Coupon(ID))";
			 try {
				 PreparedStatement pstmt = connection.prepareStatement(sql); 
			   	pstmt.executeUpdate();
			System.out.println("success: " + sql);

		} catch (SQLException e) {
			throw new CreateException("create company_coupon didn't succeed");
		}
		  finally {
				connection.close();
				try {
					Pool.returnConnection(connection);
				} catch (CouponException e) {
					try {
						throw new CreateException("connection failed");
					} catch (CreateException e1) {
						
					}
				}
			}
	}
}

