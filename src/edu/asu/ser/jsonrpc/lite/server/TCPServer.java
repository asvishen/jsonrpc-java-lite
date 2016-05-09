package edu.asu.ser.jsonrpc.lite.server;

import java.io.IOException;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;

import edu.asu.ser.jsonrpc.exception.JsonRpcException;
import edu.asu.ser.jsonrpc.lite.jsonutils.JsonResponse;

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
 * Implementation for TCP Server
 * @version 1.0.0
 * @author: Avijit Vishen avijit.vishen@asu.edu
 * Software Engineering, CIDSE, Arizona State University,Polytechnic Campus
 */


public class TCPServer extends AbstractServer{

	private Socket sock;
	
	/**
	 * Constructor for the TCP Server 
	 * @param ob : object of class with server methods
	 * @param socket: socket number at which server will accept requests
	 * @throws IOException: in case socket is not initialized 
	 */
	public TCPServer(Object ob,int socket) throws IOException{
		super(ob,socket);       
		logger = LogManager.getLogger("serverLog");
		logger.debug("Listening at port:"+socket);
		
	}
	/**
	 * Starts the TCP Server 
	 * @throws JsonRpcException: in case of abnormal execution 
	 */
	public void start() 
	{
		try {
			while(true){
				sock = getServSock().accept();
				new Thread(new ServerExecutor(this,sock,getOb())).run();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the TCP response on top of JSON response for transport
	 * @param response : JSONresponse object to be put inside the outer protocol format
	 * @return response :  packaged String response in format of the protocol for the server
	 */
	@Override
	public String getResponseInFormat(JsonResponse response) {
		return response.toString();
	}

}
