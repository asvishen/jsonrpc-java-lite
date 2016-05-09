package waypoint.client.tcp;

import java.net.MalformedURLException;
import java.net.URL;

import waypoint.server.Waypoint;
import edu.asu.ser.jsonrpc.exception.JsonRpcException;

public class WaypointTCPClient {

	public static void main(String[] args) throws MalformedURLException, JsonRpcException, InterruptedException {
		WaypointServerProxyTCPClient client = new WaypointServerProxyTCPClient("localhost",8080);
		Waypoint wp = new Waypoint(10.0, 0.0, 0.0, "home", "USA", "travel");
		System.out.println("Received response from Server: " + client.add(wp));
	}
}
