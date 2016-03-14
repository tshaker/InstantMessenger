package thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadedSocket extends Thread {
	Socket socket;
	
	ThreadedSocket(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		BufferedReader input = null;
		PrintWriter output = null;
		
		try {
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.exit(-1);
		}
		
		while(true) {
			try {
				output.println(input.readLine());
				System.out.println(input.readLine());
			} catch (IOException e) {
				System.out.println(e.getMessage());
				try {
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.exit(-1);
			}
		}
	}
}