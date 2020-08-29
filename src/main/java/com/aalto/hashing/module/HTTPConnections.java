package com.aalto.hashing.module;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HTTPConnections {
	
	private OkHttpClient client;
	
	
	public HTTPConnections() {
		client = new OkHttpClient.Builder()
				.connectTimeout(100, TimeUnit.SECONDS)
				.writeTimeout(100, TimeUnit.SECONDS)
				.readTimeout(300, TimeUnit.SECONDS).build();
	}

	
	/**
	 * Method for sending XML message to the O-MI sand box (Synchronously)
	 * 
	 */
	public String sendDataToServer(String url, String finalMessage) {
		MediaType textXml = MediaType.parse("text/xml; charset=utf-8");
		Request request = new Request.Builder().url(url)
				.post(RequestBody.create(textXml, finalMessage)).build();

		Response response = null;
		String result = null;
		try {
			response = client.newCall(request).execute();
			//System.out.println(response.message());
			result =  response.body().string();
			response.close();  
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * Method for reading data from O-MI sand box 
	 * 
	 */
	public String getDataFromServer(String url, String finalMessage) {
		//System.out.println(finalMessage);
		MediaType textXML = MediaType.parse("text/xml; charset=utf-8");
		Request request = new Request.Builder().url(url)
				.post(RequestBody.create(textXML, finalMessage)).build();

		Response response = null;
		String result = null;
		try {
			response = client.newCall(request).execute();
			//System.out.println(response.message());
			result =  response.body().string();
			response.close();  

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result; 
	}
	
	
	/**
	 * Method for reading data from O-MI sand box (Asynchronously)
	 * 
	 */
	public String getDataFromServerAsyn(String url, String finalMessage) {
		//System.out.println(finalMessage);
		MediaType textXML = MediaType.parse("text/xml; charset=utf-8");
		Request request = new Request.Builder().url(url)
				.post(RequestBody.create(textXML, finalMessage)).build();

		String result = null;
		CallbackFuture future = new CallbackFuture();
		client.newCall(request).enqueue(future);
		try {
			Response response = future.get();
			result = response.body().string();
			//System.out.println(response.message());
		} catch (Exception e) {
			System.out.println("Exception.................. " + e.getMessage());
		} 
		return result;
	}
	
	class CallbackFuture extends CompletableFuture<Response> implements Callback {
	    public void onResponse(Call call, Response response) {
	         super.complete(response);
	    }
	    public void onFailure(Call call, IOException e){
	         super.completeExceptionally(e);
	    }
	}

	
	/**
	 * Method for sending XML message to the O-MI sand box (Asynchronously)
	 * 
	 */
	public void sendDataToServerAsync(String url, String finalMessage) {
		MediaType textXml = MediaType.parse("text/xml; charset=utf-8");
		Request request = new Request.Builder().url(url)
				.post(RequestBody.create(textXml, finalMessage)).build();

		Call call = client.newCall(request);
		call.enqueue(new Callback() {

			@Override
			public void onFailure(Call call, IOException e) {
				System.out.println("Failed in callback " + e.getMessage());	
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (!response.isSuccessful()) {
					throw new IOException("Unexpected code " + response);
				} else {
					System.out.println(response.message());
				}

			}

		});
	}
	
	
	/**
	 * Method to send OMI write envelope to the sand box (Another way)
	 * 
	 */
	public void sendData(String url, String finalMessage) {
		HttpURLConnection httpcon = null;
		OutputStream os =  null;
		try {
			httpcon = (HttpURLConnection) ((new URL(url).openConnection()));
			httpcon.setDoOutput(true);
			httpcon.setRequestProperty("Content-Type", "text/xml");
			httpcon.setRequestMethod("POST");
			httpcon.setUseCaches(false);
			byte[] outputBytes = finalMessage.getBytes("UTF-8");
			os = httpcon.getOutputStream();
			os.write(outputBytes);
			System.out.println(httpcon.getResponseMessage());

		} catch (Throwable e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {}
			}

			if (httpcon != null) {
				httpcon.disconnect();
			}
		}
	}

}
