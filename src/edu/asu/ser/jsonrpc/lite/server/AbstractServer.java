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
 * Defines the Abstract Server with Generic Methods for servers.
 * @version 1.0.0
 * @author: Avijit Vishen avijit.vishen@asu.edu
 * Software Engineering, CIDSE, Arizona State University,Polytechnic Campus
 */

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.asu.ser.jsonrpc.exception.JsonRpcException;
import edu.asu.ser.jsonrpc.exception.RPCError;
import edu.asu.ser.jsonrpc.lite.jsonutils.JsonResponse;
import edu.asu.ser.jsonrpc.parameters.PositionalParams;

public abstract class AbstractServer{
	
	public int id;
	private Object ob;
	private ServerSocket servSock;
	protected Logger logger;
	
	/**
	 * Returns the correct response on top of JSON response for transport
	 * @param response : JSONresponse object to be put inside the outer protocol format
	 * @return response :  packaged String response in format of the protocol for the server
	 */
	public abstract String getResponseInFormat(JsonResponse response);
	
	/**
	 * Starts the Server 
	 * @throws JsonRpcException: in case of abnormal execution during method call using Reflections.
	 */
	public abstract void start();
	
	public Object getOb() {
		return ob;
	}

	public void setOb(Object ob) {
		this.ob = ob;
	}

	public ServerSocket getServSock() {
		return servSock;
	}

	public void setServSock(ServerSocket servSock) {
		this.servSock = servSock;
	}


	
	protected AbstractServer(Object ob, int socket) throws IOException
	{
		servSock = new ServerSocket(socket);
		this.ob = ob;
		logger = LogManager.getLogger("serverLog");
	}
	
	
	/**
	 * Method returns the JSONResponse after executing RPC method
	 * @param request : JSONRequest in string form
	 * @param logger : Apache logging object
	 * @param ob : object of class which has actual implementation of RPC method
	 * @return JsonResponse:  result of calling the method in a response format
	 */
	public JsonResponse callServerMethod(String request, Logger logger, Object ob) 
	{
	      JSONObject result = new JSONObject();

	      JSONObject req = new JSONObject(request);
	      String method = req.getString("method");
	      int id = req.getInt("id");
	      JSONArray params = null;
	      if(!req.isNull("params")){
	    	  params = req.getJSONArray("params");
	      }
	      result.put("jsonrpc","2.0");
	      JsonResponse response = new JsonResponse();
	      try{
		      Object methodCallResult = invokeRPCMethod(method,params,logger,ob);
		      response.setResult(methodCallResult);
		      response.setId(id);
	      }catch(JsonRpcException ex)
	      {
		      response.setError(true);
	    	  response.setResult(ex.getMessage());

	      }

		  return response;
	}
	
	/**
	 * Returns the result object by invoking the method
	 * @param method : method to be called
	 * @param params : method params in JSON format from Request
	 * @param logger : Apache logging object
	 * @param ob : object of class which has actual implementation of RPC method
	 * @return object :  result of calling the method
	 * @throws JsonRpcException: in case of abnormal execution during method call using Reflections.
	 */
	private Object invokeRPCMethod(String method, JSONArray params, Logger logger, Object ob) throws JsonRpcException
	{
		
		try {

			System.out.println("methodName=" + method);
			Method myMethod  = getMethodByName(method, ob, params);
			
			if(myMethod == null) throw new JsonRpcException(RPCError.METHOD_NOT_FOUND_ERROR);
			
			if(params.length()>0){
				Object[] paramObjects = getParamObjects(myMethod, params);
				return (Object) myMethod.invoke(ob, paramObjects);
			
			}
			else{
				myMethod = ob.getClass().getDeclaredMethod(method);
				return (Object) myMethod.invoke(ob);
			}
	
		} catch (NoSuchMethodException | SecurityException e) {
				logger.error("Error calling method:" + e.getMessage());
				throw new JsonRpcException(RPCError.METHOD_NOT_FOUND_ERROR);
		} catch (IllegalArgumentException e) {
			logger.error("Arguments not valid" + e.getMessage());
			throw new JsonRpcException(RPCError.INVALID_PARAMS_ERROR);
		} catch (InvocationTargetException | IllegalAccessException e) {
			logger.error("Error invoking method:" + e.getMessage());
			throw new JsonRpcException(RPCError.INTERNAL_ERROR);
		}
		
	}

	/**
	 * Returns the method object by matching names with all class methods of user's server implementation
	 * @param name : method to find
	 * @param params : method params in JSON format from Request
	 * @param ob : object of class which has actual implementation of RPC method
	 * @return method :  method object referencing the method in the class 
	 */
	private  Method getMethodByName(String name, Object ob, JSONArray params)
	{
		Method[] allMethods = ob.getClass().getMethods();
		for(Method method : allMethods)
		{
			if(method.getName().equals(name) && method.getParameterCount() == params.length())
			{
				return method;
			}
		}
		return null;
	}
	
	/**
	 * Returns the objects for parameters from the JSON request
	 * @param method : method object reference
	 * @param params : method params in JSONArray format from Request
	 * @return object :  Objects converted from JSON to Java 
	 * @throws JsonRpcException: in case object conversion fails
	 */
	private Object[]  getParamObjects(Method method, JSONArray params) throws JsonRpcException {
		
		PositionalParams  pm = new PositionalParams(params);
		Object[] objects = null;
		try
		{
			objects = pm.getObjectsFromJSONArray(method);

		}
		catch(IllegalArgumentException ex)
		{
			throw new JsonRpcException(RPCError.INVALID_PARAMS_ERROR);
		}

		return objects;
	}
	
	
	

}
