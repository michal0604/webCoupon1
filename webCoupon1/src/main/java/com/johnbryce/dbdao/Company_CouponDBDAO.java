package com.johnbryce.dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.johnbryce.beans.Company;
import com.johnbryce.beans.Company_Coupon;
import com.johnbryce.dao.Company_CouponDAO;
import com.johnbryce.exception.CouponException;
import com.johnbryce.exception.CreateException;
import com.johnbryce.exception.RemoveException;
import com.johnbryce.exception.UpdateException;
import com.johnbryce.utils.ConnectionPool;

/**
 * this class lists the Data operations the association of a coupon to a company
 * requires
 * 
 * @author Eivy & Michal
 *
 */
public class Company_CouponDBDAO implements Company_CouponDAO {
	private static final String AND = null;
	private ConnectionPool pool;

	/**
	 * Empty cTor for initiating process of the class
	 * 
	 * @throws CouponException
	 */
	public Company_CouponDBDAO() throws CouponException {
		try {
			pool = ConnectionPool.getInstance();
		} catch (SQLException e) {
			throw new CouponException("connection failed");
		}
	}

	/**
	 * Insert a connection between a company and a coupon (represented by there IDs)
	 * 
	 * @param companyId the id of the company
	 * @param couponId  the id of the coupon
	 * @throws CreateException if there was an error during the creation of the link in Data object
	 */
	@Override
	public void insertCompany_Coupon(long companyId, long couponId) throws CreateException {
		Connection connection;
		try {
			connection = pool.getConnection();
		} catch (CouponException e) {
			throw new CreateException("connection failed");
		}

		String sql = "insert into Company_Coupon (Company_ID, Coupon_ID) values (?,?)";

		try {

			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, companyId);
			pstmt.setLong(2, couponId);
			pstmt.executeUpdate();
			System.out.println("Company_Coupon added: companyId: " + companyId + " couponId: " + couponId);
		} catch (SQLException e) {
			throw new CreateException("Company_Coupon creation failed " + e.getMessage());
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
	 * remove an entry representing the connection between a coupon and company.
 
	 * @param companyId the id of the company
	 * @param couponId  the id of the coupon
	 * @throws RemoveException if the process of removal failed for any reason
	 */
	@Override
	public void removeCompany_Coupon(long companyId, long couponId) throws RemoveException {
		Connection connection;
		try {
			connection = pool.getConnection();
		} catch (CouponException e2) {
			throw new RemoveException("connection failed");
		}
		String sql = "DELETE FROM COMPANY_COUPON  WHERE company_ID=? AND Coupon_ID=?";

		try {
			PreparedStatement stm = connection.prepareStatement(sql);
			connection.setAutoCommit(false);
			stm.setLong(1, companyId);
			stm.setLong(2, couponId);
			stm.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new RemoveException("Database error " + e1.getMessage());
			}
			throw new RemoveException("failed to remove company_Coupon " + e.getMessage());
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
	 * the method removes all the entries of companies linked with a given coupon id.
	 * 
	 * @param couponId the id of the coupon
	 * @throws RemoveException if the process of removal failed for any reason
	 * @throws CouponException if the process of retrieving the information failed
	 */
	@Override
	public void removeCompany_CouponByCouponId(long couponId) throws CouponException, RemoveException {

		Connection connection;
		connection = pool.getConnection();
		String sql = "delete from Company_Coupon where coupon_ID = ?";
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
				throw new RemoveException("connection failed " + e1.getMessage());
			}
			throw new RemoveException("remove company_coupon by coupon failed " + e.getMessage());
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
     * this method removes all compnay's connection to different coupons.
     * 
     * @param company that would be removed from all connection with coupons
     * @throws RemoveException  if the process of removal failed for any reason
     */
	@Override
	public void removeCompany_Coupon(Company company) throws RemoveException {
		Connection connection;
		try {
			connection = pool.getConnection();
		} catch (CouponException e) {
			throw new RemoveException("connection failed " + e.getMessage());
		}
		String sql = "delete from Company_Coupon where company_ID = ?";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			connection.setAutoCommit(false);
			preparedStatement.setLong(1, company.getCompanyId());
			preparedStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new RemoveException("DataBase error " + e1.getMessage());
			}
			throw new RemoveException("remove Company_Coupon by companyId failed " + e.getMessage());

		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RemoveException("connction close failed " + e.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (CouponException e) {
				throw new RemoveException("return connction failed " + e.getMessage());
			}
		}

	}


	/**
	 * this method return the companies link to the provided coupon
	 * 
	 * @param couponId  the id of the coupon
	 * @return a list of companies associated to a coupon.
	 * @throws CouponException if the process of data retrieval has failed.
	 */
	@Override
	public Set<Long> getCompanysByCouponId(long couponId) throws CouponException {
		try {
			pool = ConnectionPool.getInstance();
		} catch (SQLException e) {
			throw new CouponException("connnection failed");
		}
		Connection connection;
		connection = pool.getConnection();
		Set<Long> list = new HashSet<Long>();
		String sql = "select * from Company_Coupon where Coupon_ID = " + couponId;
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				long company_Id = rs.getLong(1);
				list.add(company_Id);
			}
		} catch (Exception e) {
			throw new CouponException("didnt success to get company_coupon data");
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
	 * this method return the coupons link to the provided company
	 * 
	 * @param companyId the id of the company
	 * @return a list of coupons associated to a company.
	 * @throws CouponException if the process of data retrieval has failed.
	 */
	@Override
	public Set<Long> getCouponsByCompanyId(long companyId) throws CouponException {
		try {
			pool = ConnectionPool.getInstance();
		} catch (SQLException e) {
			throw new CouponException("connnection failed");
		}
		Connection connection;
		connection = pool.getConnection();
		Set<Long> list = new HashSet<Long>();
		String sql = "select * from Company_Coupon where company_Id = " + companyId;
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				long coupon_Id = rs.getLong(1);
				list.add(coupon_Id);
			}
		} catch (Exception e) {
			throw new CouponException("didnt success to get company_coupon data");
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
	 * this method get all the available pairing of company to coupon there is in a system.
	 * 
	 * @return return all the pairs of company and coupon (represented by id)  paring  a company to it's respected coupon
	 * @throws CouponException if the process of retrieval has failed.
	 */
	@Override
	public Set<Company_Coupon> getAllCompany_Coupons() throws CouponException {
		Connection connection = pool.getConnection();
		Set<Company_Coupon> set = new HashSet<Company_Coupon>();
		
			String sql = "SELECT * FROM COMPANY_COUPON";
			try {
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				long comp_Id = rs.getLong(1);
				long coupon_Id = rs.getLong(2);
				set.add(new Company_Coupon(comp_Id, coupon_Id));
			}
		} catch (SQLException e) {
			throw new CouponException("cannot get Company_Coupon data " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CouponException("connection close failed " + e.getMessage());
			}
			pool.returnConnection(connection);
		}
		return set;
	}


