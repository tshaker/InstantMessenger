package thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadedServer {
	
	  public static void main(String args[]) throws Exception {
		    
		    // create a server side socket - same as before
		    ServerSocket server = new ServerSocket(9999);
		    while(true) {
		    	try {
			        Socket socket = server.accept();
			        new ThreadedSocket(socket).start();
		    	} catch(IOException e) {
		    		try {
						server.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    	}
		    }
	  }
} 
