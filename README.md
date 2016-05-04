#jsonrpc-java-lite
#Open Source JSON-RPC Library for Java Application Development

This is an open source JSON-RPC Library designed to be lightweight and highly automated.[Work in Progress]

## Features

- Proxy Pattern Implementation for Client-Server Model
- Highly Automated implementation for Sending and Receiving of JSON Request & Responses
- Error Reporting using JSON-RPC 2.0 Error Code Specification
- Support for HTTP and TCP Client and Servers

## Dependencies

- [JSON-Java](https://github.com/stleary/JSON-java)
- [Jackson-datatype-json-org](https://github.com/FasterXML/jackson-datatype-json-org)

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
- Client defines the proxy implementaton. 
- Server provides the actual method implementation

## Interface 

```java
public interface Rectangle
{
  public double calculateArea(int width,int height);
}
```
## TCP Client

The client proxy should extend from the TCPClient and extend the Interface defined

```java
public class MyTCPClient extends TCPClient implements Rectangle 
{
  public MyTCPClient(String url, int port)
  {
    super(url,port);
  }
  
  @Override
  public double calculateArea(int width, int height)
  {
    return (double) callRPCMethod("calculateArea",width,height);
  }
  
}
```

Create a client object with the address and port number

```java
MyTCPClient client = new MyTCPClient("localhost",8080);
cl.calculateArea(5,2);
```

## TCP Server

Implement the interface and provide the actual implementation

```java
public class MyTCPServer implements Rectangle 
{
  
  @Override
  public double calculateArea(int width, int height)
  {
    return width * height;
  }
  
}
```
Start the server

```java
TCPServer server = new TCPServer(new MyTCPServer(),8080);
server.start();
```
## HTTP Client

The client proxy should extend from the HTTPClient and extend the Interface defined

```java
public class MyHTTPClient extends HTTPClient implements Rectangle 
{
  public MyHTTPClient(URL url)
  {
    super(url);
  }
  
  @Override
  public double calculateArea(int width, int height)
  {
    return (double) callRPCMethod("calculateArea",width,height);
  }
  
}
```

Create a client object with the address and port number

```java
MyHTTPClient client = new MyHTTPClient("http://localhost:8080");
cl.calculateArea(5,2);
```

## HTTP Server

Implement the interface and provide the actual implementation

```java
public class MyHTTPServer implements Rectangle 
{
  
  @Override
  public double calculateArea(int width, int height)
  {
    return width * height;
  }
  
}
```
Start the server

```java
HTTPServer server = new HTTPServer(new MyHTTPServer(),8080);
server.start();
```




