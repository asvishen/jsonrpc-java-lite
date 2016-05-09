package waypoint.server.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import waypoint.server.Waypoint;
import waypoint.server.WaypointServer;
import edu.asu.ser.jsonrpc.exception.JsonRpcException;
import edu.asu.ser.jsonrpc.lite.server.HttpServer;

public class WaypointHTTPServer implements WaypointServer {

	private static List<Waypoint> list = new ArrayList<>();

	@Override
	public boolean resetWaypoints() throws JsonRpcException {
		if(list.isEmpty())
		return false;
		else {
			list.clear();
			return true;
		}
	}

	@Override
	public boolean add(Waypoint wp) throws JsonRpcException {
		list.add(wp);
		return true;
	}

	@Override
	public boolean remove(String wpName) throws JsonRpcException {
		for(Waypoint wp: list)
		{
			if(wp.name.equals(wpName))
			{
				list.remove(wp);
				return true;

			}
		}
		return false;
	}

	@Override
	public Waypoint get(String wpName) throws JsonRpcException {
		
		for(Waypoint wp: list)
		{
			if(wp.name.equals(wpName))
			{
				return wp;

			}
		}
		return null;
	}
	
	public static void main(String[] args) throws IOException {
			HttpServer serv = new HttpServer(new WaypointHTTPServer(),8080);
			serv.start();
			
		}
	}

