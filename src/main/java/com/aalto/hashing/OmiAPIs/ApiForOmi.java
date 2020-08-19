/*
 * Created by Asad Javed on 28/08/2017
 * Aalto University project
 * 
 * Last modified 06/09/2017
 */

package com.aalto.hashing.OmiAPIs;

public class ApiForOmi {

	/**
	 * API for creating O-DF InfoItem without value (for read operation)
	 * 
	 */
	public String createInfoItem(String name) {
		String infoItem = "<InfoItem name=\"" + name + "\"/>";
		return infoItem;
	}


	/**
	 * API for creating O-DF InfoItem with value
	 * 
	 */
	public String createInfoItem(String name, String value) {
		String infoItem = "<InfoItem name=\"" + name + "\"><value>" + value + "</value></InfoItem>";
		return infoItem;
	}
	
	
	/**
	 * API for creating O-DF InfoItem with value and dateTime
	 * 
	 */
	public String createInfoItem(String name, String value, String pubDate) {
		String infoItem = "<InfoItem name=\"" + name + "\"><value dateTime=\"" + pubDate + "\" type=\"xs:integer\">" + 
				value + "</value></InfoItem>";
		return infoItem;
	}

	
	/**
	 * API for creating O-DF Object with InfoItems inside it
	 * 
	 */
	public String createOdfObject(String id, String nInfoItems) {
		String odfObject = "<Object>" +
				"<id>" + id + "</id>" +
				nInfoItems +
				"</Object>";
		return odfObject;
	}

	
	/**
	 * API for creating O-DF Object with child objects inside it
	 * 
	 */
	public String createOdfObjects(String nObject) {
		String odfObjects = "<Objects xmlns=\"http://www.opengroup.org/xsd/odf/1.0/\">" +
				nObject +
				"</Objects>";
		return odfObjects;
	}

	
	/**
	 * API for creating O-MI write message
	 * 
	 */
	public String createWriteMessage(String objects) {
		String omiEnvelope = "<omiEnvelope xmlns=\"http://www.opengroup.org/xsd/omi/1.0/\" version=\"1.0\" ttl=\"-1\">" +
				"<write msgformat=\"odf\">" +
				"<msg>" +
				objects +
				"</msg></write></omiEnvelope>";
		return omiEnvelope;

	}

	
	/**
	 * API for creating O-MI read message
	 * 
	 */
	public String createReadMessage(String objects) {
		String omiEnvelope = "<omiEnvelope xmlns=\"http://www.opengroup.org/xsd/omi/1.0/\" version=\"1.0\" ttl=\"-1\">" +
				"<read msgformat=\"odf\">" +
				"<msg>" +
				objects +
				"</msg></read></omiEnvelope>";
		return omiEnvelope;

	}

}
