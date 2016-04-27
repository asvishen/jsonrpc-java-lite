package edu.asu.ser.jsonrpc.lite.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TCPClient extends AbstractClient{

	public String url;
	public int port;
	protected static Logger logger; 


	public TCPClient(String url,int port){
		this.url = url;		
		this.port = port;
		logger = LogManager.getLogger("clientLog");
	}
	
	@Override
	public String sendRequest(String request) {
		try{
			
			Socket socket = new Socket(url,port);
			logger.debug("Connecting to Server...");

			PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
			logger.debug("Writing Contents to Stream");

			out.println(request+'\n');
			logger.debug("Finished writing contents to Stream");
			logger.debug("Waiting for Response...");

			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			logger.debug("Response Received from Server");

			String response = in.readLine();
			logger.debug("Closing all streams");

			
			socket.close();
			in.close();
			out.close();
			logger.debug("Closed all streams successfully");
			
			return response;
		
		}
		catch(IOException ex)
		{
			logger.error(ex.getLocalizedMessage());
		}
		return null;
	}

}
