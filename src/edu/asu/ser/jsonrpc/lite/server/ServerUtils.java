package edu.asu.ser.jsonrpc.lite.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class ServerUtils {

	public static String callMethod(String request, Logger logger, Object ob)
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

	    	  Object methodCallResult = call(method,params,logger,ob);

	    	  result.put("id",id);
	    	  result.put("jsonrpc","2.0");
	    	  result.put("result", methodCallResult==null?"error":methodCallResult);

	      }
	      catch(Exception ex)
	      {
	    	  logger.error(ex.getMessage());
	      }
	      
		  return result.toString();
	}
	
	private static Object call(String method,JSONArray params,Logger logger, Object ob)
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
