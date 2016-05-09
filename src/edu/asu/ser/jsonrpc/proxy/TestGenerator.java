package edu.asu.ser.jsonrpc.proxy;

import calc.server.Calculator;
import edu.asu.ser.jsonrpc.lite.client.HttpClient;

public class TestGenerator {
	public static void main(String args[])
	{
		SwiftClientProxyGenerator.generateSwiftProxy(Calculator.class,HttpClient.class);
		
//		SwiftClientProxyGenerator.generateSwiftProxy(Calculator.class, TCPClient.class);
		
//		JavaClientProxyGenerator.generateJavaProxy(WaypointServer.class, HttpClient.class);
//		
//		JavaClientProxyGenerator.generateJavaProxy(WaypointServer.class, TCPClient.class);

		
		
	}
	
}
