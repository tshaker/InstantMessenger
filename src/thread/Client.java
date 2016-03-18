package thread;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JTextArea;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import javax.swing.JButton;

public class Client extends JFrame {

	private JPanel contentPane;
	private static JTextField textField;
	private static JTextArea textArea = new JTextArea();
	private JScrollPane scrollPane;
	
	private static Socket socket;
	private static PrintWriter out;
	private static BufferedReader in;
	
	private static String client;
	private static String IP;
	private static String port;
	private JButton btnSendFile;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws Exception{
		JTextField clientName = new JTextField(20);
		JTextField IPAddress = new JTextField(20);
		JTextField portNo = new JTextField(20);
		JPanel infoPane = new JPanel();
		infoPane.setLayout((LayoutManager) new BoxLayout(infoPane, BoxLayout.Y_AXIS));
		
		infoPane.add(new JLabel("Name (to be displayed in chat):"));
		infoPane.add(clientName);
		infoPane.add(new JLabel("IP Address (ex. 127.0.0.1):"));
		infoPane.add(IPAddress);
		infoPane.add(new JLabel("Port Number (ex. 9999):"));
		infoPane.add(portNo);
		
		int result = JOptionPane.showConfirmDialog(null, infoPane, 
	               "Connect to Server", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			client = clientName.getText();
			IP = IPAddress.getText();
			port = portNo.getText();
	    } else {
	    	System.exit(0);
	    }
				
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client frame = new Client();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		try {
			socket = new Socket(IP, Integer.parseInt(port));
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
			
			out.println(client + " has joined the chat!");
			
			String message;
			while ((message = in.readLine()) != null) {
				if (message.equals("A file is currently being shared...")) {
					showMessage(message);
					textField.setEditable(false);
					
					try {
						String input = JOptionPane.showInputDialog("Save file:");
						File f1 = new File(input);
						FileOutputStream fs = new FileOutputStream(f1);
						
						BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(f1));
					
						byte[] buffer = new byte[1024];
			            int read;
			            while (((read = bis.read(buffer)) != -1)) {
			            	outStream.write(buffer, 0, read);
			            	outStream.flush();
			            	if (read < 1024) {
			            		break;
			            	}
			            }
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
		            
		            textField.setEditable(true);
		            showMessage("File shared!");
				} else {
					showMessage(message);
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR: Could not connect to port \"" + port + "\".");
			System.exit(-1);
		}
		
		socket.close();
	}

	/**
	 * Create the frame.
	 */
	public Client() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				out.println(client + " has exited the chat!");
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		textArea.setEditable(false);
		showMessage("Waiting to join chat...");
		
		scrollPane = new JScrollPane(textArea);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		btnSendFile = new JButton("Send File");
		btnSendFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File file = null;
				
				final JFileChooser fc = new JFileChooser(); //Create a file chooser to browse files							
				int returnVal = fc.showDialog(null, "Send");
			    if (returnVal == JFileChooser.APPROVE_OPTION) {
		        	file = fc.getSelectedFile();
		        	textField.setEditable(false);
//		        	showMessage("A file is currently being shared..."); TODO: not send to itself
		        	out.println("A file is currently being shared...");
			    }
			    
			    try {
					BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file.getPath()));
					BufferedOutputStream outStream = new BufferedOutputStream(socket.getOutputStream());
				    				    
				    byte[] buffer = new byte[1024];
		            int read;
		            while ((read = bis.read(buffer))!=-1) {
		            	outStream.write(buffer, 0, read);
		            	outStream.flush();
		            }
		            		            
		            if (file.length() % 1024 == 0) {
				    	outStream.write(new byte[1], 0, 1);
		            	outStream.flush();
				    }
		            
		            bis.close();
		            showMessage("File shared!");
			    } catch (FileNotFoundException e1) {
		            showMessage("Could not send file.");
					System.out.println(e1.getMessage());
				} catch (IOException e1) {
		            showMessage("Could not send file.");
					System.out.println(e1.getMessage());
				}
			    
	            textField.setEditable(true);
			}
		});
		scrollPane.setColumnHeaderView(btnSendFile);
		
		textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println(client + ": " + e.getActionCommand());
				textField.setText("");
			}
		});
		contentPane.add(textField, BorderLayout.SOUTH);
		textField.setColumns(10);
	}
	
	static void showMessage(final String text){
		//update GUI chatWindow
		SwingUtilities.invokeLater( //set aside a thread to update the GUI
			new Runnable(){
				public void run(){ //whenever we update the GUI, this method gets called
					textArea.append(text + "\n");
				}
			}
		);
	}
}
