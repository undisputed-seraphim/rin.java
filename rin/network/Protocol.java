package rin.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Protocol extends Thread implements ProtocolCode {
	public static final String THREAD_PREFIX = "rThread-";
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
			
			if( this.type == Device.CLIENT ) {
				this.send( Code.HANDSHAKE );
				
				//TODO: add a timeout waiting for server response
				while( ( input = this.in.readLine() ) != null ) {
					if( input.equals( Code.ACCEPT.toString() + Protocol.DELIMITER ) ) {
						this.send( Code.ACCEPTED );
					}
				}
			}
			
			while( ( input = in.readLine() ) != null ) {
				if( input.indexOf( Protocol.DELIMITER ) != -1 ) {
					String left = input.substring( 0, input.indexOf( Protocol.DELIMITER ) );
					String right = input.substring( input.indexOf( Protocol.DELIMITER ) + Protocol.DELIMITER.length() );
					this.receive( Code.get( left ), right );
				} else this.receive( Code.get( input ), "" );
			}
			
			this.destroy();
			
		} catch( IOException e ) {
			System.out.println( "Connection with " + this.getIP() + " dropped." );
			this.destroy();
		}
	}
	
	public void receive( Code code, String data ) {
		switch( this.type ) {
		case SERVER:
			switch( code ) {
			
			case HANDSHAKE:
				this.send( Code.ACCEPT );
				break;
				
			case ACCEPTED:
				callback.setInfo( this.getIP(), code, this.target );
				callback.run();
				return;
			}
			break;
			
		case CLIENT:
			break;
		}
		callback.setInfo( this.getIP(), code, data );
		callback.run();
	}
	
	public void send( Code code ) { this.send( code, "" ); }
	public void send( Code code, String data ) {
		this.out.println( code + Protocol.DELIMITER + data );
	}
	
	public void destroy() {
		callback.setInfo( this.getIP(), Code.DISCONNECTED, this.target );
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
