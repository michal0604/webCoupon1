package com.johnbryce.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.johnbryce.exception.CouponException;

/**
 * @author Eivy & Michal
 * Singleton Connect to DB server and DB url
 * pool of 10 connections ,cannot add more connection than 10,
 *   they need to wait, until 1 or more close and return connection.
 *
 */
public class ConnectionPool {

	private static ConnectionPool instance;
	private static int maxConnections = 10;
	private BlockingQueue<Connection> connections = new LinkedBlockingQueue<Connection>(maxConnections);

	private ConnectionPool() throws CouponException {
		try {
			Class.forName(Utile.getDriverData());
		} catch (Exception e) {
			throw new CouponException(e.getMessage());
		}
		Connection con;
		try {
			con = DriverManager.getConnection(DataBase.getDBUrl());
		} catch (SQLException e) {
			throw new CouponException("connection failed");
		}
		try {
			con.close();
		} catch (SQLException e) {
			throw new CouponException("connection failed");
		}
		while (this.connections.size() < maxConnections) {
			try {
				con = DriverManager.getConnection(DataBase.getDBUrl());
			} catch (SQLException e) {
				throw new CouponException("connection failed"+e.getMessage());
			}
			this.connections.offer(con);
		}

	}

	/**
	 * @return ConnectionPool method - SINGLETON instance
	 * @throws CouponException
	 * @throws SQLException
	 */
	public static ConnectionPool getInstance() throws CouponException, SQLException {
		if (instance == null)
			instance = new ConnectionPool();
		return instance;
	}

	/**
	 * Methods get from Connection pool
	 * 
	 * @return Connection
	 * @throws CouponException
	 */
	public synchronized Connection getConnection() throws CouponException {

		try {
			while (connections.isEmpty()) {
				wait();
				System.out.println("connection pool is empty");
			}
			Connection connection = connections.poll();
			connection.setAutoCommit(true);
			return connection;
		} catch (Exception e) {
			throw new CouponException("get connection faied"+e.getMessage());
		}
	}

	/**
	 * Methods return connection to Connection pool
	 * 
	 * @throws CouponException
	 */
	public synchronized void returnConnection(Connection con) throws CouponException { // throws Exception {
		try {
			Connection connection = DriverManager.getConnection(DataBase.getDBUrl());
			connections.offer(connection);
			notifyAll();
		} catch (Exception e) {
			throw new CouponException("return connection failed"+e.getMessage());
		}
	}

	/**
	 * Close all Connections
	 * 
	 * @throws ConnectionException
	 */
	public synchronized void closeAllConnections(Connection connection) throws CouponException {

		Connection connection2;
		while (this.connections.peek() != null) {
			connection2 = this.connections.poll();
			try {
				connection2.close();
				instance = null;
			} catch (Exception e) {
				throw new CouponException("Connections: Close All Connection: Error!");
			}
		}
	}
}
