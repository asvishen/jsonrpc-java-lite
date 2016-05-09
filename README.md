#jsonrpc-java-lite
#Open Source JSON-RPC Library for Java Application Development

This is an open source JSON-RPC Library designed to be lightweight and highly automated.[Work in Progress]

## Features

- Proxy Pattern Implementation for Client-Server Model
- Client Proxy Generator for TCP and HTTP Client
- Highly Automated implementation for Sending and Receiving of JSON Request & Responses
- Error Reporting using JSON-RPC 2.0 Error Code Specification
- Support for HTTP and TCP Client and Servers
- Support for primitive and user defined object
- Automatic conversion from JSON to Java Objects requires implementing additional methods to users' classes

## Limitations

- Java Collections and Arrays not supported
- Multiple request in batches not supported


## Dependencies

- [JSON-Java](https://github.com/stleary/JSON-java)

## Java Compilation Version

 - Java 1.8

## Usage

### Maven Dependency

```xml
<dependency>
	<groupId>edu.asu.ser</groupId>
	<artifactId>jsonrpc-java-lite</artifactId>
	<version>1.0.0</version>
<dependency>
```
### Implementation

This library packages the JSON-RPC requests and responses in the background for Server and Clients. 
- Define an interface with the methods for which RPC calls need to be made.
- Proxy generator creates the stub for packaging client calls in JSON Request format
- Client defines calls the proxy client implementaton to package the call
- Server provides the actual method implementation

### Requirement for anyone using this library:

- provide .toJson() method for user-defined classes for sending them as Paramters or return type
- provide a constructor for objects taking org.JSON JSONObject as parameters


## Interface 

```java
public interface Calculator {
	public int add(int a, int b);
}
```
## TCP Client

The client proxy generated using the Java Proxy generator extend from the TCPClient and implements the Interface defined
Code below code was generated using Proxy Generator

```java
public class CalculatorProxyTCPClient extends TCPClient implements Calculator{ 

	private static int id = 0;
	
	public CalculatorProxyTCPClient(String url,int port) 
	{
		super(url,port);
	}

	public int add(int param0,int param1) throws NumberFormatException,JsonRpcException
	{ 
		JsonRequest req = new JsonRequest(); 
		JSONArray arr = new JSONArray();
		arr.put(param0);
		arr.put(param1);
		PositionalParams params = new PositionalParams(arr); 
		req.setParams(params); 
		req.setMethod("add"); 
		req.setId(id++);
		Object result = sendRequest(req.toString());
		return (int) result; 
	}
}
```

Create a client object with the address and port number whenever you need to make the RPC call

```java
CalculatorProxyTCPClient client = new CalculatorProxyTCPClient("localhost",8080);
System.out.println(client.add(10, 11));
```

## TCP Server

Implement the interface and provide the actual implementation. Start the server in main

```java
public class CalculatorTCPServer implements Calculator {


	@Override 
	public int add(int a, int b) throws NumberFormatException, JsonRpcException {
		return a+b;
	}
	public static void main(String[] args) throws IOException {
		TCPServer serv = new TCPServer(new CalculatorTCPServer(),8080);
		serv.start();

	}
}

```

## HTTP Client

The client proxy generated using the Java Proxy generator extend from the HTTP and implements the Interface defined
Code below code was generated using Proxy Generator

```java
public class CalculatorProxyHTTPClient extends HttpClient implements Calculator{ 

	private static int id = 0;

	public CalculatorProxyHTTPClient(URL url) 
	{
		super(url);
	}

	public int add(int param0,int param1) throws NumberFormatException,JsonRpcException
	{ 
		JsonRequest req = new JsonRequest(); 
		JSONArray arr = new JSONArray();
		arr.put(param0);
		arr.put(param1);
		PositionalParams params = new PositionalParams(arr); 
		req.setParams(params); 
		req.setMethod("add"); 
		req.setId(id ++);
		Object result = sendRequest(req.toString());
		return (int) result; 
	}
}
```

Create a client object with the HTTP address URL

```java
CalculatorProxyHTTPClient client = new CalculatorProxyHTTPClient(new URL("http://localhost:8080"));
System.out.println("Received response from Server: " + client.add(10, 11));
```

## HTTP Server

Implement the interface and provide the actual implementation. Start the server in main.

```java
public class CalculatorHTTPServer implements Calculator {


	@Override 
	public int add(int a, int b) throws NumberFormatException, JsonRpcException {
		return a+b;
	}
	public static void main(String[] args) throws IOException {
		HttpServer serv = new HttpServer(new CalculatorHTTPServer(),8080);
		serv.start();
		
	}
}
```




