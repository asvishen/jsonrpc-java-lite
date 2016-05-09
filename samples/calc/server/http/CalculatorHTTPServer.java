package calc.server.http;

import java.io.IOException;

import calc.server.Calculator;
import edu.asu.ser.jsonrpc.exception.JsonRpcException;
import edu.asu.ser.jsonrpc.lite.server.HttpServer;

public class CalculatorHTTPServer implements Calculator {


	@Override 
	public int add(int a, int b) throws NumberFormatException, JsonRpcException {
		return a+b;
	}
	
	@Override
	public int sub(int a, int b) throws NumberFormatException, JsonRpcException {
		return a-b;
	}

	@Override
	public int mul(int a, int b) throws NumberFormatException, JsonRpcException {
		return a*b;
	}

	@Override
	public int div(int a, int b) throws NumberFormatException, JsonRpcException {
		return a/b;
	}
	
	public static void main(String[] args) throws IOException {
		HttpServer serv = new HttpServer(new CalculatorHTTPServer(),8080);
		serv.start();
		
	}
}