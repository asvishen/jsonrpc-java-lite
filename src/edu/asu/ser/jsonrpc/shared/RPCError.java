package edu.asu.ser.jsonrpc.shared;

import org.json.JSONObject;

public enum RPCError {
	PARSE_ERROR(-32700,"Parse Error"),
	INVALID_REQUEST_ERROR(-32600,"Invalid Request Error"),
	METHOD_NOT_FOUND_ERROR(-32601,"Method Not Found"),
	INVALID_PARAMS_ERROR(-32602,"Invalid Method Parameters"),
	INTERNAL_ERROR(-32603, "Internal JSON-RPC Error");
	
	
	private int code;
	private String message;
	private String data;
	
	private RPCError(int code, String message)
	{
		this.code = code;
		this.message = message;
		
	}
	
	public JSONObject getErrorJSON()
	{
		JSONObject obj = new JSONObject();
		obj.put("code",getCode());
		obj.put("message",getMessage());
		if(!data.isEmpty())obj.put("data", getData());
		return obj;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	

}
