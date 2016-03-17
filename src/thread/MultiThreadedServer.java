package thread;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MultiThreadedServer extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	public static JTextArea textArea; // TODO: pass the object and take away static
	private JScrollPane scrollPane;
	
	private static String port;
	private static ServerSocket server;
	
	/**
	 * Launch the application.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {		
		port = JOptionPane.showInputDialog("Port number:");
		
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
		textArea.append("Your chat has begun!\n");
		
		scrollPane = new JScrollPane(textArea);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.append("HOST: " + e.getActionCommand() + "\n");
				for (PrintWriter current : ThreadedSocket.out) {
					current.println("HOST: " + e.getActionCommand());
				}
				textField.setText("");
			}
		});
		contentPane.add(textField, BorderLayout.SOUTH);
		textField.setColumns(10);
	}

}
