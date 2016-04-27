package edu.asu.ser.jsonrpc.lite.client;

import edu.asu.ser.jsonrpc.lite.jsonutils.JsonRequest;

public abstract class AbstractClient {

	
	
	abstract public String sendRequest(String request);
	 
	public synchronized String call(String methodName, Object... params)
	{
			JsonRequest request = new JsonRequest();
			request.setMethod(methodName);
			request.setParams(params);
			return sendRequest(request.toString());
	}
	
	
	
	
}
