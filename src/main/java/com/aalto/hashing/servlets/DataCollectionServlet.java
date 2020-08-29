package com.aalto.hashing.servlets;


import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.impl.cookie.DateUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.aalto.hashing.OmiAPIs.ApiForOmi;
import com.aalto.hashing.module.HTTPConnections;
import com.aalto.hashing.module.HashingMethods;

public class DataCollectionServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private HashingMethods hm = new HashingMethods();
	private ApiForOmi obj = new ApiForOmi();
	private HTTPConnections httpConn = new HTTPConnections();

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{

		try {
			String data = IOUtils.toString(request.getReader());
			JSONObject jsonObject = new JSONObject(data);
			@SuppressWarnings("unchecked")
			Iterator<String> keys = jsonObject.keys();
			String key;

			while(keys.hasNext()) {
				key = keys.next();
				if (key.equals("UTC-Time")) {
					//dateTime = toISO8601UTC(jsonObject.get(key).toString());
					//System.out.println(dateTime);

				} else {
					createODFAndSend(key, jsonObject.get(key).toString());
				}
			}
			//System.out.println(finalMessage);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} 
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		doPost(request, response);
	}

	
	/**
	 * Method for creating O-DF message from the incoming data and store in O-MI server
	 * 
	 */
	private void createODFAndSend(String path, String value) {

		String odf = "";
		String dataForHash = path.substring(0, path.lastIndexOf("/"));
		String server = hm.mapDataToServer(dataForHash);
		//System.out.println(server);
		String[] objectArray = path.split("/");
		int len = objectArray.length;
		String infoItem = obj.createInfoItem(objectArray[len-1], value);
		for (int i = len-2; i >= 0; i --) {
			odf = obj.createOdfObject(objectArray[i], infoItem);
			infoItem = odf;
		}

		String finalMessage = obj.createWriteMessage(obj.createOdfObjects(odf));
	
		httpConn.sendDataToServer(server, finalMessage);

	}

	/*private String toISO8601UTC(String utcTime) throws ParseException {
		
		Date date = new Date(utcTime);
		SimpleDateFormat sdf;
		sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		//sdf.setTimeZone(TimeZone.getTimeZone("Europe/Helsinki"));
		String dateTime = sdf.format(date);
		//System.out.println(utcTime + "\t" + dateTime);
		return dateTime;
		
	}*/

}
