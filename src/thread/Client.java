package thread;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	
	public static void main(String args[]) throws Exception {
		
		System.out.println("client!");
		
		String hostName = "localhost";
		int portNumber = 9999;
		
		Socket socket = new Socket(hostName, portNumber);
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in)); // TODO: take out
		
		String userInput;
		while ((userInput = stdIn.readLine()) != null) {
			out.println(userInput);
			System.out.println("echo: " + userInput);
		}
		
		socket.close();
	}
}
