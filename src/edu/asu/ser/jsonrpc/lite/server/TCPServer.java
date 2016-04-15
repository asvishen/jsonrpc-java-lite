package edu.asu.ser.jsonrpc.lite.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class TCPServer extends Thread{

	public Socket socket;
	public int id;
	private Object ob;
	protected static Logger logger;

	
	public TCPServer(Socket socket, int id, Object ob){
		this.socket = socket;
		this.id = id ;
		this.ob = ob;
		logger = LogManager.getLogger("serverLog");
		logger.debug("Listening at port:"+socket);
		
	}
	
	
	public void run()
	{
		try{
			logger.debug("Starting new thread");
			PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line;
			while((line =in.readLine()) != null)
			{
				logger.info("client request: "+line);
				if(line.indexOf("{")>=0){
					String request = line.substring(line.indexOf("{"));
					logger.debug("Calling method");
					String response = ServerUtils.callMethod(request,logger,ob);
					logger.debug("Accepted method result");
					System.out.println(response);
					logger.debug("Sending response back to client");
					out.println(response);
					logger.debug("Response sent back to client id: "+id);


				}else{
					logger.error("No json object in clientString: "+
							line);
				}
			}
			out.close();
			in.close();
			socket.close();
		}
		catch(IOException e)
		{
			logger.error("Error in TCP connection:"+e.getLocalizedMessage());
		}
		 
	}

}
