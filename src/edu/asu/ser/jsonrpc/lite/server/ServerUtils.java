package edu.asu.ser.jsonrpc.lite.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.asu.ser.jsonrpc.lite.jsonutils.JsonResponse;
import edu.asu.ser.jsonrpc.parameters.PositionalParams;
import edu.asu.ser.jsonrpc.shared.JsonRpcException;
import edu.asu.ser.jsonrpc.shared.RPCError;

public class ServerUtils {

	public static JsonResponse callMethod(String request, Logger logger, Object ob) 
	{
	      JSONObject result = new JSONObject();

	      JSONObject req = new JSONObject(request);
	      String method = req.getString("method");
	      int id = req.getInt("id");
	      JSONArray params = null;
	      if(!req.isNull("params")){
	    	  params = req.getJSONArray("params");
	      }
	      result.put("jsonrpc","2.0");
	      JsonResponse response = new JsonResponse();
	      try{
		      Object methodCallResult = call(method,params,logger,ob);
		      response.setResponse(methodCallResult.toString());
		      response.setId(id);
	      }catch(JsonRpcException ex)
	      {
		      response.setError(true);
	    	  response.setResponse(ex.getMessage());

	      }

		  return response;
	}
	
	private static Object call(String method, JSONArray params, Logger logger, Object ob) throws JsonRpcException
	{
		
		try {

			System.out.println("methodName=" + method);
			Method myMethod  = getMethodByName(method, ob, params);
			
			if(myMethod == null) throw new JsonRpcException(RPCError.METHOD_NOT_FOUND_ERROR);
			
			if(params.length()>0){
				Object[] paramObjects = getParamObjects(myMethod, params);
				return (Object) myMethod.invoke(ob, paramObjects);
			
			}
			else{
				myMethod = ob.getClass().getDeclaredMethod(method);
				return (Object) myMethod.invoke(ob);
			}
	
		} catch (NoSuchMethodException | SecurityException e) {
				logger.error("Error calling method:" + e.getMessage());
				throw new JsonRpcException(RPCError.METHOD_NOT_FOUND_ERROR);
		} catch (IllegalArgumentException e) {
			logger.error("Arguments not valid" + e.getMessage());
			throw new JsonRpcException(RPCError.INVALID_PARAMS_ERROR);
		} catch (InvocationTargetException | IllegalAccessException e) {
			logger.error("Error invoking method:" + e.getMessage());
			throw new JsonRpcException(RPCError.INTERNAL_ERROR);
		}
		
	}

	private static Method getMethodByName(String name, Object ob, JSONArray params)
	{
		Method[] allMethods = ob.getClass().getMethods();
		for(Method method : allMethods)
		{
			if(method.getName().equals(name) && method.getParameterCount() == params.length())
			{
				return method;
			}
		}
		return null;
	}
	
	private static Object[]  getParamObjects(Method method, JSONArray params) throws JsonRpcException {
		
		PositionalParams  pm = new PositionalParams(params);
		Object[] objects = null;
		try
		{
			objects = pm.getObjectsFromJSONArray(method);

		}
		catch(IllegalArgumentException ex)
		{
			throw new JsonRpcException(RPCError.INVALID_PARAMS_ERROR);
		}

		return objects;
	}
}
