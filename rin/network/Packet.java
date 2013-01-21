package rin.network;

public class Packet implements Runnable {
	protected String data;
	
	public void setInfo( String data ) {
		this.data = data;
	}
	
	public void run() {}
}
