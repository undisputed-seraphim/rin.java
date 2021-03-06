package rin.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

public class Buffer {
	
	/* functions to be performed on javas typed buffer objects
	 * fromArray: turns array into a buffer of the appropriate type. endian ? big_endian : little_endian
	 * toString: returns a string representation of the buffer as an array
	 */
	
	/* byte buffers */
	public static byte[] toArray( ByteBuffer buf ) {
		byte[] arr = new byte[ buf.capacity() ];
		for( int i = 0; i < buf.capacity(); i++ )
			arr[i] = buf.get( i );
		return arr;
	}
	public static ByteBuffer toBuffer( byte[] arr ) { return Buffer.toBuffer( arr, false ); }
	public static ByteBuffer toBuffer( byte[] arr, boolean endian ) {
		ByteBuffer bb = ByteBuffer.allocateDirect( arr.length );
		bb.order( endian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN );
		for( byte b : arr )
			bb.put( b );
		bb.flip();
		return bb;
	}
	public static String toString( ByteBuffer buf ) {
		byte[] arr = Buffer.toArray( buf );
		String str = "ByteBuffer[ ";
		for( byte b : arr )
			str += b +" ";
		str += "]";
		return str;
	}
	
	public static String toString( Byte[] arr ) {
		String str = "Byte[ ";
		for( Byte b : arr )
			str += b + " ";
		str += "]";
		return str;
	}
	
	/* int buffers */
	public static int[] toArray( IntBuffer buf ) {
		int[] arr = new int[ buf.capacity() ];
		for( int i = 0; i < buf.capacity(); i++ )
			arr[i] = buf.get( i );
		return arr;
	}
	public static int[] toArrayi( ArrayList<Integer> al ) {
		int[] arr = new int[ al.size() ];
		for( int i = 0; i < al.size(); i++ )
			arr[i] = al.get( i );
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
	public static String toString( int[] arr ) {
		String str = "IntArray[ ";
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
	public static float[] toArrayf( ArrayList<Float> al ) {
		float[] arr = new float[ al.size() ];
		for( int i = 0; i < al.size(); i++ )
			arr[i] = al.get(i);
		return arr;
	}
	public static ArrayList<Float> toArrayList( float[] arr ) {
		ArrayList<Float> al = new ArrayList<Float>();
		for( float f : arr )
			al.add( f );
		return al;
	}
	public static ArrayList<Float> duplicateAL( float[] what, int times ) {
		ArrayList<Float> tmp = new ArrayList<Float>();
		for( int i = 0; i < times; i++ )
			for( int k = 0; k < what.length; k++ )
				tmp.add( what[k] );
		return tmp;
	}
	
	public static float[] getIndexedValues( float[] arr, int[] ind, int offset, int stride, int count ) {
		return Buffer.toArrayf( getIndexedValuesAL( arr, ind, offset, stride, count ) );
	}
	public static ArrayList<Float> getIndexedValuesAL( float[] arr, int[] ind, int offset, int stride, int count ) {
		ArrayList<Float> res = new ArrayList<Float>();
		for( int i = offset; i < ind.length; i += stride )
			for( int k = 0; k < count; k++ )
				res.add( arr[ ind[ i ] * count + k ] );
		return res;
	}
	
	public static ByteBuffer toByteBuffer( short[] arr ) {
		ByteBuffer bb = ByteBuffer.allocateDirect( arr.length );
		for( int i = 0; i < arr.length; i++ )
			bb.put( (byte)arr[i] );
		bb.flip();
		return bb;
	}
	public static ShortBuffer toBuffer( short[] arr ) { return Buffer.toBuffer( arr, false ); }
	public static ShortBuffer toBuffer( short[] arr, boolean endian ) {
		ByteBuffer bb = ByteBuffer.allocateDirect( arr.length * 2 );
		bb.order( endian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN );
		ShortBuffer sb = bb.asShortBuffer();
		for( short s : arr )
			sb.put( s );
		sb.flip();
		return sb;
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
