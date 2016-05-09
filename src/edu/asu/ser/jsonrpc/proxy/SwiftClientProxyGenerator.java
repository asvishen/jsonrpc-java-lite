package edu.asu.ser.jsonrpc.proxy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import edu.asu.ser.jsonrpc.parameters.PositionalParams;

public class SwiftClientProxyGenerator {
	
	private static StringBuilder br = new StringBuilder();
	
	private static Map<String,String> dataMap = new HashMap<>();
	
	static
	{
		dataMap.put("int", "Int");
		dataMap.put("long", "Int64");
		dataMap.put("Integer", "Int");
		dataMap.put("Long", "Int64");
		dataMap.put("boolean", "Bool");
		dataMap.put("float", "Float");
		dataMap.put("Float", "Float");
		dataMap.put("double", "Double");
		dataMap.put("Double", "Double");
		dataMap.put("char", "Character");
		dataMap.put("Character", "Character");
		dataMap.put("String", "String");
	
	}
	
	public static void generateSwiftProxy(Class<?> interf,Class<?> client)
	{
		

			
			generateClassStructure(interf.getSimpleName(),client);
			Method[] methods = interf.getMethods();
			for(Method method: methods)
			{
				generateMethodStructure(method,client);
			}
			generateClassFinishStructure();
			writeClasstoFile(interf.getSimpleName());
			System.out.println(br.toString());
		
	
		
		
	}

	private static void generateClassStructure(String simpleName,Class<?> client) {
		
		br.append("import UIKit \nimport JSONRPCLite\n\n");
		br.append("class ").append(simpleName).append("Client").append(": NSObject\n");
		br.append("{\n");
		br.append("var url: String\n");
		br.append("var id: Int\n");
		br.append("var portNo: UInt32\n");
		br.append("var client : ").append(client.getSimpleName());
		br.append("\ninit(urlName: String, id: Int, port : UInt32)\n{\nself.url=urlName\n");
		br.append("self.id = id\n");
		br.append("self.portNo = port\n");
		br.append("self.client = ").append(client.getSimpleName()).append("(url: self.url, port: self.portNo)\n}\n");
		
	}

	private static void writeClasstoFile(String name) {
		File nfile = new File("./"+name +"Client.swift");
		try {
			if(!nfile.exists())
			{
				nfile.createNewFile();
				System.out.println("file created");
				
			}
			FileWriter fw = new FileWriter(nfile);
			BufferedWriter wr = new BufferedWriter(fw);
			wr.write(br.toString());
			wr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static void generateClassFinishStructure() {
		br.append("\n}");
		
	}

	private static void generateMethodStructure(Method method, Class<?> client) {
		br.append("\n\nfunc ").append(method.getName()).append("(");
		
		int i=1;
		Class<?> clas[] = method.getParameterTypes();
		if(method.getParameterCount()>0) {
			
			for(Class<?> cla:clas)
			{
				
				br.append("param"+ Integer.toString(i++)).append(" : ");
				if(PositionalParams.PRIMITIVE_TYPES.contains(cla) || cla.isPrimitive())
				{
					br.append(dataMap.get(cla.getSimpleName()));
				}
				else{
					br.append(cla.getSimpleName());
				}
				br.append(", ");
			}
		}
		br.append("handler:(finalRes:");
		if(checkIfReturnTypePrimitive(method))
		{
			br.append(dataMap.get(method.getReturnType().getSimpleName()));
		}
		else{
			br.append(method.getReturnType().getSimpleName());
		}
		br.append(", error:Bool) -> Void) {\n");
		br.append("let requestDict : NSMutableDictionary = NSMutableDictionary()\n");
		br.append("requestDict.setObject(\"").append(method.getName()).append("\", forKey: \"method\")\n");
		br.append("requestDict.setObject(id++,forKey:\"id\")\n");
		br.append("requestDict.setObject(\"2.0\", forKey: \"jsonrpc\")\n");
		br.append("let paramArray : NSMutableArray = NSMutableArray()\n");
		i=1;
		if(method.getParameterCount()>0)
		{
			for(Class<?> cla:clas)
			{	
				if(cla.isPrimitive() || PositionalParams.PRIMITIVE_TYPES.contains(cla))
				{
					br.append("paramArray.addObject(param").append(i++).append(" as ");

					br.append(dataMap.get(cla.getSimpleName())).append(")\n");
				}
				else{
					br.append("paramArray.addObject((param").append(i++).append(" as ");

					br.append(cla.getSimpleName()).append(").toJson())\n");
				}
				
			}		
		}
		br.append("requestDict.setObject(paramArray as NSArray, forKey: \"params\")\n");
		br.append("client.sendRequest(requestDict) { (result) -> Void in\n");
        br.append("if(result  != nil){\n");
        br.append("if(((result?.allKeys)! as NSArray).containsObject(\"response\"))\n{\n"+
                    "let jsonResult : NSDictionary = result?.valueForKey(\"response\") as! NSDictionary\n" +
                    "if((jsonResult.allKeys as NSArray).containsObject(\"result\"))\n{\n" +
                        "let any : AnyObject = jsonResult.valueForKey(\"result\")!\n");
		br.append("if let check = any as? ");
		if(checkIfReturnTypePrimitive(method))
		{

			br.append(dataMap.get(method.getReturnType().getSimpleName()));
			br.append(" {\nhandler(finalRes:check,error:false)\n}");
		
		}
		else{
			br.append("[String:AnyObject]{\n").append("var recvd : ").append(method.getReturnType().getSimpleName()).append(" = ");
			br.append(method.getReturnType().getSimpleName()).append(".init(dict: check)\n");
			br.append("handler(finalRes:recvd,error:false)\n}");
		}
		br.append("\n}else if((jsonResult.allKeys as NSArray).containsObject(\"error\")){\n");
		if(checkIfReturnTypePrimitive(method))
		{
			br.append("var x: ").append(dataMap.get(method.getReturnType().getSimpleName())).append("!\n");
		}
		else{
			br.append("var x: ").append(method.getReturnType().getSimpleName()).append(" = ");
			br.append(method.getReturnType().getSimpleName()).append(".init()\n");
			
		}
		br.append("handler(finalRes:x,error:true)\n}\nelse{");
		if(checkIfReturnTypePrimitive(method))
		{
			br.append("var x: ").append(dataMap.get(method.getReturnType().getSimpleName())).append("!\n");
		}
		else{
			br.append("var x: ").append(method.getReturnType().getSimpleName()).append(" = ");
			br.append(method.getReturnType().getSimpleName()).append(".init()\n");

		}
		br.append("handler(finalRes:x,error:true)\n");

		br.append("}\n}\n");
		br.append("else{\n");
		if(checkIfReturnTypePrimitive(method))
		{
			br.append("var x: ").append(dataMap.get(method.getReturnType().getSimpleName())).append("!\n");
		}
		else{
			br.append("var x: ").append(method.getReturnType().getSimpleName()).append(" = ");
			br.append(method.getReturnType().getSimpleName()).append(".init()\n");
			
		}
		br.append("handler(finalRes:x,error:true)\n}\n}\n}\n}");

	}
	
	private static boolean checkIfReturnTypePrimitive(Method method)
	{
		return PositionalParams.PRIMITIVE_TYPES.contains(method.getReturnType()) || method.getReturnType().isPrimitive();
	}

}
