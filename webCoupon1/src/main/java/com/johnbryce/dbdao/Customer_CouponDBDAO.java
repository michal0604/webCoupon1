package com.johnbryce.dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.johnbryce.beans.Customer;
import com.johnbryce.beans.Customer_Coupon;
import com.johnbryce.dao.Customer_CouponDAO;
import com.johnbryce.exception.CouponException;
import com.johnbryce.exception.CreateException;
import com.johnbryce.exception.RemoveException;
import com.johnbryce.exception.UpdateException;
import com.johnbryce.utils.ConnectionPool;

/**
 *  this class lists the Data operations the association of a coupon to a customer
 *   requires
 *   
 * @author Eivy & Michal
 *
 */
public class Customer_CouponDBDAO implements Customer_CouponDAO {

	private static final String AND = null;

	private ConnectionPool pool;

	/**
	 * Empty cTor for initiating process of the class
	 * 
	 * @throws CouponException
	 */
	public Customer_CouponDBDAO() throws CouponException {
		try {
			pool = ConnectionPool.getInstance();
		} catch (SQLException e) {
			throw new CouponException("connection failed");
		}
	}

	/**
	 * Insert a connection between a customer and a coupon (represented by their IDs)
	 * 
	 * @param customerId the id of the customer
	 * @param couponId  the id of the coupon
	 * @throws CreateException if there was an error during the creation of the link in Data object
	 */
	public void insertCustomer_Coupon(long customerId, long couponId) throws CreateException {
		Connection connection;
		try {
			connection = pool.getConnection();
		} catch (Exception e1) {
			throw new CreateException("connection failed");
		}
		String sql = "INSERT INTO Customer_Coupon(Customer_ID, Coupon_ID) VALUES(?,?)";

		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, customerId);
			pstmt.setLong(2, couponId);
			pstmt.executeUpdate();
			System.out.println("Customer_Coupon created " + couponId);
		} catch (SQLException ex) {
			throw new CreateException("Customer_Coupon creation failed " + ex.getMessage());
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
	 * remove an entry representing the connection between a coupon and customer.
 
	 * @param customerId the id of the customer
	 * @param couponId  the id of the coupon
	 * @throws RemoveException if the process of removal failed for any reason
	 */
	@Override
	public void removeCustomer_Coupon(long customerId, long couponId) throws RemoveException {
		Connection connection;
		try {
			connection = pool.getConnection();
		} catch (Exception e) {
			throw new RemoveException("connection failed " + e.getMessage());
		}
		String sql = "DELETE FROM CUSTOMER_COUPON  WHERE customer_ID=? AND Coupon_ID=?";

		try {
			PreparedStatement stm = connection.prepareStatement(sql);
			{
			}
			connection.setAutoCommit(false);
			stm.setLong(1, customerId);
			stm.setLong(2, couponId);
			stm.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new RemoveException("Database error " + e1.getMessage());
			}
			throw new RemoveException("failed to remove customer_Coupon " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RemoveException("connection close failed " + e.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (CouponException e) {
				throw new RemoveException("return connection failed"+e.getMessage());
			}
		}
	}

	/**
	 * this method get all the available pairing of customer to coupon there is in a system.
	 * 
	 * @return return all the pairs of customer and coupon (represented by id)  paring  a customer to it's respected coupon
	 * @throws CouponException if the process of retrieval has failed.
	 * @throws CreateException if the process of retrieval has failed.
	 */
	@Override
	public Set<Customer_Coupon> getAllCustomer_Coupon() throws CouponException, CreateException {
		Connection connection;
		try {
			connection = pool.getConnection();
		} catch (Exception e) {
			throw new CouponException("connection failed " + e.getMessage());
		}
		Set<Customer_Coupon> list = new HashSet<Customer_Coupon>();

			String sql = "SELECT * FROM CUSTOMER_COUPON";
			try {
				PreparedStatement stm1 = connection.prepareStatement(sql);
			ResultSet rs = stm1.executeQuery();
			while (rs.next()) {
				long customerId = rs.getLong(1);
				long couponId = rs.getLong(2);
				list.add(new Customer_Coupon(customerId, couponId));
			}
		} catch (SQLException e) {
			throw new CouponException("cannot get Customer_Coupon data " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CouponException("connection close failed " + e.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (CouponException e) {
				throw new CreateException("return connection failed " + e.getMessage());
			}
		}
		return list;
	}

	/**
	 * this method return the customers link to the provided coupon
	 * 
	 * @param couponId  the id of the coupon
	 * @return a list of customers associated to a coupon.
	 * @throws CouponException if the process of data retrieval has failed.
	 * @throws CreateException if the process of retrieval has failed.
	 */
	@Override
	public Set<Long> getCustomersByCouponId(long couponId) throws CouponException, CreateException {
		try {
			pool = ConnectionPool.getInstance();
		} catch (SQLException e) {
			throw new CouponException("connection failed"+e.getMessage());
		}
		Connection connection = pool.getConnection();
		Set<Long> list = new HashSet<Long>();
			String sql = "select * from Customer_Coupon where Coupon_ID = " + couponId;
			try {
				PreparedStatement stm1 = connection.prepareStatement(sql);
			ResultSet rs = stm1.executeQuery();
			while (rs.next()) {
				long customerId = rs.getLong(1);
				list.add(customerId);
			}
		} catch (SQLException e) {
			try {
				throw new Exception("unable to get Customer_Coupon data. couponId: " + couponId);
			} catch (Exception e1) {
				throw new CouponException("connection failed");
			}
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CouponException("connection close failed"+e.getMessage());
			}

			pool.returnConnection(connection);
		}
		return list;
	}

	/**
	 * this method return the coupons link to the provided customer
	 * 
	 * @param customerId the id of the customer
	 * @return a list of coupons associated to a customer.
	 * @throws CouponException if the process of data retrieval has failed.
	 */
	@Override
	public Set<Long> getCouponsByCustomerId(long customerId) throws CouponException, CreateException {
		try {
			pool = ConnectionPool.getInstance();
		} catch (SQLException e) {
			throw new CouponException("connection failed"+e.getMessage());
		}
		Connection connection = pool.getConnection();
		Set<Long> list = new HashSet<Long>();
		try {
			String sql = "SELECT COUPON_ID FROM Customer_Coupon WHERE CUSTOMER_ID=?";
			PreparedStatement stat = connection.prepareStatement(sql);
			stat.setLong(1, customerId);
			ResultSet rs = stat.executeQuery();
			
			while (rs.next()) {
				long coupon_Id = rs.getLong(1);
				list.add(coupon_Id);
			}
		} catch (SQLException e) {
			try {
				throw new Exception("unable to get Customer_Coupon data. couponId: " + customerId);
			} catch (Exception e1) {
				throw new CouponException("get customer failed"+e1.getMessage());
			}
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CouponException("connection close failed"+e.getMessage());
			}

			pool.returnConnection(connection);
		}
		return list;
	}

	/**
	 * this method updates a link between customer and coupon.
	 * 
	 * @param customerId the id of the customer
	 * @param couponId the id of the coupon
	 * @throws UpdateException if the update process has failed 
	 */
	@Override
	public void updateCustomer_Coupon(long customerId, long couponId) throws UpdateException {
		Connection connection;
		try {
			connection = pool.getConnection();
		} catch (Exception e1) {
			throw new UpdateException("connection failed"+e1.getMessage());
		}
			String sql = "UPDATE Customer_Coupon " + " SET customer_Id='" + customerId + "', coupon_Id='" + couponId
					+ "' WHERE ID=" + customerId + AND + "' WHERE ID=" + couponId;
			try {
			PreparedStatement stm1 = connection.prepareStatement(sql);
			
			stm1.executeUpdate(sql);
		} catch (SQLException e) {
			throw new UpdateException("unable to update table " + e.getMessage());
		}
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

	/**
     * this method removes all customer's connection to different coupons.
     * 
     * @param customer that would be removed from all connection with coupons
     * @throws RemoveException  if the process of removal failed for any reason
     */
	@Override
	public void removeCustomer_Coupon(Customer customer) throws RemoveException {

		Connection connection;
		try {
			connection = pool.getConnection();
		} catch (Exception e) {
			throw new RemoveException("connection failed " + e.getMessage());
		}

		String sql = "delete from Customer_Coupon where customer_Id = ?";

		try {
			
		PreparedStatement preparedStatement = connection.prepareStatement(sql);

			connection.setAutoCommit(false);
			preparedStatement.setLong(1, customer.getCustomerId());
			preparedStatement.executeUpdate();
			connection.commit();

		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new RemoveException("DataBase error " + e1.getMessage());
			}
			throw new RemoveException("remove Customer_Coupon by customer Id failed " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RemoveException("connction close failed" + e.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (CouponException e) {
				throw new RemoveException("return connction failed " + e.getMessage());
			}
		}

	}

	/**
	 * this method verifies if a coupon is already purchased by customer.
	 * 
	 * @param customerId the id of the customer
	 * @param couponId the id of the coupon
	 * @return if the coupon  and customer are linked already
	 * @throws CouponException if there was a failure in the validation process
	 */
	@Override
	public boolean isCouponPurchasedByCustomer(long customerId, long couponId) throws CouponException {
		Connection connection = pool.getConnection();
		try {
			String sql = "SELECT coupon_Id FROM Customer_Coupon WHERE customer_Id = ? AND coupon_Id = ? ";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, customerId);
			pstmt.setLong(2, couponId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			}
			return false;

		} catch (SQLException e) {
			throw new CouponException(
					"ERROR! Checking If Coupon Already Exists For Company is Failed. " + e.getMessage());
		} catch (Exception e) {
			throw new CouponException(
					"ERROR! Checking If Coupon Already Exists For Company is Failed. " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CouponException("connection close failed"+e.getMessage());
			}
			pool.returnConnection(connection);
		}
	}

	/**
	 * the method removes all the entries of customers linked with a given coupon id.
	 * 
	 * @param couponId the id of the coupon
	 * @throws RemoveException if the process of removal failed for any reason
	 *
	 */
	@Override
	public void removeCustomer_CouponByCoupId(long couponId) throws RemoveException {
		Connection connection;
		try {
			connection = pool.getConnection();
		} catch (Exception e) {
			throw new RemoveException("connection failed " + e.getMessage());
		}

		String sql = "delete from Customer_Coupon where Coupon_ID = ?";

		try {
			
		PreparedStatement preparedStatement = connection.prepareStatement(sql);

			connection.setAutoCommit(false);
			preparedStatement.setLong(1, couponId);
			preparedStatement.executeUpdate();
			connection.commit();

		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new RemoveException("DataBase error " + e1.getMessage());
			}
			throw new RemoveException("remove Customer_Coupon by coupon Id failed " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RemoveException("connction close failed" + e.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (CouponException e) {
				throw new RemoveException("return connction failed " + e.getMessage());
			}
		}

	}
}