package rin.util.bio;

import java.nio.ByteBuffer;
import java.util.Arrays;

import rin.util.IO;

public class BIOBuffer extends BIOReader {
	private ByteBuffer data;
	public ByteBuffer actual() { return this.data; }
	
	@Override public BIOBuffer getBuffer() { return this; }
	
	public BIOBuffer( byte[] data ) { this.data = ByteBuffer.wrap( data ); }
	
	public static BIOBuffer fromFile( String file ) { return new BIOBuffer( IO.file.asByteArray( file ) ); }
	public static BIOBuffer fromByteArray( byte[] arr ) { return new BIOBuffer( arr ); }
	
	public static String asString( Object arr ) { return BIOBuffer.asString( Arrays.copyOf( arr, 1 ) ); }
	public static String asString( Object[] arr ) { return BIOBuffer.asString( arr, false ); }
	public static String asString( Object[] arr, boolean trim ) {
		String res = "";
		String split = trim ? "" : " ";
		
		if( arr != null )
			for( Object o : arr )
				res += o + split;
		
		return res;
	}
}
