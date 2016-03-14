package thread;

import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadedServer {
	
	  public static void main(String args[]) throws Exception {
		    
		    // create a server side socket - same as before
		    ServerSocket server = new ServerSocket(9999);
		    while(true) {
		        Socket socket = server.accept();
		        new ThreadedSocket(socket).start();
		    }
	  } 
} 
