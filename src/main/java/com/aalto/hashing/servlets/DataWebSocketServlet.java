package com.aalto.hashing.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;


public class DataWebSocketServlet extends WebSocketServlet  {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().println("HTTP GET method not implemented.");
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		response.getWriter().println("HTTP POST method not implemented.");
	}

	
	@Override
	public void configure(WebSocketServletFactory factory) {
		// set a 10 second idle timeout
        factory.getPolicy().setIdleTimeout(50000);
        
        // register the socket
        factory.register(DataSocket.class);
	}

}
