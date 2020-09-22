package com.aalto.hashing.servlets;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.aalto.hashing.module.HashingMethods;
import com.aalto.hashing.module.WebsocketClient;

@WebSocket
public class DataSocket {
	
	private static HashingMethods hm = new HashingMethods();
	private static HashMap<String, WebsocketClient> websocketObject = new HashMap<String, WebsocketClient>();
	
	@OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.println("Closing websocket: " + reason);
    }

    @OnWebSocketError
    public void onError(Throwable t) {
        System.out.println("Error: " + t.getMessage());
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("Connect: " + session.getRemoteAddress().getAddress());
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        System.out.println("Message: " + message);
        String omiResponse = null;
        if (message.isEmpty()) {
			System.out.println("The request body is empty");
			return;
		} else {		
			omiResponse = parseXml(message);	
			session.getRemote().sendString(omiResponse);
		}
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
