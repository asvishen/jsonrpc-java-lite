package edu.asu.ser.jsonrpc.lite.client;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
 * Implementation for TCP Client
 * @version 1.0.0
 * @author: Avijit Vishen avijit.vishen@asu.edu
 * Software Engineering, CIDSE, Arizona State University,Polytechnic Campus
 */

public class TCPClient extends AbstractClient{

	public String url;
	public int port;
	protected static Logger logger; 


	public TCPClient(String url,int port){
		this.url = url;		
		this.port = port;
		logger = LogManager.getLogger("clientLog");
	}
	/**
	   *  Method which the call the server method using JSON-RPC
	   * @param request : JSON RPC request in String format
	   * @return result:  object in case of successful execution
	   * @exception JsonRpcException in case of errors during RPC calls
	   */
	@Override
	public Object sendRequest(String request) throws JsonRpcException{
		String response;
		Socket socket;
		try{
			
			socket = new Socket(url,port);
			logger.debug("Connecting to Server...");

			PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
			logger.debug("Writing Contents to Stream");

			out.println(request+'\n');
			logger.debug("Finished writing contents to Stream");
			logger.debug("Waiting for Response...");
			System.out.println(request);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			logger.debug("Response Received from Server");

			response = in.readLine();
			logger.debug("Closing all streams");

			socket.close();
			in.close();
			out.close();
			logger.debug("Closed all streams successfully");
			System.out.println(response);
			return getDeserializedResponse(response);

		}
		catch(IOException ex)
		{
			throw new JsonRpcException(RPCError.INTERNAL_ERROR);
		}
			
	}

}
