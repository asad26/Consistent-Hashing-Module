package com.aalto.hashing.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import com.aalto.hashing.module.HashingMethods;
import com.aalto.hashing.module.WebsocketClient;


public class HTTPRequestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private HashingMethods hm = new HashingMethods();
	private HashMap<String, WebsocketClient> websocketObject = new HashMap<String, WebsocketClient>();


	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
	{

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		String omiResponse = null;

		String data = IOUtils.toString(request.getReader());

		if (data.isEmpty()) {
			System.out.println("The request body is empty");
			return;
		} else {		
			omiResponse = parseXml(data);
			out.println("Response " + omiResponse);
		}


		// Start asynchronous operation and parse O-MI request
		//		final AsyncContext acontext = request.startAsync();
		//		acontext.setTimeout(0);
		//		acontext.start(new Runnable() {
		//			List<String> objectIds = new ArrayList<String>();
		//			@Override
		//			public void run() {
		//				try {
		//					//HttpServletResponse response = (HttpServletResponse) acontext.getResponse();
		//
		//					String data = IOUtils.toString(acontext.getRequest().getReader());
		//					if (data.isEmpty()) {
		//						System.out.println("The request body is empty");
		//						return;
		//					} else {
		//						String omiResponse = "";
		//						objectIds = parseXml(data);
		//						if (objectIds.get(0).equals("read")) {
		//							objectIds.remove(0);
		//							String dataForHash = String.join("/", objectIds);
		//							String server = hm.mapDataToServer(dataForHash);
		//							omiResponse = httpConn.getDataFromServer(server, data);
		//							out.println("Response " + omiResponse);
		//							//System.out.println("Response sent! " + omiResponse);
		//						}
		//
		//					}
		//
		//					acontext.complete();
		//
		//				} catch (Exception e) {
		//					e.printStackTrace();
		//				}
		//
		//			}		
		//		});
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		doPost(request, response);
	}


	public String parseXml(String omiMessage) {

		List<String> objects = new ArrayList<String>();
		String omiResponse = null;
		WebsocketClient clientEndpoint;

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new InputSource(new StringReader(omiMessage)));
			doc.getDocumentElement().normalize();

			// For storing message operation: read, write, cancel, delete
			Node rootObject = doc.getElementsByTagName("omiEnvelope").item(0);
			NodeList childList = rootObject.getChildNodes();
			objects.add(childList.item(1).getNodeName());		

			// Extracting all the objects ID
			NodeList objectList = doc.getElementsByTagName("Object");
			int objectCount = objectList.getLength();
			//System.out.println("Total objects: " + objectCount);
			for (int i = 0; i < objectCount; i++) {
				Node objectNode = objectList.item(i);
				Element objectElement = (Element) objectNode;
				String id = objectElement.getElementsByTagName("id").item(0).getTextContent();
				objects.add(id);
			}

			//if (objects.get(0).equals("read")) {
			objects.remove(0);
			String dataForHash = String.join("/", objects);
			String server = hm.mapDataToServer(dataForHash);
			//omiResponse = httpConn.getDataFromServerAsyn(server, omiMessage);
			synchronized(websocketObject) {
				if (!(websocketObject.containsKey(server))) {
					clientEndpoint = new WebsocketClient(new URI(server)); 
					websocketObject.put(server, clientEndpoint);
				} else {
					clientEndpoint = websocketObject.get(server);
				}

			}
			synchronized(clientEndpoint) {
				omiResponse = clientEndpoint.sendAndReceive(omiMessage);
			}
			//} 

		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
			e.printStackTrace();
		}
		return omiResponse;

	}
}
