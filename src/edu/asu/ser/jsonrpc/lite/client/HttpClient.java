package edu.asu.ser.jsonrpc.lite.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient extends AbstractClient{
	public URL url;

	public HttpClient(URL url){
		this.url = url;
	}
	
	public synchronized String sendRequest(String content){
		HttpURLConnection connection = null;

		try{

			connection = (HttpURLConnection)url.openConnection();
	        connection.addRequestProperty("Accept-Encoding", "application/jsonrequest");
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", 
					"application/jsonrequest");

			connection.setRequestProperty("Content-Length", 
					Integer.toString(content.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");  

			connection.setUseCaches(false);
			connection.setDoOutput(true);
			connection.connect();

			System.out.println(content.getBytes().toString());
			
			DataOutputStream wr = new DataOutputStream (
					connection.getOutputStream());
			wr.write(content.getBytes());
			wr.flush();
			wr.close();

			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder(); 
			String line;
			while((line = rd.readLine()) != null) {
				response.append(line);
				System.out.println(line);
				response.append('\r');
			}
			rd.close();
			
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(connection != null) {
				connection.disconnect(); 
			}

		}
	}
}


