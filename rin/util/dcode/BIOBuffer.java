package rin.util.dcode;

import java.nio.ByteBuffer;

public class BIOBuffer {
	private ByteBuffer target = null;
	
	public int getInt() { return this.target.getInt(); }
	public int getInt( int index ) { return this.target.getInt( index ); }
}
