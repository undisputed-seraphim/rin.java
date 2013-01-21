package rin.network;

import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.JFrame;
import javax.swing.JPanel;

import rin.network.Protocol;

public class Server {
	public static final int port = 1337;
	
	protected static JFrame window;
	protected static ServerSocket socket;
	protected static boolean listening = false;
	protected static int threads = 0;
	
	public static void main( String args[] ) {
		Server.createGUI();
		
		try {
			Server.listening = true;
			Server.listen( Server.port );
		} catch( IOException e ) {
			System.out.println( "unable to listen on port " + Server.port );
			e.printStackTrace();
		}
	}
	
	public static void createGUI() {
		Server.window = new JFrame();
		Server.window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		Server.window.add( new JPanel() );
		Server.window.setVisible( true );
	}
	
	public static void listen( int port ) throws IOException {
		Server.socket = new ServerSocket( port );
		
		Packet onResponse = new Packet() {
			public void run() {
				Server.process( this.data );
			}
		};
		
		while( Server.listening )
			new Protocol( Server.socket.accept(), 0, onResponse, "rThread-" + Server.threads++ ).start();
		
		Server.socket.close();
	}
	
	public static void process( String data ) {
		System.out.println( "received " + data );
	}
}
