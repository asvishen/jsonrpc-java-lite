package calc.client.tcp;

import org.json.JSONArray;

import calc.server.Calculator;
import edu.asu.ser.jsonrpc.exception.JsonRpcException;
import edu.asu.ser.jsonrpc.lite.client.TCPClient;
import edu.asu.ser.jsonrpc.lite.jsonutils.JsonRequest;
import edu.asu.ser.jsonrpc.parameters.PositionalParams;

/**
 * Copyright 2016 Avijit Singh Vishen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Calculator Client TCP Proxy generator by Java Client Proxy generator using Calculator Interface
 * @version 1.0.0
 * @author: Avijit Vishen avijit.vishen@asu.edu
 * Software Engineering, CIDSE, Arizona State University,Polytechnic Campus
 */

public class CalculatorProxyTCPClient extends TCPClient implements Calculator{ 

	private static int id = 0;
	
	public CalculatorProxyTCPClient(String url,int port) 
	{
		super(url,port);
	}

	public int add(int param0,int param1) throws NumberFormatException,JsonRpcException
	{ 
		JsonRequest req = new JsonRequest(); 
		JSONArray arr = new JSONArray();
		arr.put(param0);
		arr.put(param1);
		PositionalParams params = new PositionalParams(arr); 
		req.setParams(params); 
		req.setMethod("add"); 
		req.setId(id++);
		Object result = sendRequest(req.toString());
		return (int) result; 
	} 

	public int div(int param0,int param1) throws NumberFormatException,JsonRpcException
	{ 
		JsonRequest req = new JsonRequest(); 
		JSONArray arr = new JSONArray();
		arr.put(param0);
		arr.put(param1);
		PositionalParams params = new PositionalParams(arr); 
		req.setParams(params); 
		req.setMethod("div"); 
		req.setId(id++);
		Object result = sendRequest(req.toString());
		return (int) result; 
	} 

	public int sub(int param0,int param1) throws NumberFormatException,JsonRpcException
	{ 
		JsonRequest req = new JsonRequest(); 
		JSONArray arr = new JSONArray();
		arr.put(param0);
		arr.put(param1);
		PositionalParams params = new PositionalParams(arr); 
		req.setParams(params); 
		req.setMethod("sub"); 
		req.setId(id++);
		Object result = sendRequest(req.toString());
		return (int) result; 
	} 

	public int mul(int param0,int param1) throws NumberFormatException,JsonRpcException
	{ 
		JsonRequest req = new JsonRequest(); 
		JSONArray arr = new JSONArray();
		arr.put(param0);
		arr.put(param1);
		PositionalParams params = new PositionalParams(arr); 
		req.setParams(params); 
		req.setMethod("mul");
		req.setId(id++);
		Object result = sendRequest(req.toString());
		return (int) result; 
	} 
}
