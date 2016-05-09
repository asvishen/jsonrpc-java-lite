package edu.asu.ser.jsonrpc.exception;

import java.io.Serializable;

import org.json.JSONObject;

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
 * Enumerator for Errors following JSON RPC Specification.
 * 
 * @version 1.0.0
 * @author: Avijit Vishen avijit.vishen@asu.edu
 * Software Engineering, CIDSE, Arizona State University,Polytechnic Campus
 */

public enum RPCError implements Serializable{
	PARSE_ERROR(-32700,"Parse Error"),
	INVALID_REQUEST_ERROR(-32600,"Invalid Request Error"),
	METHOD_NOT_FOUND_ERROR(-32601,"Method Not Found"),
	INVALID_PARAMS_ERROR(-32602,"Invalid Method Parameters"),
	INTERNAL_ERROR(-32603, "Internal JSON-RPC Error");
	
	
	private int code;
	
	private String message;
	
	private String data;
	
	/**
	 * Private contructor for error enumerator
	 * @param code: error code from enum
	 * @param message: error message from enum
	 */
	private RPCError(int code, String message)
	{
		this.code = code;
		this.message = message;
	}
	
	/**
	 * Returns the corresponding error type from a JSON Error object 
	 * @param error : JSON error object from RPC response
	 * @return RPCError :  RPCError enum
	 * @exception JsonRpcException in case response contains error object
	 */
	public static RPCError getErrorType(JSONObject response)
	{
		System.out.println(response);
		JSONObject error = new JSONObject(response.get("error"));
		System.out.println(error);

		for(RPCError err: RPCError.values())
		{
			if(error.get("code").equals(err.getCode()))
			{
				return err;
			}
		}
		return null;
	}
	
	
	/**
	 * Method to get the result from the JSON RPC Response 
	 * @param response : RPC response in string format
	 * @return result:  object retrieved from Result object in response
	 * @exception JsonRpcException in case response contains error object
	 */
	public JSONObject getErrorJSON()
	{
		JSONObject obj = new JSONObject();
		obj.put("code",getCode());
		obj.put("message",getMessage());
		if(data!=null)obj.put("data", getData());
		return obj;
	}

	/**
	 * Getter for code member of error
	 * @return code: error code 
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Sets code for an error object 
	 * @param code: code to be set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * returns message from error object 
	 * @return message: error message 
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets message for an error object 
	 * @param message: error message 
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Getter for data for error object
	 * @return data: data member for error object
	 */
	public String getData() {
		return data;
	}

	/**
	 * Sets message for an error object 
	 * @param data: error message 
	 */
	public void setData(String data) {
		this.data = data;
	}
	
	

}
