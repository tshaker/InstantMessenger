package thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class ThreadedSocket extends Thread {
	Socket socket;
	
	ThreadedSocket(Socket socket) {
		this.socket = socket;
	}
	
	public static ArrayList<PrintWriter> out = new ArrayList<PrintWriter>();
	private static ArrayList<BufferedReader> in = new ArrayList<BufferedReader>();
	
	public void run() {
		try {
			out.add(new PrintWriter(socket.getOutputStream(), true));
			in.add(new BufferedReader(new InputStreamReader(socket.getInputStream())));
			
			while (true) {
				synchronized (in) {
					for (int i = 0; i < in.size(); i++) {
						if ((in.get(i) != null) && in.get(i).ready()) {						
							String message = in.get(i).readLine();
							MultiThreadedServer.showMessage(message);
							
							for (PrintWriter current : out) {
								current.println(message);
								current.flush();
							}
						}
					}
				}
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