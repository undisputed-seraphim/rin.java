package rin.network;

public class Packet implements Runnable, ProtocolCode {
	protected Code code;
	protected String data;
	
	public void setInfo( Code code, String data ) {
		this.code = code;
		this.data = data;
	}
	
	public void run() {}
}
