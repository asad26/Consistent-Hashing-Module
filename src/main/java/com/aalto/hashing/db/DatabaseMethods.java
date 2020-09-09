package com.aalto.hashing.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeMap;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseMethods {

	//private HikariPool CONNECTION_POOL;
	private static HikariDataSource ds;

	public DatabaseMethods(String dbName) {

		HikariConfig hikariConfig = new HikariConfig();
		//hikariConfig.setJdbcUrl("jdbc:sqlite:./resources/sqlite/db/" + dbName);
		//hikariConfig.setDriverClassName("org.sqlite.JDBC");
		hikariConfig.setJdbcUrl("jdbc:h2:./resources/h2/db/" + dbName);
		hikariConfig.setDriverClassName("org.h2.Driver");
		hikariConfig.addDataSourceProperty("cachePrepStmts" , "true");
		hikariConfig.addDataSourceProperty("prepStmtCacheSize" , "250");
		hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit" , "2048");
		hikariConfig.setMaximumPoolSize(10);
		ds = new HikariDataSource(hikariConfig);
		createTable();
	}


	/**
	 * Connect to the database 
	 * 
	 */
	private Connection connect() {

		try {
			return ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}


	/**
	 * Create table
	 * 
	 */
	public void createTable() {
		String sql = "CREATE TABLE IF NOT EXISTS lookup" +
				"(hashKey VARCHAR(128) PRIMARY KEY," +
				"server VARCHAR(128) NOT NULL)";

		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement()) {

			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}



	/**
	 * Insert a new row into the table
	 *
	 */
	public void insertToTable(String hashKey, String server) {
		String sql = "INSERT INTO lookup (hashKey,server) VALUES(?,?)";

		try (Connection conn = this.connect();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, hashKey);
			pstmt.setString(2, server);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}


	/**
	 * Query server from table based on the hash key
	 *
	 */
	public String queryData(String hashKey) {
		String sql = "SELECT * FROM lookup WHERE hashKey = ?";
		String server = null;

		try (Connection conn = this.connect();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, hashKey);
			try (ResultSet rs  = pstmt.executeQuery()) {
				while (rs.next()) {
					server = rs.getString("server");
				}
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return server;
	}


	/**
	 * Search database for the given hash key
	 *
	 */
	public Boolean searchData(String hashKey) {
		String sql = "SELECT 1 FROM lookup WHERE hashKey = ?";
		Boolean findData = false;

		try (Connection conn = this.connect();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, hashKey);
			try (ResultSet rs  = pstmt.executeQuery()) {
				if (!(rs.next())) { 
					//System.out.println("ResultSet is empty"); 
					findData = false;
				} else {
					findData = true;
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return findData;
	}


	/**
	 * Update value based on the hash key
	 *
	 */
	public void updateData(String hashKey, String newValue) {
		String sql = "UPDATE lookup SET server = ? WHERE hashKey = ?";

		try (Connection conn = this.connect();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, newValue);
			pstmt.setString(2, hashKey);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}


	/**
	 * Delete row from the table
	 *
	 */
	public void deleteData(String hashKey) {
		String sql = "DELETE FROM lookup WHERE hashKey = ?";

		try (Connection conn = this.connect();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, hashKey);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}


	/**
	 * Read all the data from table
	 *
	 */
	public TreeMap<String, String> readAllData() {
		String sql = "SELECT * FROM lookup";

		TreeMap<String, String> hashKeysList = new TreeMap<String, String>();
		try (Connection conn = this.connect();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			try (ResultSet rs  = pstmt.executeQuery()) {

				while (rs.next()) {
					hashKeysList.put(rs.getString("hashKey"), rs.getString("server"));
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return hashKeysList;
	}


	//	public void readAllFromLookup(String limit, String offset) throws ClassNotFoundException {
	//		String sql = "SELECT * FROM lookup LIMIT " + limit + " OFFSET " + offset;
	//		String groupId = null;
	//		String serverUrl = null;
	//		try (Connection conn = this.connect("lookup.db");
	//				Statement stmt  = conn.createStatement();
	//				ResultSet rs    = stmt.executeQuery(sql)){
	//
	//			while (rs.next()) {
	//				groupId = rs.getString("groupId");
	//				serverUrl = rs.getString("server");
	//				
	//				String readObject = obj1.createOdfObject(groupId, "");
	//				String topObject = obj1.createOdfObject("Brussels-Smart-City", readObject);
	//				String finalMessage = obj1.createReadMessage(obj1.createOdfObjects(topObject));
	//					
	//				System.out.println(Thread.currentThread().getName() + groupId);
	//				//HashingMethods.readData(serverUrl, finalMessage);
	//				//Thread.sleep(50);
	//			}
	//
	//		} catch (SQLException e) {
	//			System.out.println("In Read All " + e.getMessage());
	//		}
	//		
	//	}

}
