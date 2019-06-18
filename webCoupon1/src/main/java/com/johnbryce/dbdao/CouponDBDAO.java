package com.johnbryce.dbdao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.johnbryce.beans.Coupon;
import com.johnbryce.beans.CouponType;
import com.johnbryce.dao.CouponDAO;
import com.johnbryce.exception.CouponException;
import com.johnbryce.exception.CreateException;
import com.johnbryce.exception.RemoveException;
import com.johnbryce.exception.UpdateException;
import com.johnbryce.utils.ConnectionPool;

/**
 * this class implements data base operations for Coupon's requirements.
 * 
 * @author Michal & Eivy
 *
 */
public class CouponDBDAO implements CouponDAO {
	private ConnectionPool pool;

	/**
	 * cTor for the coupon object.
	 * 
	 * @throws CouponException
	 */
	public CouponDBDAO() throws CouponException {
		try {
			pool = ConnectionPool.getInstance();
		} catch (SQLException e) {
			throw new CouponException("connection failed");
		}
	}

	/**
	 * Inserts a coupon data set to the Database
	 * 
	 * @param coupon
	 *            coupon to be inserted
	 * @throws CreateException
	 *             for problems in inserting the coupon to the DB
	 * @throws SQLException
	 *             for DB related failures
	 */
	@Override
	public long insertCoupon(Coupon coupon) throws CreateException {

		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (CouponException e) {
			throw new CreateException("didnt success to connect " + e.getMessage());
		}
		String sql = "INSERT INTO Coupon (title,start_date,end_date,amount,type,message,price,image) VALUES(?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, coupon.getTitle());
			pstmt.setDate(2, (Date) coupon.getStart_date());
			pstmt.setDate(3, (Date) coupon.getEnd_date());
			pstmt.setInt(4, coupon.getAmount());
			pstmt.setString(5, coupon.getType().name());
			pstmt.setString(6, coupon.getMessage());
			pstmt.setDouble(7, coupon.getPrice());
			pstmt.setString(8, coupon.getImage());
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
			long generatedKey = 0;
			if (rs.next()) {
			    generatedKey = rs.getLong(1);
			}
			return generatedKey;
		} catch (SQLException ex) {
			throw new CreateException("Coupon creation failed " + ex.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e1) {
				throw new CreateException("close connection was failed " + e1.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (CouponException e) {
				throw new CreateException("return connection was failed " + e.getMessage());
			}

		}
	}

	/**
	 * this function removed a given coupon from the coupon data
	 * 
	 * @param Coupon
	 *            to be removed.
	 * 
	 * @throws RemoveException
	 * @throws CreateException
	 */
	@Override
	public void removeCoupon(Coupon Coupon) throws CreateException, RemoveException {
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (CouponException e) {
			throw new CreateException("didnt success to connect " + e.getMessage());
		}
		String sql = "DELETE FROM Coupon WHERE ID=?";
		try {
			PreparedStatement pstm1 = connection.prepareStatement(sql);
			connection.setAutoCommit(false);
			pstm1.setLong(1, Coupon.getCouponId());
			pstm1.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new RemoveException("Database error " + e1.getMessage());
			}
			throw new RemoveException("failed to remove Coupon " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RemoveException("connection close failed" + e.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (CouponException e) {
				throw new RemoveException("return connection failed" + e.getMessage());
			}

		}
	}

	/**
	 * updates a coupon into the Database
	 * 
	 * @param coupon
	 *            coupon to update
	 * @throws UpdateException
	 *             for problems in updating the coupon to the DB
	 * @throws CreateException
	 *             for problems in inserting the coupon to the DB
	 */
	@Override
	public long updateCoupon(Coupon Coupon) throws UpdateException, CreateException {
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (CouponException e) {
			throw new CreateException("didnt success to connect " + e.getMessage());
		}
		try {
			String sql = "UPDATE Coupon SET TITLE=?, START_DATE=?, END_DATE=?, AMOUNT=?,"
					+ " TYPE=?, MESSAGE=?, PRICE=?, IMAGE=? WHERE ID=?";
			PreparedStatement stm1 = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stm1.setString(1, Coupon.getTitle());
			stm1.setDate(2, (Date) Coupon.getStart_date());
			stm1.setDate(3, (Date) Coupon.getEnd_date());
			stm1.setInt(4, Coupon.getAmount());
			stm1.setString(5, Coupon.getType().toString());
			stm1.setString(6, Coupon.getMessage());
			stm1.setDouble(7, Coupon.getPrice());
			stm1.setString(8, Coupon.getImage());
			stm1.setLong(9, Coupon.getCouponId());
			stm1.executeUpdate();
			ResultSet rs = stm1.getGeneratedKeys();
			long generatedKey = 0;
			if (rs.next()) {
			    generatedKey = rs.getLong(1);
			}
			return generatedKey;
		} catch (SQLException e) {
			throw new UpdateException("update Coupon failed " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new UpdateException("connection close failed " + e.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (CouponException e) {
				throw new CreateException(" return connection failed " + e.getMessage());
			}
		}
	}

	/**
	 * get a coupon data set by the coupon's id.
	 * 
	 * @param couponId
	 *            representing the id of the required coupon
	 * @return a coupon which her id is couponId.
	 * @throws CouponException
	 *             for error related to the retrieval of the coupon
	 * @throws CreateException
	 */
	@Override
	public Coupon getCoupon(long couponId) throws CreateException, CouponException {
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (CouponException e) {
			throw new CreateException("didnt success to connect " + e.getMessage());
		}
		Coupon coupon = null;
		String sql = "SELECT * FROM Coupon WHERE ID=" + couponId;
		try {
			PreparedStatement stm1 = connection.prepareStatement(sql);
			ResultSet rs = stm1.executeQuery();
			if (rs.next()) {
				coupon = new Coupon();
				coupon.setCouponId(rs.getLong(1));
				coupon.setTitle(rs.getString(2));
				coupon.setStart_date(rs.getDate(3));
				coupon.setEnd_date(rs.getDate(4));
				coupon.setAmount(rs.getInt(5));
				coupon.setMessage(rs.getString(7));
				coupon.setPrice(rs.getDouble(8));
				coupon.setImage(rs.getString(9));
				coupon.setType(Coupon.resolveType(rs.getString(6)));
			}
		} catch (SQLException e) {
			throw new CouponException("unable to get Coupon data " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CouponException("connection close failed " + e.getMessage());
			}
			pool.returnConnection(connection);

		}
		return coupon;
	}

	/**
	 * get a list of all available coupons
	 * 
	 * @return a list of available coupons
	 * @throws CouponException
	 *             for error related to the retrieval of the coupon
	 */
	@Override
	public Set<Coupon> getAllCoupons() throws CouponException {
		Connection connection;
		try {
			connection = pool.getConnection();
		} catch (Exception e) {
			throw new CouponException("connection failed" + e.getMessage());
		}
		Set<Coupon> set = new HashSet<Coupon>();
		Coupon coupon;
		String sql = "SELECT * FROM Coupon";
		try {
			PreparedStatement stm1 = connection.prepareStatement(sql);
			ResultSet rs = stm1.executeQuery();
			while (rs.next()) {
				coupon = new Coupon();
				coupon.setCouponId(rs.getLong(1));
				coupon.setTitle(rs.getString(2));
				coupon.setStart_date(rs.getDate(3));
				coupon.setEnd_date(rs.getDate(4));
				coupon.setAmount(rs.getInt(5));
				coupon.setMessage(rs.getString(7));
				coupon.setPrice(rs.getDouble(8));
				coupon.setImage(rs.getString(9));
				coupon.setType(Coupon.resolveType(rs.getString(6)));
				set.add(coupon);
			}
		} catch (SQLException e) {
			System.out.println(e);
			throw new CouponException("cannot get Coupon data " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CouponException("connection close failed" + e.getMessage());
			}
			pool.returnConnection(connection);
		}
		return set;
	}

	/**
	 * this function remove a coupon by it's id
	 * 
	 * @param couponId
	 *            of the coupon to be deleted
	 * 
	 * @throws CouponException
	 */
	@Override
	public void removeCouponID(long couponId) throws CouponException {
		Connection connection;
		try {
			connection = pool.getConnection();
		} catch (Exception e) {
			throw new CouponException("Database error " + e.getMessage());
		}
		String sql = "DELETE FROM Coupon WHERE ID=?";
		try {
			PreparedStatement pstm1 = connection.prepareStatement(sql);
			connection.setAutoCommit(false);
			pstm1.setLong(1, couponId);
			pstm1.executeUpdate();
			connection.commit();

		} catch (SQLException e) {
			try {
				connection.rollback();

			} catch (SQLException e1) {
				throw new CouponException("Database error " + e1.getMessage());
			}
			throw new CouponException("failed to remove Coupon " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CouponException("connection close failed " + e.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (Exception e) {
				throw new CouponException("return connection failed " + e.getMessage());
			}
		}

	}

	/**
	 * this function returns all available coupons by specific max coupon
	 * exploration Date
	 * 
	 * @param untilDate
	 *            the date that a coupon's expire Date cannot exceed
	 * @return a list of available coupon that expire before or the same date as
	 *         untilDate
	 * @throws CouponException
	 *             for error related to the retrieval of the coupon
	 */
	@Override
	public Set<Coupon> getAllCouponsByDate(String untilDate) throws CouponException {
		Connection connection;
		try {
			connection = pool.getConnection();
		} catch (Exception e) {
			throw new CouponException("Database error " + e.getMessage());
		}
		Set<Coupon> set = new HashSet<Coupon>();
		Coupon coupon;
		String sql = "select * from Coupon where end_date=" + untilDate;
		try {
			PreparedStatement stm1 = connection.prepareStatement(sql);
			ResultSet rs = stm1.executeQuery();
			while (rs.next()) {
				coupon = new Coupon();
				coupon.setCouponId(rs.getLong(1));
				coupon.setTitle(rs.getString(2));
				coupon.setStart_date(rs.getDate(3));
				coupon.setEnd_date(rs.getDate(4));
				coupon.setAmount(rs.getInt(5));
				coupon.setMessage(rs.getString(7));
				coupon.setPrice(rs.getDouble(8));
				coupon.setImage(rs.getString(9));
				coupon.setType(Coupon.resolveType(rs.getString(6)));
				set.add(coupon);
			}
		} catch (SQLException e) {
			System.out.println(e);
			throw new CouponException("cannot get Coupon data " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CouponException("connection close failed " + e.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (Exception e) {
				throw new CouponException("return connection failed " + e.getMessage());
			}
		}
		return set;
	}

	/**
	 * this function returns all available coupons by specific type
	 * 
	 * @param coupType
	 *            the required coupon type
	 * @return all available coupons that much the type
	 * @throws CouponException
	 *             for error related to the retrieval of the coupon
	 */

	public Set<Coupon> getAllCouponsByType(CouponType couponType) throws CouponException {

		Connection connection;
		connection = pool.getConnection();

		try {
			Set<Coupon> list = new HashSet<>();
			String sql = String.format("select * from Coupon where TYPE = '%s'", couponType);
			PreparedStatement stm1 = connection.prepareStatement(sql);
			ResultSet resultSet = stm1.executeQuery();

			while (resultSet.next()) {
				Coupon coupon = new Coupon();
				coupon.setCouponId(resultSet.getLong(1));
				coupon.setTitle(resultSet.getString(2));
				coupon.setStart_date(resultSet.getDate(3));
				coupon.setEnd_date(resultSet.getDate(4));
				coupon.setAmount(resultSet.getInt(5));
				coupon.setType(Coupon.resolveType(resultSet.getString(6)));
				coupon.setMessage(resultSet.getString(7));
				coupon.setPrice(resultSet.getDouble(8));
				coupon.setImage(resultSet.getString(9));

				list.add(coupon);
			}
			return list;
		} catch (SQLException e) {
			System.out.println(e);
			throw new CouponException("cannot get Coupon data " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CouponException("connection close failed " + e.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (Exception e) {
				throw new CouponException("return connection failed " + e.getMessage());
			}
		}

	}

	/**
	 * this function returns all available coupons by specific max coupon price
	 * 
	 * @param priceMax
	 *            the price that a coupon's price cannot exceed
	 * @return a list of available coupon that are lower or equal to PriceMax
	 * @throws CouponException
	 *             for error related to the retrieval of the coupon
	 */

	@Override
	public Set<Coupon> getAllCouponsByPrice(double priceMax) throws CouponException {
		Connection connection;
		try {
			connection = pool.getConnection();
		} catch (Exception e) {
			throw new CouponException("Database error " + e.getMessage());
		}
		Set<Coupon> set = new HashSet<Coupon>();
		Coupon coupon;
		String sql = "select * from Coupon where Price = " + priceMax;
		try {
			PreparedStatement stm1 = connection.prepareStatement(sql);
			ResultSet rs = stm1.executeQuery();
			while (rs.next()) {
				coupon = new Coupon();
				coupon.setCouponId(rs.getLong(1));
				coupon.setTitle(rs.getString(2));
				coupon.setStart_date(rs.getDate(3));
				coupon.setEnd_date(rs.getDate(4));
				coupon.setAmount(rs.getInt(5));
				coupon.setMessage(rs.getString(7));
				coupon.setPrice(rs.getDouble(8));
				coupon.setImage(rs.getString(9));
				coupon.setType(Coupon.resolveType(rs.getString(6)));
				set.add(coupon);
			}
		} catch (SQLException e) {
			System.out.println(e);
			throw new CouponException("cannot get Coupon data " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CouponException("connection close failed" + e.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (Exception e) {
				throw new CouponException("return connection failed " + e.getMessage());
			}
		}
		return set;
	}

	/**
	 * this function search if a given string exist as a title of one of the
	 * coupons.
	 * 
	 * @param coupTitle
	 *            a string representing a coupon title
	 * 
	 * @return true if there is such a coupon title, false otherwise.
	 * @throws CouponException
	 */
	@Override
	public boolean isCouponTitleExists(String Title) throws CouponException {
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (Exception e1) {
			throw new CouponException("connection failed");
		}
		try {
			String sql = "SELECT ID FROM Coupon WHERE title = ? ";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, Title);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			}
			return false;

		} catch (SQLException e) {

			throw new CouponException("  Checking if Coupon Title Exists Failed." + e.getMessage());

		} finally {
			try {
				connection.close();
			} catch (SQLException e1) {
				throw new CouponException("connection close failed" + e1.getMessage());
			}

			try {
				pool.returnConnection(connection);
			} catch (CouponException e) {
			}

		}
	}

}
