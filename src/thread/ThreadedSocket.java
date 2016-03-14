package thread;

import java.net.Socket;

public class ThreadedSocket extends Thread {
	Socket socket;
		  ThreadedSocket( Socket socket ) {
		    this.socket = socket;
		  }
		 // ...
}