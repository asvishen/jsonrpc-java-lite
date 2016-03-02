package edu.asu.ser.jsonrpc.lite.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class HttpClient extends AbstractClient{
	public URL url;
	protected static Logger logger; 


	public HttpClient(URL url){
		this.url = url;
		logger = LogManager.getLogger("serverLog");
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
			logger.debug("Connecting to Server...");
			
			DataOutputStream wr = new DataOutputStream (
					connection.getOutputStream());
			logger.debug("Writing Contents to Stream");
			
			wr.write(content.getBytes());
			logger.debug("Finished writing contents to Stream");
			wr.flush();
			wr.close();
			logger.debug("Waiting for Response...");
			InputStream is = connection.getInputStream();
			logger.debug("Response Received from Server");
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
			logger.error(e.getLocalizedMessage());
			return null;
		} finally {
			if(connection != null) {
				logger.debug("Disconnecting from server");
				connection.disconnect(); 
			}

		}
	}
}


