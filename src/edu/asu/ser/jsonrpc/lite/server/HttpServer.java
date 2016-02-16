package edu.asu.ser.jsonrpc.lite.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class HttpServer extends Thread{
	public Socket socket;
	public int id;
	private Object ob;
	
	public HttpServer(Socket socket, int id, Object ob){
		this.socket = socket;
		this.id = id ;
		this.ob = ob;
		
	}

	public void run(){
		try {
	         OutputStream outSock = socket.getOutputStream();
	         InputStream inSock = socket.getInputStream();
	         System.out.println("entering server thread");
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

	            if(clientString.indexOf("{")>=0){
	               String request = clientString.substring(clientString.indexOf("{"));
	               String response = callMethod(request);
	               byte clientOut[] = response.getBytes();
	               System.out.println(response);
	               outSock.write(clientOut,0,clientOut.length);
	            }else{
	               System.out.println("No json object in clientString: "+
	                                  clientString);
	            }
	         }
	         inSock.close();
	         outSock.close();
	         socket.close();
	      } catch (Exception e) {
	         System.out.println("Can't get I/O for the connection.");
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
	    	  ex.printStackTrace();
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
				e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
	



