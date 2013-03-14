package rin.util.bio;

import java.nio.ByteBuffer;

import static rin.util.bio.BIOTypes.*;
import rin.util.IO;

public class BIOBuffer {
	private ByteBuffer data;
	private int pointer = 0;
	public int position() { return this.pointer; }
	public void position( int position ) { this.pointer = position; this.data.position( position ); }
	
	public BIOBuffer( String data ) {
		this.data = ByteBuffer.allocate( data.length() );
		for( byte b : data.getBytes() )
			this.data.put( b );
		this.data.flip();
	}
	
	public static BIOBuffer fromString( String data ) { return new BIOBuffer( data ); }
	public static BIOBuffer fromFile( String file ) { return new BIOBuffer( IO.file.asString( file ) ); }
	public static String asString( Object[] arr ) {
		String res = "";
		
		if( arr != null )
			for( Object o : arr )
				res += o + " ";
		
		return res;
	}
	
	public BIOBuffer reset() { this.pointer = 0; this.data.position( 0 ); return this; }
	
	private <T> T preview( Type<T> t ) { T res = t.getData( this.data ); this.data.position( this.pointer ); return res; }
	private <T> T[] preview( Type<T> t, T[] res ) {
		for( int i = 0; i < res.length; i++ )
			res[i] = t.getData( this.data );
		
		/* reset pointer to original position because preview only */
		this.data.position( this.pointer );
		return res;
	}
	
	public byte previewByte() { return this.<Byte>preview( BYTE ); }
	public Byte[] previewBytes( int num ) { return this.<Byte>preview( BYTE, new Byte[num] ); }
	
	public char previewChar() { return this.<Character>preview( CHAR ); }
	public Character[] previewChars( int num ) { return this.<Character>preview( CHAR, new Character[num] ); }
	
	public short previewShort() { return this.<Short>preview( SHORT ); }
	public Short[] previewShorts( int num ) { return this.<Short>preview( SHORT, new Short[num] ); }
	
	public int previewInt() { return this.<Integer>preview( INT ); }
	public Integer[] previewInts( int num ) { return this.<Integer>preview( INT, new Integer[num] ); }
	
	public float previewFloat() { return this.<Float>preview( FLOAT ); }
	public Float[] previewFloats( int num ) { return this.<Float>preview( FLOAT, new Float[num] ); }
	
	public double previewDouble() { return this.<Double>preview( DOUBLE ); }
	public Double[] previewDoubles( int num ) { return this.<Double>preview( DOUBLE, new Double[num] ); }
	
	public long previewLong() { return this.<Long>preview( LONG ); }
	public Long[] previewLongs( int num ) { return this.<Long>preview( LONG, new Long[num] ); }
	
	private <T> T read( Type<T> t ) { T res = t.getData( this.data ); this.pointer = this.data.position(); return res; }
	private <T> T[] read( Type<T> t, T[] res ) {
		for( int i = 0; i < res.length; i++ )
			res[i] = t.getData( this.data );
		
		/* advance the pointer to the new read position */
		this.pointer = this.data.position();
		return res;
	}
	
	public <T> T[] read( Type<T> t, int amount ) {
		T[] res = t.allocate( amount );
		for( int i = 0; i < amount; i++ )
			res[i] = t.getData( this.data );
		
		this.pointer = this.data.position();
		return res;
	}
	
	public byte readByte() { return this.<Byte>read( BYTE ); }
	public Byte[] readBytes( int num ) { return this.<Byte>read( BYTE, new Byte[num] ); }
	
	public char readChar() { return this.<Character>read( CHAR ); }
	public Character[] readChars( int num ) { return this.<Character>read( CHAR, new Character[num] ); }
	
	public short readShort() { return this.<Short>read( SHORT ); }
	public Short[] readShorts( int num ) { return this.<Short>read( SHORT, new Short[num] ); }
	
	public int readInt() { return this.<Integer>read( INT ); }
	public Integer[] readInts( int num ) { return this.<Integer>read( INT, new Integer[num] ); }
	
	public float readFloat() { return this.<Float>read( FLOAT ); }
	public Float[] readFloats( int num ) { return this.<Float>read( FLOAT, new Float[num] ); }
	
	public double readDouble() { return this.<Double>read( DOUBLE ); }
	public Double[] readDoubles( int num ) { return this.<Double>read( DOUBLE, new Double[num] ); }
	
	public long readLong() { return this.<Long>read( LONG ); }
	public Long[] readLongs( int num ) { return this.<Long>read( LONG, new Long[num] ); }
}
