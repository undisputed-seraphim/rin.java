package rin.util.bio;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;

import rin.util.IO;

public class BIOBuffer extends BIOReader {
	private ByteBuffer data;
	public ByteBuffer actual() { return this.data; }
	
	@Override public BIOBuffer getBuffer() { return this; }
	
	public BIOBuffer( byte[] data ) { this.data = ByteBuffer.wrap( data ); }
	
	public static BIOBuffer fromFile( String file ) { return new BIOBuffer( IO.file.asByteArray( file ) ); }
	public static BIOBuffer fromByteArray( byte[] arr ) { return new BIOBuffer( arr ); }
	
	public <T> T[] newArray( T[] arr, Class<T[]> type, int size ) {
		return type.cast( Array.newInstance( type.getComponentType(), size ) );
	}
	
	public static String asString( Object arr ) { return BIOBuffer.asString( arr, false ); }
	public static String asString( Object arr, boolean trim ) {
		String res = "";
		String split = trim ? "" : " ";
		
		if( arr != null )
			if( arr.getClass().isArray() )
				for( int i = 0; i < Array.getLength( arr ); i++ )
					res += Array.get( arr, i ) + split;
		
		return res;
	}
	
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
