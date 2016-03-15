package thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ThreadedSocket extends Thread {
	Socket socket;
	
	ThreadedSocket(Socket socket) {
		this.socket = socket;
	}
	
	public static ArrayList<PrintWriter> out = new ArrayList<PrintWriter>();
	private static BufferedReader in;
	
	public void run() {
		try {
			out.add(new PrintWriter(socket.getOutputStream(), true));
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String message;
			while((message = in.readLine()) != null) {
				MultiThreadedServer.textArea.append(message + "\n");
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