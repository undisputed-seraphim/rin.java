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
	protected Packet response;
	protected Socket socket;
	protected String target;
	protected Device type;
	
	public Protocol( Socket socket, Device type, Packet callback ) { this( socket, type, callback, "" ); }
	public Protocol( Socket socket, Device type, Packet callback, String target ) {
		super();
		this.callback = callback;
		this.callback.setSender( target );
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
					this.callback.encode( input );
					if( callback.code == Code.ACCEPT ) {
						this.send( Code.ACCEPTED );
						break;
					}
				}
			}
			
			while( ( input = in.readLine() ) != null ) {
				if( input.indexOf( Protocol.DELIMITER ) != -1 ) {
					this.callback.encode( input );
					this.receive( this.callback.code, this.callback.data );
				} else this.receive( Code.get( input ), "" );
			}
			
			this.destroy();
			
		} catch( IOException e ) {
			System.out.println( "Connection with " + this.target + " [" + this.getIP() + "] dropped." );
			this.destroy();
		}
	}
	
	public void receive( Code code, String data ) {
		switch( this.type ) {
		
		case SERVER:
			switch( code ) {
			
			case HANDSHAKE:
				this.callback.setInfo( this.target, Code.ACCEPT, "" );
				this.send();
				break;
				
			case ACCEPTED:
				this.callback.setInfoAndRun( this.target, code, this.getIP() );
				return;
			}
			break;
			
		case CLIENT:
			break;
		}
		
		this.callback.setInfoAndRun( this.target, code, data );
	}
	
	public void send() { this.send( this.callback.decode() ); }
	public void send( Code code ) { this.callback.setInfo( this.target, code, "" ); this.send( this.callback.decode() ); }
	public void send( String data ) {
		this.out.println( data );
	}
	
	public void destroy() {
		this.callback.setInfoAndRun( this.target, Code.DISCONNECTED, this.getIP() );
		
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
