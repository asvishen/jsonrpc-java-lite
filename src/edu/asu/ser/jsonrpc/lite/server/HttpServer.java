package edu.asu.ser.jsonrpc.lite.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.asu.ser.jsonrpc.lite.jsonutils.JsonResponse;

public class HttpServer extends Thread{
	public int socket;
	
	public int id;
	private Object ob;
	private static ServerSocket servSock;
	protected static Logger logger;
	private static Socket sock;

	
	public HttpServer(Object ob,int socket) throws IOException{
		this.socket = socket;
		servSock = new ServerSocket(socket);
		
		this.ob = ob;
		logger = LogManager.getLogger("serverLog");
		logger.debug("Listening at port:"+socket);
		
	}

	public void start() 
	{
		try {
			sock = servSock.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
		new Thread(this);

	}
	public void run(){
		try {
			 
	         OutputStream outSock = sock.getOutputStream();
	         InputStream inSock = sock.getInputStream();
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
	            //logger.debug("Accepted request from client with id "+id);
	            if(clientString.indexOf("{")>=0){
	               String request = clientString.substring(clientString.indexOf("{"));
	               
	               System.out.println(request);
	               
	               String httpResponse = "";
	               
	               JsonResponse response = ServerUtils.callMethod(request,logger,ob);
	               
	               if(response.isError()) httpResponse = getHttpResponseFormat(response);

	               else httpResponse = getHttpResponseFormat(response);
	               
	               byte clientOut[] = httpResponse.getBytes();
	               
	               System.out.println(response);
	               outSock.write(clientOut,0,clientOut.length);

	               
	            }else{
	               logger.error("No json object in clientString: "+
	                                  clientString);
	            }
	         }
	         inSock.close();
	         outSock.close();
	         sock.close();
	      } catch (IOException e) {
	         logger.error("Can't get I/O for the connection.");
	      } catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public String getHttpResponseFormat(JsonResponse response)
	{
		  StringBuilder builder  = new StringBuilder();
		  builder.append("HTTP/1.0");
	      if(response.isError())builder.append("400\n");
	      else builder.append("200\n");
	      builder.append("Content-Type:application/jsonrpc\n");
	      builder.append("\nContent-Length:").append(response.toString().length());
	      builder.append("\n").append(response);
	      return builder.toString();
	}
	
}
	



