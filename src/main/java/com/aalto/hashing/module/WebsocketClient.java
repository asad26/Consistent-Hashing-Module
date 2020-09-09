package com.aalto.hashing.module;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;


@ClientEndpoint
public class WebsocketClient {

	Session userSession = null;
	private String omiResponse = "";
	private Object syncObject = new Object();	// This object is used for synchronization
	private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

	public WebsocketClient(URI endpointURI) {
		try {
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			container.connectToServer(this, endpointURI);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Callback hook for Connection open events.
	 *
	 */
	@OnOpen
	public void onOpen(Session userSession) {
		System.out.println("opening websocket");
		this.userSession = userSession;

		// Send ping every 40s to make the connection alive
		executorService.scheduleAtFixedRate(() -> {
			try {
				String data = "Ping";
				ByteBuffer payload = ByteBuffer.wrap(data.getBytes());
				this.userSession.getAsyncRemote().sendPing(payload);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, 40, 40, TimeUnit.SECONDS);

	}

	/**
	 * Callback hook for Connection close events
	 *
	 */
	@OnClose
	public void onClose(Session userSession, CloseReason reason) {
		System.out.println("closing websocket");
		this.userSession = null;
	}

	/**
	 * Callback hook for Message Events. This method will be invoked when a server send a message
	 *
	 */
	@OnMessage
	public void onMessage(String message) {
		synchronized(syncObject) {
			omiResponse = message;
			syncObject.notify();
		}
	}


	/**
	 * Send a message and receive response in synchronous way
	 *
	 */   
	public String sendAndReceive(String message) {
		synchronized(syncObject) {
			this.userSession.getAsyncRemote().sendText(message);
			try {
				syncObject.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			return omiResponse;
		}
	}

}