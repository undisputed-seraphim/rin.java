package rin.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Protocol extends Thread {
	public static final String DELIMITER = "||";
	
	protected PrintWriter out;
	protected BufferedReader in;
	protected Packet callback;
	protected Socket socket;
	protected String target;
	protected int type;
	
	public Protocol( Socket socket, int type, Packet callback ) { this( socket, type, callback, "" ); }
	public Protocol( Socket socket, int type, Packet callback, String target ) {
		super();
		this.callback = callback;
		this.socket = socket;
		this.target = target;
		this.type = type;
	}
	
	public String getIP() { return this.socket.getInetAddress().toString(); }
	
	public void run() {
		try {
			this.out = new PrintWriter( this.socket.getOutputStream(), true );
			this.in = new BufferedReader( new InputStreamReader( this.socket.getInputStream() ) );
			String input;
			
			if( this.type == 1 ) {
				out.println( "hi" );
			}
			
			while( ( input = in.readLine() ) != null ) {
				this.receive( input );
			}
			
			this.destroy();
			
		} catch( IOException e ) {
			System.out.println( "Connection with " + this.getIP() + " dropped." );
			this.destroy();
		}
	}
	
	public void receive( String data ) {
		callback.setInfo( data );
		callback.run();
	}
	
	public void destroy() {
		if( this.type == 0 ) {
			callback.setInfo( this.target );
			callback.run();
		}
		
		try {
			this.out.close();
			this.in.close();
			this.socket.close();
		} catch (IOException e) {
			System.out.println( "could not close output/input/socket" );
			e.printStackTrace();
		}
	}
}
