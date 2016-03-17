package thread;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JTextField;
import javax.swing.JTextArea;

public class Client extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private static JTextArea textArea = new JTextArea();
	private JScrollPane scrollPane;
	
	private static String clientName;
	private static Socket socket;
	private static PrintWriter out;
	private static BufferedReader in;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws Exception{
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
		
		clientName = "CLIENT";
		String hostName = "localhost";
		int portNumber = 9999;
		
		socket = new Socket(hostName, portNumber);
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		out.println(clientName + " has joined the chat!");
				
		String message;
		while((message = in.readLine()) != null) {
			textArea.append(message + "\n");
		}
		
		socket.close();
	}

	/**
	 * Create the frame.
	 */
	public Client() {
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
				out.println(clientName + ": " + e.getActionCommand());
				textField.setText("");
			}
		});
		contentPane.add(textField, BorderLayout.SOUTH);
		textField.setColumns(10);
	}

}
