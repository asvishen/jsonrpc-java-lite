package edu.asu.ser.jsonrpc.shared;


public class JsonRpcException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JsonRpcException() {
		super();
	}
	public JsonRpcException(RPCError error)
	{
		super(error.getErrorJSON().toString());
	}

	
	
	
	
	
	

}
