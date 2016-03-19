package thread;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JOptionPane;

public class ThreadedSocket extends Thread {
	Socket socket;
	
	ThreadedSocket(Socket socket) {
		this.socket = socket;
	}
	
	public static ArrayList<PrintWriter> out = new ArrayList<PrintWriter>();
	private static ArrayList<BufferedReader> in = new ArrayList<BufferedReader>();
	private static ArrayList<BufferedInputStream> bis = new ArrayList<BufferedInputStream>();
	
	public void run() {
		try {
			out.add(new PrintWriter(socket.getOutputStream(), true));
			in.add(new BufferedReader(new InputStreamReader(socket.getInputStream())));
			bis.add(new BufferedInputStream(socket.getInputStream()));
			
			while (true) {
				synchronized (in) {
					for (int i = 0; i < in.size(); i++) {
						if ((in.get(i) != null) && in.get(i).ready()) {						
							String message = in.get(i).readLine();
							MultiThreadedServer.showMessage(message);
							
							if (!message.equals("A file is currently being shared...")) {
								for (PrintWriter current : out) {
									current.println(message);
									current.flush();
								}
							} else {
								MultiThreadedServer.textField.setEditable(false);
								
								// receiving file from the client
								synchronized (bis) {
									try {
										String input = JOptionPane.showInputDialog("Save file:");
										File f1 = new File(input);
										FileOutputStream fs = new FileOutputStream(f1);
										
										BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(f1));

										byte[] buffer = new byte[1024];
							            int read;
							            while (((read = bis.get(i).read(buffer)) != -1)) {
							            	outStream.write(buffer, 0, read);
							            	outStream.flush();
							            	if (read < 1024) {
							            		break;
							            	}
							            }
									} catch (Exception e) {
										System.out.println(e.getMessage());
									}
								}
					            
					            MultiThreadedServer.textField.setEditable(true);
					            MultiThreadedServer.showMessage("File shared!");
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