package waypoint.client.http;

import java.net.MalformedURLException;
import java.net.URL;

import waypoint.server.Waypoint;
import edu.asu.ser.jsonrpc.exception.JsonRpcException;

public class WaypointHTTPClient {

	public static void main(String[] args) throws MalformedURLException, JsonRpcException, InterruptedException {
		WaypointServerProxyHTTPClient client = new WaypointServerProxyHTTPClient(new URL("http://localhost:8080"));
		Waypoint wp = new Waypoint(10.0, 0.0, 0.0, "home", "USA", "travel");
		System.out.println("Received response from Server: " + client.add(wp));
	}

}
