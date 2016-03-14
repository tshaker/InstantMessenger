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
		PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		
		String userInput;
		while ((userInput = stdIn.readLine()) != null) {
			output.println(userInput);
			System.out.println("echo: " + userInput);
		}
		
		socket.close();
	}
}