	/**
	 * this method updates a link between company and coupon.
	 * 
	 * @param companyId the id of the company
	 * @param couponId the id of the coupon
	 * @throws UpdateException if the updaye process has failed 
	 */
	@Override
	public void updateCompany_Coupon(long companyId, long couponId) throws UpdateException {
		Connection connection;
		try {
			connection = pool.getConnection();
		} catch (CouponException e) {
			throw new UpdateException("connection failed " + e.getMessage());
		}
		
			String sql = "UPDATE Company_Coupon " + " SET company_ID='" + companyId + "', coupon_ID='" + couponId
					+ "' WHERE ID=" + companyId + AND + "' WHERE ID=" + couponId;
			try {
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new UpdateException("update error " + e.getMessage());
		}
		try {
			connection.close();
		} catch (SQLException e) {
			throw new UpdateException("connection close failed " + e.getMessage());
		}
		try {
			pool.returnConnection(connection);
		} catch (CouponException e) {
			throw new UpdateException("return connection failed " + e.getMessage());
		}
	}

	
	/**
	 * this method verifies if a coupon is already asosiated with the company.
	 * 
	 * @param companyId the id of the company
	 * @param couponId the id of the coupon
	 * @return if the coupon  and company are linked already
	 * @throws CouponException if there was a failure in the validation process
	 */
	@Override
	public boolean isCouponExistsForCompany(long companyId, long couponId) throws CouponException {
		Connection connection = pool.getConnection();
		try {
			String sql = "SELECT * FROM Company_Coupon WHERE company_Id = ? AND coupon_Id = ? ";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, companyId);
			pstmt.setLong(2, couponId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			}
			return false;

		} catch (SQLException e) {
			throw new CouponException(" Checking if Coupon Exists For The Company is Failed. " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CouponException("connection close failed"+e.getMessage());
			}
			pool.returnConnection(connection);
		}
	}

}
