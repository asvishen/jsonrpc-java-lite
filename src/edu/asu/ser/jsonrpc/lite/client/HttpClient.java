package edu.asu.ser.jsonrpc.lite.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.asu.ser.jsonrpc.exception.JsonRpcException;
import edu.asu.ser.jsonrpc.exception.RPCError;

/**
 * Copyright 2016 Avijit Singh Vishen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Implementation for HTTP Client
 * @version 1.0.0
 * @author: Avijit Vishen avijit.vishen@asu.edu
 * Software Engineering, CIDSE, Arizona State University,Polytechnic Campus
 */


public class HttpClient extends AbstractClient{
	public URL url;
	protected static Logger logger; 


	public HttpClient(URL url){
		this.url = url;
		logger = LogManager.getLogger("clientLog");
	}
	

	
	public synchronized Object sendRequest(String content) throws JsonRpcException{
		HttpURLConnection connection = null;

		try{

			connection = (HttpURLConnection)url.openConnection();
	        connection.addRequestProperty("Accept-Encoding", "text/plain");
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", 
					"text/plain");

			connection.setRequestProperty("Content-Length", 
					Integer.toString(content.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");  

			connection.setUseCaches(false);
			connection.setDoOutput(true);
			connection.connect();
			logger.debug("Connecting to Server...");
			
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
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
				response.append('\r');
			}
			rd.close();
			is.close();
			return getDeserializedResponse(response.toString());
		}
		catch(IOException ex)
		{
			throw new JsonRpcException(RPCError.INTERNAL_ERROR);
		}
	}

}


