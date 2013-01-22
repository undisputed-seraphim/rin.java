package rin.network;

public class Packet implements Runnable, ProtocolCode {
	protected String sender = "NULL";
	protected Code code = Code.NULL;
	protected String data = "NULL";
	
	public void setSender( String sender ) {
		this.sender = sender;
	}
	
	public void setInfo( String sender, Code code, String data ) {
		this.sender = sender;
		this.code = code;
		this.data = data;
	}
	
	public void setInfoAndRun( String sender, Code code, String data ) {
		this.setInfo( sender, code, data );
		this.run();
	}
	
	public void encode( String input ) {
		String code = input.substring( input.indexOf( Protocol.DELIMITER ) + Protocol.DELIMITER.length(),
				input.lastIndexOf( Protocol.DELIMITER ) );
		String data = input.substring( input.lastIndexOf( Protocol.DELIMITER ) + Protocol.DELIMITER.length() );
		this.code = Code.get( code );
		this.data = data;
	}
	
	public String decode() {
		return this.sender + Protocol.DELIMITER + this.code + Protocol.DELIMITER + this.data;
	}
	
	public void run() {}
}
