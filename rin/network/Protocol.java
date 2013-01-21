package rin.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Protocol extends Thread implements ProtocolCode {
	public static final String DELIMITER = "||";
	
	protected PrintWriter out;
	protected BufferedReader in;
	protected Packet callback;
	protected Socket socket;
	protected String target;
	protected Device type;
	
	public Protocol( Socket socket, Device type, Packet callback ) { this( socket, type, callback, "" ); }
	public Protocol( Socket socket, Device type, Packet callback, String target ) {
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
			
			this.send( Code.HANDSHAKE );
			
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
		callback.setInfo( Code.NULL, data );
		callback.run();
	}
	
	public void send( Code code ) { this.send( code, "" ); }
	public void send( Code code, String data ) {
		this.out.println( code + Protocol.DELIMITER + data );
	}
	
	public void destroy() {
		callback.setInfo( Code.DISCONNECTED, this.target );
		callback.run();
		
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