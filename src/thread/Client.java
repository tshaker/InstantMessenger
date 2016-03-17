package thread;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Client extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private static JTextArea textArea = new JTextArea();
	private JScrollPane scrollPane;
	
	private static Socket socket;
	private static PrintWriter out;
	private static BufferedReader in;
	
	private static String client;
	private static String IP;
	private static String port;

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
			
			out.println(client + " has joined the chat!");
					
			String message;
			while((message = in.readLine()) != null) {
				textArea.append(message + "\n");
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
		textArea.append("Waiting to join chat...\n");
		
		scrollPane = new JScrollPane(textArea);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
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
}
