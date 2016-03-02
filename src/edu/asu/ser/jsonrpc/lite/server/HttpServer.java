package edu.asu.ser.jsonrpc.lite.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class HttpServer extends Thread{
	public Socket socket;
	public int id;
	private Object ob;
	protected static Logger logger;

	
	public HttpServer(Socket socket, int id, Object ob){
		this.socket = socket;
		this.id = id ;
		this.ob = ob;
		logger = LogManager.getLogger("clientLog");
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
	               
	               String response = callMethod(request);
	               logger.debug("Accepted method result");
	               byte clientOut[] = response.getBytes();
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
	
	public String callMethod(String request)
	{
	      JSONObject result = new JSONObject();
	      try{
	    	  JSONObject theCall = new JSONObject(request);
	    	  String method = theCall.getString("method");
	    	  int id = theCall.getInt("id");
	    	  JSONArray params = null;
	    	  if(!theCall.isNull("params")){
	    		  params = theCall.getJSONArray("params");
	    	  }

	    	  Object methodCallResult = call(method,params);

	    	  result.put("id",id);
	    	  result.put("jsonrpc","2.0");
	    	  result.put("result", methodCallResult==null?"error":methodCallResult);

	      }
	      catch(Exception ex)
	      {
	    	  logger.error(ex.getMessage());
	      }
	      StringBuilder builder  = new StringBuilder();
	      builder.append("HTTP/1.0 200 Data follows\n");
	      builder.append("Content-Type:text/plain");
	      builder.append("\nContent-Length:").append(result.toString().length());
	      builder.append("\n\n").append(result.toString());
	      
		  return builder.toString();
	}
	
	protected Object call(String method,JSONArray params)
	{
		try {
			System.out.println("methodName=" + method);
			Method myMethod ;
			
			
			if(params.length()>0){
				myMethod = ob.getClass().getDeclaredMethod(method, JSONArray.class);
				return (Object) myMethod.invoke(ob, params);
			
			}
			else{
				myMethod = ob.getClass().getDeclaredMethod(method);
				return (Object) myMethod.invoke(ob);
			}
	
		} catch (NoSuchMethodException | SecurityException e) {
				logger.error("Error calling method:" + e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("Arguments not valid" + e.getMessage());
		} catch (InvocationTargetException e) {
			logger.error("Error invoking method:" + e.getMessage());
		} catch (IllegalAccessException e) {
			logger.error("Cannot access Method:" + e.getMessage());
		}
		
		return null;
	}

}
	



