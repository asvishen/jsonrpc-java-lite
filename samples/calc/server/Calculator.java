package calc.server;

import edu.asu.ser.jsonrpc.exception.JsonRpcException;

public interface Calculator {

	public int add(int a, int b) throws NumberFormatException, JsonRpcException;
	
	public int sub(int a, int b) throws NumberFormatException, JsonRpcException;

	public int mul(int a, int b) throws NumberFormatException, JsonRpcException;

	public int div(int a, int b) throws NumberFormatException, JsonRpcException;

}
