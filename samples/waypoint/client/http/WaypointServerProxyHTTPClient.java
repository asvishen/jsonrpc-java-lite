package waypoint.client.http;

import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import waypoint.server.Waypoint;
import waypoint.server.WaypointServer;
import edu.asu.ser.jsonrpc.exception.JsonRpcException;
import edu.asu.ser.jsonrpc.lite.client.HttpClient;
import edu.asu.ser.jsonrpc.lite.jsonutils.JsonRequest;
import edu.asu.ser.jsonrpc.parameters.PositionalParams;

public class WaypointServerProxyHTTPClient extends HttpClient implements WaypointServer{ 

	public WaypointServerProxyHTTPClient(URL url) 
	{
		super(url);
	}

	@Override
	public boolean add(Waypoint param0) throws JsonRpcException
	{ 
		JsonRequest req = new JsonRequest(); 
		JSONArray arr = new JSONArray();
		JSONObject obj = param0.toJson();
		arr.put(obj);
		PositionalParams params = new PositionalParams(arr); 
		req.setParams(params); 
		req.setMethod("add"); 
		Object result = sendRequest(req.toString());
		return (boolean) result; 
	} 

	@Override
	public boolean remove(String param0) throws JsonRpcException
	{ 
		JsonRequest req = new JsonRequest(); 
		JSONArray arr = new JSONArray();
		arr.put(param0);
		PositionalParams params = new PositionalParams(arr); 
		req.setParams(params); 
		req.setMethod("remove"); 
		Object result = sendRequest(req.toString());
		return (boolean) result; 
	} 

	@Override
	public Waypoint get(String param0) throws JsonRpcException
	{ 
		JsonRequest req = new JsonRequest(); 
		JSONArray arr = new JSONArray();
		arr.put(param0);
		PositionalParams params = new PositionalParams(arr); 
		req.setParams(params); 
		req.setMethod("get"); 
		Object result = sendRequest(req.toString());
		JSONObject resultObj = new JSONObject(result);
		return new Waypoint(resultObj);
	}

	@Override
	public boolean resetWaypoints() throws JsonRpcException
	{ 
		JsonRequest req = new JsonRequest(); 
		JSONArray arr = new JSONArray(); 
		PositionalParams params = new PositionalParams(arr); 
		req.setParams(params);req.setMethod("resetWaypoints"); 
		Object result = sendRequest(req.toString());
		return (boolean) result; 
	} 
}
