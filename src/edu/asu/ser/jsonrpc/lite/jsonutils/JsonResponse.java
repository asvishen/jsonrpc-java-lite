package edu.asu.ser.jsonrpc.lite.jsonutils;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonResponse implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String RPC_VERSION_DEFAULT = "2.0";
	private static final String ID_PROPERTY = "id";
	private static final String RPC_VERSION_PROPERTY = "jsonrpc";
	private static final String RPC_RESULT_PROPERTY="result";
	private static final String RPC_ERROR_PROPERTY="error";
	
	private String response;
	
	private int id;
	
	private boolean error;

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	
	public JSONObject getNormalResponse()
	{
		JSONObject obj = new JSONObject();
		obj.put(ID_PROPERTY, getId());
		obj.put(RPC_VERSION_PROPERTY, RPC_VERSION_DEFAULT);
		obj.put(RPC_RESULT_PROPERTY, getResponse());
		return obj;
	}
	
	public JSONObject getErrorResponse()
	{
		JSONObject obj = new JSONObject();
		obj.put(ID_PROPERTY, getId());
		obj.put(RPC_VERSION_PROPERTY, RPC_VERSION_DEFAULT);
		obj.put(RPC_ERROR_PROPERTY, getResponse());
		return obj;
	}
	
	@Override
	public String toString()
	{
		if(isError()) return getErrorResponse().toString();
		return getNormalResponse().toString();
	}
	


	

}
