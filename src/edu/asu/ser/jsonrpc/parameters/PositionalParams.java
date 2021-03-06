package edu.asu.ser.jsonrpc.parameters;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
 * Positional Parameters representation and methods
 * @version 1.0.0
 * @author: Avijit Vishen avijit.vishen@asu.edu
 * Software Engineering, CIDSE, Arizona State University,Polytechnic Campus
 */

public class PositionalParams implements Serializable {


	private static final long serialVersionUID = 1L;

	protected JSONArray params;
		
	public static final Set<Class<?>> PRIMITIVE_TYPES  = new HashSet<Class<?>>();

	static
	{
		PRIMITIVE_TYPES.add(Boolean.class);
		PRIMITIVE_TYPES.add(Integer.class);
		PRIMITIVE_TYPES.add(Character.class);
		PRIMITIVE_TYPES.add(Byte.class);
		PRIMITIVE_TYPES.add(Short.class);
		PRIMITIVE_TYPES.add(Long.class);
		PRIMITIVE_TYPES.add(Float.class);
		PRIMITIVE_TYPES.add(Double.class);
		PRIMITIVE_TYPES.add(Void.class);
		PRIMITIVE_TYPES.add(String.class);
		
	}
	
	/**
	 * Constructor to set Params from JSON array
	 * @param id: id to be set for request
	 */
	public PositionalParams(JSONArray params)
	{
		this.params = params;

	}

	/**
	 * Sets the value for JSON request parameters by converting Java objects to JSONArray 
	 * @param objects : Parameters objects of RPC method 
	 * @throws JsonRpcException 
	 */
	public void setParamsFromObjects(Object[] objects) throws JsonRpcException
	{
		JSONArray arr = new JSONArray();
		for(Object object: objects)
		{
			if(PRIMITIVE_TYPES.contains(object.getClass()) || object.getClass().isPrimitive())
			{
				arr.put(object);
			} else
				try {
					arr.put(object.getClass().getMethod("toJson", null).invoke(object, null));
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException
						| SecurityException e) {
					throw new JsonRpcException(RPCError.INTERNAL_ERROR);
				}	
		}
		this.params = arr;
	}
	
	/**
	 * Getter for params array
	 * @return params: json array of paramters
	 */
	public JSONArray getParamsJSONArray()
	{
		return params;
	}
	
	 
	
	/**
	 * Converts JSONObjects from Request to Java Types  
	 * @param method : method for which parameters are to be converted
	 * @return Object[]: Objects converted from JSON to Java Objects
	 * @exception IllegalArgumentException when request params cannot be casted to method parameters
	 * @throws JsonRpcException 
	 */
	public Object[] getObjectsFromJSONArray(Method m) throws JsonRpcException
	{
		Object[] objects = new Object[params.length()];
		Class<?>[] paramClasses = m.getParameterTypes();
		
		
		//System.out.println(paramClasses[0].toString() + paramClasses[1].toString());
		for(int i=0;i<params.length();i++)	
		{
			try{
			if(paramClasses[i].isPrimitive()) 
			{
				objects[i] = params.get(i);
			}
			else if (PRIMITIVE_TYPES.contains(paramClasses[i]))
			{
				objects[i] =  paramClasses[i].cast(params.get(i));
			}
			else{
			
				JSONObject jsonObj = new JSONObject(params.get(i));
				objects[i] = paramClasses[i].getConstructor(JSONObject.class).newInstance(jsonObj);
			}
			}catch (ClassCastException | JSONException | NoSuchMethodException | IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException | SecurityException ex)
			{
				ex.printStackTrace();
				throw new JsonRpcException(RPCError.INVALID_PARAMS_ERROR);
			
			}
			
		}
		
		return objects;
		
	}
	
	
}
