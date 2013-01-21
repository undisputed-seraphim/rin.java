package rin.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Protocol extends Thread {
	protected PrintWriter out;
	protected BufferedReader in;
	protected int type;
	protected Socket socket;
	
	public Protocol( Socket socket, int type ) {
		super();
		this.socket = socket;
		this.type = type;
	}
	
	public void run() {
		try {
			this.out = new PrintWriter( this.socket.getOutputStream(), true );
			this.in = new BufferedReader( new InputStreamReader( this.socket.getInputStream() ) );
			String input;
			
			if( this.type == 1 ) {
				out.println( "hi" );
			}
			
			while( ( input = in.readLine() ) != null ) {
				System.out.println( input );
			}
			
		} catch( IOException e ) {
			
		}
	}
}
