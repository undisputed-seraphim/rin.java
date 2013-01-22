package rin.network;

import java.io.IOException;
import java.net.ServerSocket;

import rin.gui.GUI.*;
import rin.network.Protocol;

public class Server implements ProtocolCode {
	public static final int port = 1337;
	
	protected static Window window;
	protected static GUIComponent list;
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
		Server.window = new Window()
			.setTitle( "rin.ai | Server" )
			.setSize( 800, 300 )
			.setLocation( 20, 20 )
			.setBackgroundColor( 233, 233, 233 );

		Server.list = new TextList()
			.setBackgroundColor( 130, 130, 130 )
			.addTo( Server.window );
	
		{ new Panel()
			.setBackgroundColor( 233, 233, 233 )
			.addTo( Server.window ); }
	
		Server.window.show();
	}
	
	public static void listen( int port ) throws IOException {
		Server.socket = new ServerSocket( port );
		
		System.out.println( "Server listening on port " + Server.port );
		Packet onResponse = new Packet() {
			public void run() {
				Server.process( this.ip, this.code, this.data );
			}
		};
		
		while( Server.listening )
			new Protocol( Server.socket.accept(), Device.SERVER, onResponse, Protocol.THREAD_PREFIX + Server.threads++ )
					.start();
		
		Server.socket.close();
	}
	
	public static void addEntry( String id, String entry ) {
		Server.list.toTextList().addListItem( id, entry );
	}
	
	public static void removeEntry( String id ) {
		Server.list.toTextList().removeListItem( id );
	}
	
	public static void process( String ip, Code code, String data ) {
		System.out.println( "received " + ip + code + data );
		
		switch( code ) {
		
		case ACCEPTED:
			Server.addEntry( data, ip + " connected [" + data + "]" );
			break;
		
		case DISCONNECTED:
			Server.removeEntry( data );
			break;
		}
	}
}
