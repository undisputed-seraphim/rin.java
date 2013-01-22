package rin.network;

import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Client extends JFrame implements ProtocolCode {
	private static final long serialVersionUID = 7L;
	public static String server = "127.0.0.1";
	public static final int port = 1337;
	
	protected Socket socket;
	
	public Client() {
		super();
		super.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		this.createGUI();
	}
	
	public static void main( String args[] ) {
		Client client = new Client();
		try {
			client.connect( Client.server, Client.port );
		} catch (IOException e) {
			System.out.println( "Could not connect to server!" );
			e.printStackTrace();
		}
	}
	
	public void createGUI() {
		this.add( new JPanel() );
		
		this.setVisible( true );
	}
	
	public void connect( String server, int port ) throws IOException {
		this.socket = new Socket( server, port );
		
		final Client client = this;
		Packet callback = new Packet() {
			public void run() {
				System.out.println( "nope" );
				client.process( this );
			}
		};
		
		new Protocol( this.socket, Device.CLIENT, callback, "Server" ).start();
	}
	
	public void process( Packet packet ) {
		System.out.println( "WTF" );
	}
}
