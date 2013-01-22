package rin.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class Buffer {
	
	/* functions to be performed on javas typed buffer objects
	 * fromArray: turns array into a buffer of the appropriate type. endian ? big_endian : little_endian
	 * toString: returns a string representation of the buffer as an array
	 */
	
	/* byte buffers */
	public static ByteBuffer toBuffer( byte[] arr ) { return Buffer.toBuffer( arr, false ); }
	public static ByteBuffer toBuffer( byte[] arr, boolean endian ) {
		ByteBuffer bb = ByteBuffer.allocateDirect( arr.length );
		bb.order( endian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN );
		for( byte b : arr )
			bb.put( b );
		bb.flip();
		return bb;
	}
	
	/* int buffers */
	public static int[] toArray( IntBuffer buf ) {
		int[] arr = new int[ buf.capacity() ];
		for( int i = 0; i < buf.capacity(); i++ )
			arr[i] = buf.get( i );
		return arr;
	}
	public static IntBuffer toBuffer( int[] arr ) { return Buffer.toBuffer( arr, false ); }
	public static IntBuffer toBuffer( int[] arr, boolean endian ) {
		ByteBuffer bb = ByteBuffer.allocateDirect( arr.length * 4 );
		bb.order( endian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN );
		IntBuffer ib = bb.asIntBuffer();
		for( int i : arr )
			ib.put( i );
		ib.flip();
		return ib;
	}
	public static String toString( IntBuffer buf ) {
		int[] arr = Buffer.toArray( buf );
		String str = "IntBuffer[ ";
		for( int i : arr )
			str += i+" ";
		str += "]";
		return str;
	}
	
	/* float buffers */
	public static float[] toArray( FloatBuffer buf ) {
		float[] arr = new float[ buf.capacity() ];
		for( int i = 0; i < buf.capacity(); i++ )
			arr[i] = buf.get( i );
		return arr;
	}
	public static float[] toArray( ArrayList<Float> al ) {
		float[] arr = new float[ al.size() ];
		for( int i = 0; i < al.size(); i++ )
			arr[i] = al.get(i);
		return arr;
	}
	public static FloatBuffer toBuffer( float[] arr ) { return Buffer.toBuffer( arr, false ); }
	public static FloatBuffer toBuffer( float[] arr, boolean endian ) {
		ByteBuffer bb = ByteBuffer.allocateDirect( arr.length * 4 );
		bb.order( endian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN );
		FloatBuffer fb = bb.asFloatBuffer();
		for( float f : arr )
			fb.put( f );
		fb.flip();
		return fb;
	}
	public static String toString( ArrayList<Float> al ) {
		String str = "ArrayList<Float>[ ";
		for( float f : al )
			str += f+" ";
		str += "]";
		return str;
	}
	public static String toString( float[] arr ) {
		String str = "FloatArray[ ";
		for( float f : arr )
			str += f+" ";
		str += "]";
		return str;
	}
	public static String toString( FloatBuffer buf ) {
		float[] arr = Buffer.toArray( buf );
		String str = "FloatBuffer[ ";
		for( float f : arr )
			str += f+" ";
		str += "]";
		return str;
	}
}
