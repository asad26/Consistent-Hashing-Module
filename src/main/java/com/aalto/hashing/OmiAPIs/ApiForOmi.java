/*
 * Created by Asad Javed on 28/08/2017
 * Aalto University project
 * 
 * Last modified 06/09/2017
 */

package com.aalto.hashing.OmiAPIs;

public class ApiForOmi {

	// For read message
	public String createInfoItem(String name) {
		String infoItem = "<InfoItem name=\"" + name + "\"/>";
		return infoItem;
	}


	public String createInfoItem(String name, String value) {
		String infoItem = "<InfoItem name=\"" + name + "\"><value>" + value + "</value></InfoItem>";
		return infoItem;
	}

	public String createInfoItem(String name, String value, String pubDate) {
		String infoItem = "<InfoItem name=\"" + name + "\"><value dateTime=\"" + pubDate + "\" type=\"xs:integer\">" + 
				value + "</value></InfoItem>";
		return infoItem;
	}

	public String createOdfObject(String id, String nInfoItems) {
		String odfObject = "<Object>" +
				"<id>" + id + "</id>" +
				nInfoItems +
				"</Object>";
		return odfObject;
	}

	public String createOdfObjects(String nObject) {
		String odfObjects = "<Objects xmlns=\"http://www.opengroup.org/xsd/odf/1.0/\">" +
				nObject +
				"</Objects>";
		return odfObjects;
	}

	public String createWriteMessage(String objects) {
		String omiEnvelope = "<omiEnvelope xmlns=\"http://www.opengroup.org/xsd/omi/1.0/\" version=\"1.0\" ttl=\"-1\">" +
				"<write msgformat=\"odf\">" +
				"<msg>" +
				objects +
				"</msg></write></omiEnvelope>";
		return omiEnvelope;

	}

	public String createReadMessage(String objects) {
		String omiEnvelope = "<omiEnvelope xmlns=\"http://www.opengroup.org/xsd/omi/1.0/\" version=\"1.0\" ttl=\"-1\">" +
				"<read msgformat=\"odf\">" +
				"<msg>" +
				objects +
				"</msg></read></omiEnvelope>";
		return omiEnvelope;

	}

}
