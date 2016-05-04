package edu.asu.ser.jsonrpc.lite.client;

import org.json.JSONObject;

import edu.asu.ser.jsonrpc.exception.JsonRpcException;
import edu.asu.ser.jsonrpc.exception.RPCError;
import edu.asu.ser.jsonrpc.lite.jsonutils.JsonRequest;
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
 * Defines the abstract methods for Clients
 * @version 1.0.0
 * @author: Avijit Vishen avijit.vishen@asu.edu
 * Software Engineering, CIDSE, Arizona State University,Polytechnic Campus
 */

public abstract class AbstractClient {
	
	JsonRequest request;
	
	 /**
	   *  Method which the call the server method using JSON-RPC
	   * @param request : JSON RPC request in String format
	   * @return result:  object in case of successful execution
	   * @exception JsonRpcException in case of errors during RPC calls
	   */
	abstract public Object sendRequest(String request) throws JsonRpcException;

	 /**
	   * Method to makes calls from client to the server method using JSON-RPC
	   * @param methodName : name of the server method to be called
	   * @param params: arguments for the RPC method in correct order
	   * @return result:  object in case of successful execution
	   * @exception JsonRpcException in case of errors during RPC calls
	   */
	public synchronized Object callRPCMethod(String methodName, Object... params) throws JsonRpcException
	{
			request = new JsonRequest();
			request.setMethod(methodName);
			request.setParams(params);
			return sendRequest(request.toString());
	}
	
	/**
	 * Fetches the result from the JSON RPC Response 
	 * @param response : RPC response in string format
	 * @return result:  object retrieved from Result object in response
	 * @exception JsonRpcException in case response contains error object
	 */
	protected Object getDeserializedResponse(String response) throws JsonRpcException{
		
		if(response.contains("{")){
			String jsonResult = response.substring(response.indexOf('{'));
			JSONObject obj = new JSONObject(jsonResult);
			if(obj.has("error"))
			{
				RPCError err = RPCError.getErrorType(obj);
				throw new JsonRpcException(err);
			}

			JsonResponse resp = JsonResponse.getResponse(obj);
			return resp.getResult();
		}
		return null;
	}
	
	
	
	
	
	
}
