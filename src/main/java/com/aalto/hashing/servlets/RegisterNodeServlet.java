package com.aalto.hashing.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.aalto.hashing.module.HashingMethods;

public class RegisterNodeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private HashingMethods hm = new HashingMethods();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{
		String ipAddress = request.getParameter("ip");
		String omiPort = request.getParameter("port");
		String serverUrl = "http://" + ipAddress + ":" + omiPort;
		hm.addServers(serverUrl);
		System.out.println("New node added with IP: " + ipAddress + " and port: " + omiPort);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		doPost(request, response);
	}
}
