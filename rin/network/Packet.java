package rin.network;

public class Packet implements Runnable, ProtocolCode {
	protected String ip;
	protected Code code;
	protected String data;
	
	public void setInfo( String ip, Code code, String data ) {
		this.code = code;
		this.data = data;
		this.ip = ip;
	}
	
	public void run() {}
}
