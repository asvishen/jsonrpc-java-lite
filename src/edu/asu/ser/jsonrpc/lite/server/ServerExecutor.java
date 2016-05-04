package edu.asu.ser.jsonrpc.lite.server;

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
 * Executes functions for a threaded Server
 * @version 1.0.0
 * @author: Avijit Vishen avijit.vishen@asu.edu
 * Software Engineering, CIDSE, Arizona State University,Polytechnic Campus
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.asu.ser.jsonrpc.lite.jsonutils.JsonResponse;

public class ServerExecutor implements Runnable
{

	private AbstractServer server;
	private Socket sock;
	private Logger logger;
	private Object ob;
	
	public ServerExecutor(AbstractServer server, Socket sock, Object ob) {
		this.server = server;
	    this.sock = sock;
		this.ob = ob;
		logger = LogManager.getLogger("serverLog");
		
	}
	

	@Override
	public void run() {
		try {
			 
	         OutputStream outSock = sock.getOutputStream();
	         InputStream inSock = sock.getInputStream();
	         logger.debug("entering server thread");
	         byte clientInput[] = new byte[4096]; 
	         
	         int numr = inSock.read(clientInput,0,4096);
	         if (numr != -1) {
	            Thread.sleep(200);
	            int ind = numr;
	            while(inSock.available()>0){
	               numr = inSock.read(clientInput,ind,4096-ind);
	               ind = numr + ind;
	               Thread.sleep(200);
	            }
	            String clientString = new String(clientInput,0,ind);
	            if(clientString.indexOf("{")>=0){
	               String request = clientString.substring(clientString.indexOf("{"));
	               
	               System.out.println(request);
	               
	               String strResponse = "";
	               
	               JsonResponse response = server.callServerMethod(request,logger,ob);
	               
	               strResponse = server.getResponseInFormat(response);
	            		  
	               byte clientOut[] = strResponse.getBytes();
	               
	               System.out.println(strResponse);
	               
	               outSock.write(clientOut,0,clientOut.length);

	               
	            }else{
	               logger.error("No json object in clientString: "+
	                                  clientString);
	            }
	         }
	         inSock.close();
	         outSock.close();
	         sock.close();
	      } catch (IOException e) {
	         logger.error("Can't get I/O for the connection.");
	      } catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
}
