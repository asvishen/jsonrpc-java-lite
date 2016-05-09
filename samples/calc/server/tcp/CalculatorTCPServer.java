package calc.server.tcp;

import java.io.IOException;

import calc.server.Calculator;
import edu.asu.ser.jsonrpc.exception.JsonRpcException;
import edu.asu.ser.jsonrpc.lite.server.TCPServer;

public class CalculatorTCPServer implements Calculator {


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
		TCPServer serv = new TCPServer(new CalculatorTCPServer(),8080);
		serv.start();

	}
}

