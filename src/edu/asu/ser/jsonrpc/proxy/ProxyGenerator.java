package edu.asu.ser.jsonrpc.proxy;

import java.lang.reflect.Method;

import edu.asu.ser.jsonrpc.lite.client.HttpClient;

public class ProxyGenerator {
	
	
	
	private static StringBuilder br = new StringBuilder();

	private static void generateClassStructure(String name,Class<?> client)
	{
		br.append("public class ").append(name).append("Client extends ").append(client.getSimpleName());
		br.append(" implements ").append(name).append("{ \n\n");
		if(client.equals(HttpClient.class))
		{
			generateHttpConstructor(name);
		}
		else generateTCPClientConstructor(name);

		
	}
	
	private static void generateTCPClientConstructor(String name) {
		br.append("public ").append(name).append("Client(String url,int port) \n{\n");
		br.append("super(url,port);\n}\n");		
	}

	private static void generateHttpConstructor(String name)
	{
		br.append("public ").append(name).append("Client(URL url) \n{\n");
		br.append("super(url);\n}\n");
	}
	
	
	public static void generateJavaProxy(Class<?>[] interfaces,Class<?> client)
	{
		
		for(Class<?> interf: interfaces)
		{
			
			generateClassStructure(interf.getSimpleName(),client);
			Method[] methods = interf.getMethods();
			for(Method method: methods)
			{
				generateMethodStructure(method,client);
			}
			generateClassFinishStructure();
			writeClasstoFile();
			//System.out.println(br.toString());
		}
	
		
		
	}


	private static void writeClasstoFile() {
		// TODO Auto-generated method stub
		
	}


	private static void generateClassFinishStructure() {
		// TODO Auto-generated method stub
		br.append("}");
		
	}


	private static void generateMethodStructure(Method method, Class<?> client) {
		br.append("\n").append("public ").append(method.getReturnType().getSimpleName()).append(" ");
		br.append(method.getName()).append("(");
		int i=0;
		Class<?> clas[] = method.getParameterTypes();
		if(method.getParameterCount()>0) {
			
			for(Class<?> cla:clas)
			{
				br.append(cla.getSimpleName()).append(" ");
				br.append("param"+ Integer.toString(i++));
				br.append(",");
			}
		}
		br.replace(br.toString().lastIndexOf(','),br.length(),"");
		br.append(") ");
		if(method.getExceptionTypes().length>0)
		{
			
			br.append("throws ");
			for(Class<?> cla: method.getExceptionTypes())
			{
				br.append(cla.getSimpleName()).append(",");
			}
		}
		br.replace(br.toString().lastIndexOf(','),br.length(),"");

		//Generating method body below
		br.append("\n").append("{ \n");
		br.append("JsonRequest req = new JsonRequest(); \n");
		
		if(method.getParameterCount()==0) 
		{
			br.append("JSONArray arr = new JSONArray(); \n");
			br.append("PositionalParams params = new PositionalParams(arr); \n");
			br.append("req.setParams(params);");
		}
		else{
			br.append("JSONArray arr = new JSONArray();\n");
			 i=0;
			for(Class<?> cla:clas)
			{	
							
				if(cla.isPrimitive())
				{
					br.append("arr.put(param").append(i++).append(");\n");
				}
				else{
					
					br.append("JSONObject obj = param").append(i++).append(".toJson();\n");
					br.append("arr.put(obj);\n");
				}
				
			}
			br.append("PositionalParams params = new PositionalParams(arr); \n");

			br.append("req.setParams(params); \n");
			br.append("req.setMethod(").append("\""+method.getName()+"\"); \n");
			br.append("Object result = sendRequest(req.toString());\n");

			if(method.getReturnType().isPrimitive())
			{
				br.append("return (").append(method.getReturnType().getSimpleName()).append(") result; \n} \n");
			}
			else{
				br.append("JSONObject resultObj = new JSONObject(result);\n");

				br.append("return new ").append(method.getReturnType().getSimpleName()).append("(resultObj);\n}\n");
			}
			
			
		}
		
		
		
	}
	
	
}
