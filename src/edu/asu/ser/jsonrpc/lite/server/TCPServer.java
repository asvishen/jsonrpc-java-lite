package edu.asu.ser.jsonrpc.lite.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class TCPServer extends Thread{

	public Socket socket;
	public int id;
	private Object ob;
	protected static Logger logger;

	
	public TCPServer(Socket socket, int id, Object ob){
		this.socket = socket;
		this.id = id ;
		this.ob = ob;
		logger = LogManager.getLogger("serverLog");
		logger.debug("Listening at port:"+socket);
		
	}
	
	
	public void run()
	{
		try{
			logger.debug("Starting new thread");
			PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line;
			while((line =in.readLine()) != null)
			{
				logger.info("client request: "+line);
				if(line.indexOf("{")>=0){
					String request = line.substring(line.indexOf("{"));
					logger.debug("Calling method");
					String response = callMethod(request);
					logger.debug("Accepted method result");
					System.out.println(response);
					logger.debug("Sending response back to client");
					out.println(response);
					logger.debug("Response sent back to client id: "+id);


				}else{
					logger.error("No json object in clientString: "+
							line);
				}
			}
			out.close();
			in.close();
			socket.close();
		}
		catch(IOException e)
		{
			logger.error("Error in TCP connection:"+e.getLocalizedMessage());
		}
		 
	}
	public String callMethod(String request)
	{
		JSONObject result = new JSONObject();
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

		return result.toString();
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
