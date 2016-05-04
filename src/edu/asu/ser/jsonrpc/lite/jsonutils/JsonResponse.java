package edu.asu.ser.jsonrpc.lite.jsonutils;

import java.io.Serializable;

import org.json.JSONObject;

import edu.asu.ser.jsonrpc.exception.JsonRpcException;

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
 * Implementation for Json Reponse Object 
 * @version 1.0.0
 * @author: Avijit Vishen avijit.vishen@asu.edu
 * Software Engineering, CIDSE, Arizona State University,Polytechnic Campus
 */
public class JsonResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final String RPC_VERSION_DEFAULT = "2.0";
	private static final String ID_PROPERTY = "id";
	private static final String RPC_VERSION_PROPERTY = "jsonrpc";
	private static final String RPC_RESULT_PROPERTY="result";
	private static final String RPC_ERROR_PROPERTY="error";
	
	private Object result;
	
	private int id;
	
	private boolean error = false;

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object response) {
		this.result = response;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Returns the response in form of JSON object when no error is encountered
	 * @return obj :  response packaged in JSON format
	 */
	
	private JSONObject getNormalResponse()
	{
		JSONObject obj = new JSONObject();
		obj.put(ID_PROPERTY, getId());
		obj.put(RPC_VERSION_PROPERTY, RPC_VERSION_DEFAULT);
		obj.put(RPC_RESULT_PROPERTY, getResult());
		return obj;
	}
	
	/**
	 * Returns the response in form of JSON object when an error is encountered
	 * @return obj :  response packaged in JSON format
	 */
	private JSONObject getErrorResponse()
	{
		JSONObject obj = new JSONObject();
		obj.put(ID_PROPERTY, getId());
		obj.put(RPC_VERSION_PROPERTY, RPC_VERSION_DEFAULT);
		obj.put(RPC_ERROR_PROPERTY, getResult());
		return obj;
	}
	/**
	 * Converts the response object to an error or normal response
	 * @return response :  response JSON converted to String format
	 */
	@Override
	public String toString()
	{
		if(isError()) return getErrorResponse().toString();
		return getNormalResponse().toString();
	}
	
	/**
	 * Returns the object of this class from a JSONObject 
	 * @return obj :  response packaged in JSON format
	 */
	public static JsonResponse getResponse(JSONObject obj)
	{
		JsonResponse resp = new JsonResponse();
		resp.setResult(obj.get(RPC_RESULT_PROPERTY));
		resp.setId(obj.getInt(ID_PROPERTY));
		return resp;
		
	}

	

}
