package com.aalto.hashing.module;

import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.codec.digest.DigestUtils;
import com.aalto.hashing.db.DatabaseMethods;

public class HashingMethods {

	private DatabaseMethods database;
	private static TreeMap<String, String> allData;
	
	public HashingMethods() {
		database = new DatabaseMethods("3server.db");
		allData = database.readAllData();
	}

	
	/**
	 * Generate SHA-256 hash code for a given data id 
	 * 
	 */
	public String hashFunction(String dataItem) {
		String hashHex = DigestUtils.sha256Hex(dataItem);
		return hashHex;
	}	
	
	
	/**
	 * Store server URL with unique hash key in the list 
	 * 
	 */
	public void addServers(String url) {	
		database.insertToTable(hashFunction(url), url);
	}
	
	
	/**
	 * Remove server from the list 
	 * 
	 */
	public void removeServers(String url) {
		database.deleteData(hashFunction(url));
	}
	
	
	/**
	 * Get all the servers 
	 * 
	 */
	public TreeMap<String, String> getServers() {
		return allData;
	}


	/**
	 * Consistent hashing method for mapping data hash keys to the servers
	 * 
	 */
	public String mapDataToServer(String id) {

		String hashKey = hashFunction(id);
		String serverSucc = null; 
		//TreeMap<String, String> allData = database.readAllData();
		for (Map.Entry<String, String> pair : allData.entrySet()) {
			int compare = hashKey.compareTo(pair.getKey());
			if (compare < 0) {
				serverSucc =  pair.getKey();		// key is smaller than pair.getkey
				return allData.get(serverSucc);
			}
			else if (compare > 0) {			// key is greater than pair.getkey
				continue;
			}
			else {
				serverSucc = hashKey;
				return allData.get(serverSucc);
			}
		}
		return allData.get(allData.firstKey());
	} 


	/**
	 * Consistent hashing method for getting server URL from lookup table 
	 * 
	 */
	public String getServerUrl(String id) {
		String hashKey = hashFunction(id);
		String serverURL = null;
		serverURL = database.queryData(hashKey);
		//System.out.println(serverURL);
		return serverURL;
	}
	
	
	/**
	 * Consistent hashing method to find successor server in the 'servers' list 
	 * 
	 */
	public String findSuccessorServer(String serverKey) {
		//TreeMap<String, String> allData = database.readAllData();
		for (Map.Entry<String, String> pair : allData.entrySet()) {
			int compare = serverKey.compareTo(pair.getKey());
			if (compare < 0) {
				return pair.getKey();		// key is smaller than pair.getkey
			}
			else {
				continue;
			}
		}
		return allData.firstKey();

	}
	
	
	/**
	 * Consistent hashing method for finding predecessor server in the 'servers' list 
	 * 
	 */
	public String findPredecessorServer(String serverKey) {
		
		//TreeMap<String, String> allData = database.readAllData();
		String preServer = allData.lowerKey(serverKey);
		if (preServer != null) {
			return preServer;
		}
		return allData.lastKey();
	}
	
	
	/*/**
	 * Consistent hashing method to find server for data item 
	 * 
	 *
	public String findServer(String dataKey) {
		
		TreeMap<String, String> allData = database.readAllData(dbName);
		for (Map.Entry<String, String> pair : allData.entrySet()) {
			int compare = dataKey.compareTo(pair.getKey());
			if (compare < 0) {
				return pair.getKey();		// key is smaller than pair.getkey
			}
			else if (compare > 0) {			// key is greater than pair.getkey
				continue;
			}
			else {
				return dataKey;
			}
		}
		
		return allData.firstKey();
	}*/
	
	
	/*public String findServer(String dataKey) {
		for (Map.Entry<String, String> pair : servers.entrySet()) {
			int compare = dataKey.compareTo(pair.getKey());
			if (compare < 0) {
				return pair.getKey();		// key is smaller than pair.getkey
			}
			else if (compare > 0) {			// key is greater than pair.getkey
				continue;
			}
			else {
				return dataKey;
			}
		}
		return servers.firstKey();
	}*/
	
}
