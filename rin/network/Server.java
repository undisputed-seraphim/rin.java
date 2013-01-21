package rin.network;

import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.JFrame;
import javax.swing.JPanel;

import rin.network.Protocol;

public class Server extends JFrame {
	private static final long serialVersionUID = 7L;
	public static final int port = 1337;
	
	protected ServerSocket socket;
	protected boolean listening = false;
	
	public Server() {
		super();
		super.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		this.createGUI();
		this.listening = true;
	}
	
	public static void main( String args[] ) {
		Server server = new Server();
		try {
			server.listen( Server.port );
		} catch( IOException e ) {
			System.out.println( "unable to listen on port " + Server.port );
			e.printStackTrace();
		}
	}
	
	public void createGUI() {
		this.add( new JPanel() );
		
		this.setVisible( true );
	}
	
	public void listen( int port ) throws IOException {
		this.socket = new ServerSocket( port );
		
		while( this.listening )
			new Protocol( this.socket.accept(), 0 ).start();
		
		this.socket.close();
	}
}
