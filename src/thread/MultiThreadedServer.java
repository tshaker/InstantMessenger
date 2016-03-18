package thread;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;

public class MultiThreadedServer extends JFrame {

	private JPanel contentPane;
	public static JTextField textField;
	public static JTextArea textArea;
	private JScrollPane scrollPane;
	
	private static String port;
	private JButton btnSendFile;
	
	private static ArrayList<Socket> sockets = new ArrayList<Socket>();
	
	/**
	 * Launch the application.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		port = JOptionPane.showInputDialog("Port Number:");
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MultiThreadedServer frame = new MultiThreadedServer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		try {
			ServerSocket server = new ServerSocket(Integer.parseInt(port));
			
			while(true) {
		    	try {
		    		Socket socket = server.accept();
		    		new ThreadedSocket(socket).start();
		    		sockets.add(socket);
		    	} catch(IOException e) {
		    		System.out.println(e.getMessage());
		    		server.close();
		    	}
		    }
		} catch(Exception e) {
			System.out.println("ERROR: Could not connect to port \"" + port + "\".");
			System.exit(-1);
		}
	}

	/**
	 * Create the frame.
	 */
	public MultiThreadedServer() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				for (PrintWriter current : ThreadedSocket.out) {
					current.println("This chat has ended.");
				}
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		showMessage("Your chat has begun!");
		
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
		        	for (PrintWriter current : ThreadedSocket.out) {
						current.println("A file is currently being shared...");
					}
		        	textField.setEditable(false);
		        	showMessage("A file is currently being shared...");
			    }
			    
			    try {
					BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file.getPath()));
					ArrayList<BufferedOutputStream> outStreams = new ArrayList<BufferedOutputStream>();
				    for (Socket s : sockets) {
				    	outStreams.add(new BufferedOutputStream(s.getOutputStream()));
				    }
				    				    
				    byte[] buffer = new byte[1024];
		            int read;
		            while ((read = bis.read(buffer))!=-1) {
		            	for (BufferedOutputStream outSt : outStreams) {
		            		outSt.write(buffer, 0, read);
		            		outSt.flush();
		            	}
		            }
		            		            
		            if (file.length() % 1024 == 0) {
				    	for (BufferedOutputStream outSt : outStreams) {
		            		outSt.write(new byte[1], 0, 1);
		            		outSt.flush();
		            	}
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
				showMessage("HOST: " + e.getActionCommand());
				for (PrintWriter current : ThreadedSocket.out) {
					current.println("HOST: " + e.getActionCommand());
				}
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
