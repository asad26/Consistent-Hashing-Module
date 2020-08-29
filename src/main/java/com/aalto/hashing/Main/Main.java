/*
 * The consistent hashing module for Open Messaging Interface standards
 * Created this project for Scalability version (Conference paper)
 */

package com.aalto.hashing.Main;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.json.JSONException;


public class Main {
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException, JSONException {

		System.out.println("Process has been started...");

		// Initialization
		String webappDirLocation = "src/main/webapp";

		// Load and get properties from the file
		Properties prop = new Properties();
		prop.load(new FileInputStream("resources/config.properties"));
		String serverPort = prop.getProperty("port_listen");
		
		// Create a database
		//dm.createTable();
		
		//DatabaseMethods dm = new DatabaseMethods("lookup.db");

		Server server = new Server();
		WebAppContext root = new WebAppContext();
		root.setContextPath("/");
		root.setDescriptor(webappDirLocation + "/WEB-INF/web.xml");
		root.setResourceBase(webappDirLocation);
		root.setParentLoaderPriority(true);
		server.setHandler(root);

		ServerConnector connector = new ServerConnector(server);
		connector.setPort(Integer.valueOf(serverPort));
		server.setConnectors(new Connector[] {connector});	

		try {
			server.start();
			server.join();
		} catch (Exception e) {	
			e.printStackTrace();
		}	

	}

}
