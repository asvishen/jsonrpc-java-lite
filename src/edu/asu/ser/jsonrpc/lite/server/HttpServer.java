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
 * Implementation for HTTP Server
 * @version 1.0.0
 * @author: Avijit Vishen avijit.vishen@asu.edu
 * Software Engineering, CIDSE, Arizona State University,Polytechnic Campus
 */

import java.io.IOException;
import java.net.Socket;

import edu.asu.ser.jsonrpc.lite.jsonutils.JsonResponse;

public class HttpServer extends AbstractServer{

	private Socket sock;
	public HttpServer(Object ob,int socket) throws IOException{
		super(ob,socket);
		
	}

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

	

	@Override
	public String getResponseInFormat(JsonResponse response) {
		  StringBuilder builder  = new StringBuilder();
		  builder.append("HTTP/1.0 ");
	      if(response.isError())builder.append("400\n");
	      else builder.append("200 OK\n");
	      builder.append("Content-Type: text/plain\n");
	      builder.append("Content-Length:").append(response.toString().length());
	      builder.append("\n\n").append(response);
	      return builder.toString();
	}
	
}
	



