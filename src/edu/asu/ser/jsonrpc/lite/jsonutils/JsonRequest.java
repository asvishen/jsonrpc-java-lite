package edu.asu.ser.jsonrpc.lite.jsonutils;

import java.io.Serializable;

import org.json.JSONObject;

import edu.asu.ser.jsonrpc.exception.JsonRpcException;
import edu.asu.ser.jsonrpc.parameters.PositionalParams;

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
 * Implementation for JSON Request Object
 * @version 1.0.0
 * @author: Avijit Vishen avijit.vishen@asu.edu
 * Software Engineering, CIDSE, Arizona State University,Polytechnic Campus
 */

public class JsonRequest implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private static final String RPC_VERSION_DEFAULT = "2.0";
	private static final String ID_PROPERTY = "id";
	private static final String RPC_VERSION_PROPERTY = "jsonrpc";
	private static final String METHOD_PROPERTY = "method";
	private static final String PARAMS_PROPERTY = "params";
	
	protected String method;
	
	protected PositionalParams params;
	
	protected int id;
	
	/**
	 * Sets method for a request object 
	 * @param method: method name to be set 
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * Sets parameters from objects to positional params object
	 * @param objects: array of objects to be set
	 */
	public void setParams(Object[] ob) throws JsonRpcException{
		params.setParamsFromObjects(ob);
	}
	
	/**
	 * Sets params object for a request 
	 * @param params: Positional Params object to be set
	 */
	public void setParams(PositionalParams params)
	{
		this.params = params;
	}
	
	/**
	 * Sets id  for a request 
	 * @param id: id to be set for request
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Returns the String representation of the JSON format of RPC Request
	 * @return obj :  request object in String JSON format
	 */
	@Override
	public String toString()
	{
		JSONObject obj = new JSONObject();
		obj.put(ID_PROPERTY, id);
		obj.put(RPC_VERSION_PROPERTY, RPC_VERSION_DEFAULT);
		obj.put(METHOD_PROPERTY, method);
		obj.put(PARAMS_PROPERTY, params.getParamsJSONArray());
		return obj.toString();
	}

	

	

}
