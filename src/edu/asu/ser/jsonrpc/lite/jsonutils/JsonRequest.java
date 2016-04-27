package edu.asu.ser.jsonrpc.lite.jsonutils;

import java.io.Serializable;

import org.json.JSONObject;

import edu.asu.ser.jsonrpc.parameters.PositionalParams;

public class JsonRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String RPC_VERSION_DEFAULT = "2.0";
	private static final String ID_PROPERTY = "id";
	private static final String RPC_VERSION_PROPERTY = "jsonrpc";
	private static final String METHOD_PROPERTY = "method";
	private static final String PARAMS_PROPERTY = "params";
	
	protected String method;
	
	protected PositionalParams params;
	
	protected int id;
	

	public JsonRequest()
	{
		params = new PositionalParams();
		
	}
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public PositionalParams getParams() {
		return params;
	}

	public void setParams(Object[] ob) throws IllegalArgumentException{
		params.setParamsFromObjects(ob);
	}
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	@Override
	public String toString()
	{
		JSONObject obj = new JSONObject();
		obj.put(ID_PROPERTY, getId());
		obj.put(RPC_VERSION_PROPERTY, RPC_VERSION_DEFAULT);
		obj.put(METHOD_PROPERTY, getMethod());
		obj.put(PARAMS_PROPERTY, getParams().getParamsJSONArray());
		return obj.toString();
	}

	

	

}
