package rin.network;

public interface ProtocolCode {
	public enum Device {
		SERVER,
		CLIENT,
		LOGIN
	}
	
	public enum Code {
		NULL,
		KILL,
		HANDSHAKE,
		ACCEPT,
		ACCEPTED,
		DISCONNECTED;
		
		public static Code get( String str ) {
			for( Code c : Code.values() )
				if( c.toString().toLowerCase().equals( str.toLowerCase() ) )
					return c;
			return Code.NULL;
		}
	}
}
