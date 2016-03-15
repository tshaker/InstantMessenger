package thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadedSocket extends Thread {
	Socket socket;
	
	ThreadedSocket(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			// PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in)); // TODO: take out
			
//			String inputLine;
//			while ((inputLine = stdIn.readLine()) != null) {
//				out.println(inputLine);
//				System.out.println("echo: " + inputLine);
//			}
			
			while(true) { // TODO: make this stop printing "null" after client exits
				String message = (String) in.readLine();
				System.out.println(message);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			try {
				socket.close();
			} catch (IOException e1) {
				System.out.println(e1.getMessage());
			}
			System.exit(-1);
		}
	}
}