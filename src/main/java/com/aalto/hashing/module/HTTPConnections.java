package com.aalto.hashing.module;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HTTPConnections {

	public String sendDataToServer(String url, String finalMessage) {
		OkHttpClient client = new OkHttpClient.Builder().connectTimeout(100, TimeUnit.SECONDS)
				.writeTimeout(50, TimeUnit.SECONDS)
				.readTimeout(50, TimeUnit.SECONDS).build();

		MediaType textXml = MediaType.parse("text/xml; charset=utf-8");
		Request request = new Request.Builder().url(url)
				.post(RequestBody.create(textXml, finalMessage)).build();

		Response response = null;
		String result = null;
		try {
			response = client.newCall(request).execute();
			System.out.println(response.message());
			result =  response.body().string();
			response.close();  
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getDataFromServer(String url, String finalMessage) {
		//System.out.println(finalMessage);
		OkHttpClient client = new OkHttpClient.Builder()
				.connectTimeout(100, TimeUnit.SECONDS)
				.writeTimeout(100, TimeUnit.SECONDS)
				.readTimeout(100, TimeUnit.SECONDS).build();

		MediaType textXML = MediaType.parse("text/xml; charset=utf-8");
		Request request = new Request.Builder().url(url)
				.post(RequestBody.create(textXML, finalMessage)).build();

		Response response = null;
		String result = null;
		try {
			response = client.newCall(request).execute();
			System.out.println(response.message());
			result =  response.body().string();
			response.close();  

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result; 
	}


	public void sendDataToServerAsync(String url, String finalMessage) {
		OkHttpClient client = new OkHttpClient.Builder().connectTimeout(100, TimeUnit.SECONDS)
				.writeTimeout(50, TimeUnit.SECONDS)
				.readTimeout(50, TimeUnit.SECONDS).build();

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

}
