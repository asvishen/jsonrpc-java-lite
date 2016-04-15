package edu.asu.ser.jsonrpc.lite.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class HttpServer extends Thread{
	public Socket socket;
	public int id;
	private Object ob;
	protected static Logger logger;

	
	public HttpServer(Socket socket, int id, Object ob){
		this.socket = socket;
		this.id = id ;
		this.ob = ob;
		logger = LogManager.getLogger("serverLog");
		logger.debug("Listening at port:"+socket);
		
	}

	public void run(){
		try {
	         OutputStream outSock = socket.getOutputStream();
	         InputStream inSock = socket.getInputStream();
	         logger.debug("entering server thread");
	         byte clientInput[] = new byte[4096]; 
	         
	         int numr = inSock.read(clientInput,0,4096);
	         if (numr != -1) {

	            Thread.sleep(200);
	            int ind = numr;
	            while(inSock.available()>0){
	               numr = inSock.read(clientInput,ind,4096-ind);
	               ind = numr + ind;
	               Thread.sleep(200);
	            }
	            String clientString = new String(clientInput,0,ind);
	            logger.debug("Accepted request from client with id "+id);
	            if(clientString.indexOf("{")>=0){
	               String request = clientString.substring(clientString.indexOf("{"));
	               logger.debug("Calling method");
	               System.out.println(request);
	               String response = ServerUtils.callMethod(request,logger,ob);
	               String httpResponse = getHttpResponseFormat(response);
	               logger.debug("Accepted method result");
	               byte clientOut[] = httpResponse.getBytes();
	               System.out.println(response);
	               logger.debug("Sending response back to client");
	               outSock.write(clientOut,0,clientOut.length);
	               logger.debug("Response sent back to client id: "+id);

	               
	            }else{
	               logger.error("No json object in clientString: "+
	                                  clientString);
	            }
	         }
	         inSock.close();
	         outSock.close();
	         socket.close();
	      } catch (Exception e) {
	         logger.error("Can't get I/O for the connection.");
	      }
	}
	
	public String getHttpResponseFormat(String response)
	{
		StringBuilder builder  = new StringBuilder();
	      builder.append("HTTP/1.0 200 Data follows\n");
	      builder.append("Content-Type:text/plain");
	      builder.append("\nContent-Length:").append(response.length());
	      builder.append("\n\n").append(response);
	      return builder.toString();
	}
	
}
	



