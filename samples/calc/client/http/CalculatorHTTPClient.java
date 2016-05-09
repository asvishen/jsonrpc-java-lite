package calc.client.http;

import java.net.MalformedURLException;
import java.net.URL;

import edu.asu.ser.jsonrpc.exception.JsonRpcException;

public class CalculatorHTTPClient {
	
	public static void main(String args[]) throws MalformedURLException, NumberFormatException, JsonRpcException
	{
		CalculatorProxyHTTPClient client = new CalculatorProxyHTTPClient(new URL("http://localhost:8080"));
		System.out.println("Received response from Server: " + client.add(10, 11));
	}

}
