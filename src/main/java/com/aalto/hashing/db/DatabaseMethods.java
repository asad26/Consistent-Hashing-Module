package com.aalto.hashing.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeMap;

public class DatabaseMethods {

	
	/**
	 * Connect to the database 
	 * 
	 */
	private Connection connect(String dbName) {
		// SQLite connection string
		String url = "jdbc:sqlite:./resources/sqlite/db/" + dbName;
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} 
		return conn;
	}

	
	/**
	 * Create table
	 * 
	 */
	public void createTable(String dbName) {
		String sql = "CREATE TABLE IF NOT EXISTS " + dbName +
					 "(hashKey text PRIMARY KEY," +
					 "server text NOT NULL)";

		try (Connection conn = this.connect(dbName + ".db");
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
	public void insertToTable(String hashKey, String server, String dbName) {
		String sql = "INSERT or REPLACE INTO " + dbName + "(hashKey,server) VALUES(?,?)";

		try (Connection conn = this.connect(dbName + ".db");
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
	public String queryData(String hashKey, String dbName) {
		String sql = "SELECT * FROM " + dbName + " WHERE hashKey = ?";
		String server = null;
		
		try (Connection conn = this.connect(dbName + ".db");
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			pstmt.setString(1, hashKey);
			ResultSet rs  = pstmt.executeQuery();
			while (rs.next()) {
				server = rs.getString("server");
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
	public Boolean searchData(String hashKey, String dbName) {
		String sql = "SELECT 1 FROM " + dbName + " WHERE hashKey = ?";
		Boolean findData = false;
		
		try (Connection conn = this.connect(dbName + ".db");
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			pstmt.setString(1, hashKey);
			ResultSet rs  = pstmt.executeQuery();
			if (!(rs.next())) { 
				//System.out.println("ResultSet is empty"); 
				findData = false;
			} else {
				findData = true;
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
	public void updateData(String hashKey, String newValue, String dbName) {
		String sql = "UPDATE " + dbName + " SET server = ? WHERE hashKey = ?";
		
		try (Connection conn = this.connect(dbName + ".db");
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
	public void deleteData(String hashKey, String dbName) {
		String sql = "DELETE FROM " + dbName + " WHERE hashKey = ?";
		
		try (Connection conn = this.connect(dbName + ".db");
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
	public TreeMap<String, String> readAllData(String dbName) {
		String sql = "SELECT * FROM " + dbName;
		
		TreeMap<String, String> hashKeysList = new TreeMap<String, String>();
		try (Connection conn = this.connect(dbName + ".db");
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			ResultSet rs  = pstmt.executeQuery();

			while (rs.next()) {
				hashKeysList.put(rs.getString("hashKey"), rs.getString("server"));
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
