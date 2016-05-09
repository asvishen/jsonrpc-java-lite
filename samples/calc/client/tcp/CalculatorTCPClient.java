package calc.client.tcp;

import edu.asu.ser.jsonrpc.exception.JsonRpcException;

public class CalculatorTCPClient {
	public static void main(String args[]) throws NumberFormatException, JsonRpcException
	{
		CalculatorProxyTCPClient client = new CalculatorProxyTCPClient("localhost",8080);
		System.out.println("Received response from Server: " + client.add(10, 11));
	}
}
