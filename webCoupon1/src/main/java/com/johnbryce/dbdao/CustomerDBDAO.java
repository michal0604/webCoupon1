package com.johnbryce.dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.johnbryce.beans.Coupon;
import com.johnbryce.beans.Customer;
import com.johnbryce.dao.CustomerDAO;
import com.johnbryce.exception.CouponException;
import com.johnbryce.exception.CreateException;
import com.johnbryce.exception.CustomerException;
import com.johnbryce.exception.RemoveException;
import com.johnbryce.exception.UpdateException;
import com.johnbryce.utils.ConnectionPool;

/**
 * this class implement the DB operations associated with the customer's data
 * access requirements.
 * 
 * @author Eivy & Michal
 *
 */
public class CustomerDBDAO implements CustomerDAO {
	private ConnectionPool pool;

	/**
	 * cTor for the DBDAO that initiate the resource required for the class
	 * 
	 * @throws CouponException
	 *             for problems from creation.
	 */
	public CustomerDBDAO() throws CouponException {
		try {
			pool = ConnectionPool.getInstance();
		} catch (SQLException e) {
			throw new CouponException("connection failed");
		}
	}

	/**
	 * Inserts a customer data set to the Database
	 * 
	 * @throws CreateException
	 *             for problems in inserting the customer to the DB
	 *
	 */
	@Override
	public long insertCustomer(Customer Customer) throws CreateException {
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (CouponException e) {
			throw new CreateException("connection failed " + e.getMessage());
		}
		String sql = "insert into Customer(CUST_NAME, PASSWORD) values (?,?)";

		try {
			PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, Customer.getCustomerName());
			pstmt.setString(2, Customer.getPassword());
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
			long generatedKey = 0;
			if (rs.next()) {
				generatedKey = rs.getLong(1);
			}
			return generatedKey;
		} catch (SQLException e) {
			throw new CreateException("Customer creation failed " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CreateException("connection close failed " + e.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (CouponException e) {
				throw new CreateException("return connection failed " + e.getMessage());
			}
		}
	}

	/**
	 * remove a customer from the Database
	 * 
	 * @param customer
	 *            customer to be remove
	 * @throws RemoveException
	 */
	@Override
	public void removeCustomer(Customer customer) throws RemoveException {
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (CouponException e) {
			throw new RemoveException("connection failed " + e.getMessage());
		}
		String sql = "DELETE FROM Customer WHERE ID=?";

		try {
			PreparedStatement pstm1 = connection.prepareStatement(sql);
			connection.setAutoCommit(false);
			pstm1.setLong(1, customer.getCustomerId());
			pstm1.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new RemoveException("Database error " + e1.getMessage());
			}
			throw new RemoveException("failed to remove customer " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RemoveException("connection close failed " + e.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (CouponException e) {
				throw new RemoveException("return connection failed " + e.getMessage());
			}

		}

	}

	/**
	 * updates a customer into the Database
	 * 
	 * @param customer
	 *            customer to update
	 */
	@Override
	public long updateCustomer(Customer Customer) throws UpdateException {
		Connection connection;
		try {
			connection = pool.getConnection();
		} catch (CouponException e) {
			throw new UpdateException("connection failed " + e.getMessage());
		}

		String sql = "UPDATE Customer " + " SET CUST_NAME='" + Customer.getCustomerName() + "', PASSWORD='"
				+ Customer.getPassword() + "' WHERE ID=" + Customer.getCustomerId();
		try {
			PreparedStatement pstm1 = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstm1.executeUpdate();
			ResultSet rs = pstm1.getGeneratedKeys();
			long generatedKey = 0;
			if (rs.next()) {
				generatedKey = rs.getLong(1);
			}
			return generatedKey;
		} catch (SQLException e) {
			throw new UpdateException("update Customer failed " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new UpdateException("connection close failed " + e.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (Exception e) {
				throw new UpdateException("return connection failed " + e.getMessage());
			}
		}
	}

	/**
	 * get a customer data set by the customer's name.
	 * 
	 * @param customerName
	 *            representing the name of the required customer
	 * @throws CustomerException
	 *             for errors happing due to trying to get a customer from DB
	 */
	@Override
	public Customer getCustomer(String customerName) throws CustomerException {
		Connection connection;
		try {
			connection = pool.getConnection();
		} catch (CouponException e) {
			throw new CustomerException("connection failed " + e.getMessage());
		}
		Customer customer = null;

		String sql = "SELECT * FROM Customer WHERE ID=" + customerName;
		try {
			PreparedStatement stm1 = connection.prepareStatement(sql);
			ResultSet rs = stm1.executeQuery();

			if (rs.next()) {
				customer = new Customer();
				customer.setCustomerId(rs.getLong(1));
				customer.setCustomerName(rs.getString(2));
				customer.setPassword(rs.getString(3));
			}

		} catch (SQLException e) {
			throw new CustomerException("unable to get Customer data " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CustomerException("connection close failed " + e.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (CouponException e) {
				throw new CustomerException("return connection failed " + e.getMessage());
			}

		}
		return customer;
	}

	/**
	 * get all the Customers from the Database.
	 * 
	 * @throws CustomerException
	 *             for errors occurring due to trying to get all customers from DB
	 */
	@Override
	public Set<Customer> getAllCustomers() throws CustomerException {
		Connection connection;
		try {
			connection = pool.getConnection();
		} catch (CouponException e) {
			throw new CustomerException("connection failed " + e.getMessage());
		}

		Set<Customer> list = new HashSet<Customer>();

		String sql = "SELECT * FROM CUSTOMER";
		try {
			PreparedStatement stm1 = connection.prepareStatement(sql);
			ResultSet rs = stm1.executeQuery();

			while (rs.next()) {
				long customerId = rs.getLong(1);
				String customerName = rs.getString(2);
				String password = rs.getString(3);
				list.add(new Customer(customerId, customerName, password));
			}
		} catch (SQLException e) {
			throw new CustomerException("cannot get Customer data " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CustomerException("connection close failed " + e.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (Exception e) {
				throw new CustomerException("return connection failed" + e.getMessage());
			}
		}
		return list;
	}

	/**
	 * get a customer data set by the customer's id.
	 * 
	 * @param id
	 *            representing the id of the required customer
	 * @throws CustomerException
	 *             for errors happing due to trying to get a customer from DB
	 */
	@Override
	public Customer getCustomer(long CustomerId) throws CustomerException {
		try {
			try {
				pool = ConnectionPool.getInstance();
			} catch (SQLException e) {
				throw new CouponException("connection failed");
			}
		} catch (CouponException e) {
			throw new CustomerException("connection failed " + e.getMessage());
		}
		Connection connection;
		try {
			connection = pool.getConnection();
		} catch (CouponException e) {
			throw new CustomerException("connection failed " + e.getMessage());
		}
		Customer customer = new Customer();

		String sql = "SELECT * FROM Customer WHERE ID=" + CustomerId;
		try {
			PreparedStatement stm1 = connection.prepareStatement(sql);
			ResultSet rs = stm1.executeQuery();

			if (rs.next()) {
				customer = new Customer();
				customer.setCustomerId(rs.getLong(1));
				customer.setCustomerName(rs.getString(2));
				customer.setPassword(rs.getString(3));
			}

		} catch (SQLException e) {
			throw new CustomerException("unable to get data, customerId: " + CustomerId);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CustomerException("connection close failed " + e.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (CouponException e) {
				throw new CustomerException("return connection failed " + e.getMessage());
			}
		}
		return customer;
	}

	/**
	 * this method returns a customer if the user password is correct.
	 * 
	 * @param name
	 *            customer's name of the logged in customer
	 * @param password
	 *            customer's password of the logged in customer
	 * @throws CustomerException
	 *             for problem retrieving the customer data.
	 */
	@Override
	public Customer login(String customerName, String password) throws CustomerException {
		Connection connection;
		try {
			connection = pool.getConnection();
		} catch (CouponException e) {
			throw new CustomerException("connection failed " + e.getMessage());
		}
		Customer customer = null;
		try {
			String sql = "SELECT id, CUST_NAME, password FROM app.Customer WHERE CUST_NAME = ? and password = ?";
			PreparedStatement pstmt = connection.prepareStatement(sql);

			pstmt.setString(1, customerName);
			pstmt.setString(2, password);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				long customerId = rs.getLong("id");
				if (customerId > 0) {
					customer = new Customer();
					customer.setCustomerId(customerId);
					customer.setCustomerName(customerName);
					customer.setPassword(password);
					return customer;
				}
			}
			return null;

		} catch (SQLException e) {
			throw new CustomerException(" Login Failed " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CustomerException("connection close failed " + e.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (CouponException e) {
				throw new CustomerException("return connection failed " + e.getMessage());
			}
		}
	}

	/**
	 * returns if a customer identified by the name exist in the DB records.
	 * 
	 * @param customerName
	 *            name that should be checked for existing
	 * @throws CouponException
	 *             for error related to the retrieval of the customer
	 * @throws CustomerException
	 */
	@Override
	public boolean isCustomerNameExists(String customerName) throws CouponException, CustomerException {
		Connection connection = pool.getConnection();
		try {
			String sql = "SELECT ID FROM Customer WHERE CUST_NAME = ? ";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, customerName);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			throw new CouponException(" Failed to checking if Customer name already exists. " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CustomerException("connection close failed " + e.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (CouponException e) {
				throw new CustomerException("return connection failed " + e.getMessage());
			}
		}
	}

	/**
	 * this method get customer ID, and return list of coupons from data base.
	 * 
	 */
	@Override
	public Set<Coupon> getAllCoupons(long customerId) throws CouponException {
		Connection connection = pool.getConnection();
		Set<Coupon> coupons = new HashSet<Coupon>();
		CouponDBDAO couponDB = new CouponDBDAO();
		try {

			String sql = "SELECT COUPON_ID FROM Customer_Coupon WHERE CUSTOMER_ID=?";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, customerId);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				try {
					coupons.add(couponDB.getCoupon(rs.getLong("COUPON_ID")));
				} catch (CreateException e) {
					throw new CouponException("get coupon failed" + e.getMessage());
				}
			}
		} catch (SQLException e) {
			throw new CouponException(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CouponException("connection close failed" + e.getMessage());
			}
			pool.returnConnection(connection);
		}
		return coupons;

	}
}
