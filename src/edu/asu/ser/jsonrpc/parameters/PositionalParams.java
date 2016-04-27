package edu.asu.ser.jsonrpc.parameters;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;

public class PositionalParams implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected JSONArray params;
	
	private ObjectMapper mapper;
	
	private static final Set<Class<?>> PRIMITIVE_TYPES  = new HashSet<Class<?>>();

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
		
	}
	
	public PositionalParams()
	{
		mapper= new ObjectMapper();
		mapper.registerModule(new JsonOrgModule());
	}
	
	public PositionalParams(JSONArray params)
	{
		this.params = params;
		mapper= new ObjectMapper();
		mapper.registerModule(new JsonOrgModule());
	}


	public void setParamsFromObjects(Object[] objects)
	{
		JSONArray arr = new JSONArray();
		for(Object object: objects)
		{
			if(PRIMITIVE_TYPES.contains(object.getClass()))
			{
				arr.put(object);
			}
			else arr.put(mapper.convertValue(object, JSONObject.class));	
		}
		this.params = arr;
	}
	
	public JSONArray getParamsJSONArray()
	{
		return params;
	}
	
	public Object[] getObjectsFromJSONArray(Method m) throws IllegalArgumentException
	{
		Object[] objects = new Object[params.length()];
		Class<?>[] paramClasses = new Class<?>[m.getParameterCount()];
		for(int i=0;i<objects.length;i++)	
		{
			try{
				JSONObject jsonObj = new JSONObject(params.get(i));
				objects[i] = mapper.convertValue(jsonObj, paramClasses[i]);
			}catch (JSONException ex)
			{

				objects[i] = mapper.convertValue(params.get(i), paramClasses[i]);
			}
		}
		
		return objects;
		
	}
	
	
}
