package edu.asu.ser.jsonrpc.exception;

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
 * Exception Type for JSON-RPC Errors
 * @version 1.0.0
 * @author: Avijit Vishen avijit.vishen@asu.edu
 * Software Engineering, CIDSE, Arizona State University,Polytechnic Campus
 */

public class JsonRpcException extends Exception{
	

	private static final long serialVersionUID = 1L;
	
	private RPCError error;

	/**
	 * Sets error type for exception 
	 * @param error: RPC error enum type 
	 */
	public JsonRpcException(RPCError error)
	{
		super(error.getErrorJSON().toString());
		this.error = error;
	}
	
	/**
	 * returns error type for object
	 * @return error: Type of error
	 */
	public RPCError getError() {
		return error;
	}
	
	/**
	 * Sets error object for exception 
	 * @param error: Type of error
	 */
	public void setError(RPCError error) {
		this.error = error;
	}
	
	

	
	
	
	
	
	

}
