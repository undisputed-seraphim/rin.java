package rin.util;

import java.nio.ByteBuffer;

public class BIO {
	protected static class Types<T> {
		protected static final Types<Byte> BYTE = new Types<Byte>() { public Byte getData( ByteBuffer bb ) { return bb.get(); } };
		protected static final Types<Character> CHAR = new Types<Character>() {
			public Character getData( ByteBuffer bb ) { return new String( new byte[] { bb.get() } ).charAt( 0 ); }
		};
		protected static final Types<Short> SHORT = new Types<Short>() { public Short getData( ByteBuffer bb ) { return bb.getShort(); } };
		protected static final Types<Integer> INT = new Types<Integer>() { public Integer getData( ByteBuffer bb ) { return bb.getInt(); } };
		protected static final Types<Float> FLOAT = new Types<Float>() { public Float getData( ByteBuffer bb ) { return bb.getFloat(); } };
		protected static final Types<Double> DOUBLE = new Types<Double>() { public Double getData( ByteBuffer bb ) { return bb.getDouble(); } };
		protected static final Types<Long> LONG = new Types<Long>() { public Long getData( ByteBuffer bb ) { return bb.getLong(); } };
		public T getData( ByteBuffer bb ) { return null; }
	}
	
	private ByteBuffer data;
	private int pointer = 0;
	
	public BIO( String data ) {
		this.data = ByteBuffer.allocate( data.length() );
		for( byte b : data.getBytes() )
			this.data.put( b );
		this.data.flip();
		System.out.println( this.data.limit() );
	}
	
	public static BIO fromString( String data ) { return new BIO( data ); }
	public static BIO fromFile( String file ) { return new BIO( IO.file.asString( file ) ); }
	public static String asString( Object[] arr ) {
		String res = "";
		
		for( Object o : arr )
			res += o + " ";
		
		return res;
	}
	
	private <T> T preview( Types<T> t ) { T res = t.getData( this.data ); this.data.position( this.pointer ); return res; }
	private <T> T[] preview( Types<T> t, T[] res ) {
		for( int i = 0; i < res.length; i++ )
			res[i] = t.getData( this.data );
		
		/* reset pointer to original position because preview only */
		this.data.position( this.pointer );
		return res;
	}
	
	public byte previewByte() { return this.<Byte>preview( Types.BYTE ); }
	public Byte[] previewBytes( int num ) { return this.<Byte>preview( Types.BYTE, new Byte[num] ); }
	
	public char previewChar() { return this.<Character>preview( Types.CHAR ); }
	public Character[] previewChars( int num ) { return this.<Character>preview( Types.CHAR, new Character[num] ); }
	
	public short previewShort() { return this.<Short>preview( Types.SHORT ); }
	public Short[] previewShorts( int num ) { return this.<Short>preview( Types.SHORT, new Short[num] ); }
	
	public int previewInt() { return this.<Integer>preview( Types.INT ); }
	public Integer[] previewInts( int num ) { return this.<Integer>preview( Types.INT, new Integer[num] ); }
	
	public float previewFloat() { return this.<Float>preview( Types.FLOAT ); }
	public Float[] previewFloats( int num ) { return this.<Float>preview( Types.FLOAT, new Float[num] ); }
	
	public double previewDouble() { return this.<Double>preview( Types.DOUBLE ); }
	public Double[] previewDoubles( int num ) { return this.<Double>preview( Types.DOUBLE, new Double[num] ); }
	
	public long previewLong() { return this.<Long>preview( Types.LONG ); }
	public Long[] previewLongs( int num ) { return this.<Long>preview( Types.LONG, new Long[num] ); }
	
	private <T> T read( Types<T> t ) { T res = t.getData( this.data ); this.pointer = this.data.position(); return res; }
	private <T> T[] read( Types<T> t, T[] res ) {
		for( int i = 0; i < res.length; i++ )
			res[i] = t.getData( this.data );
		
		/* advance the pointer to the new read position */
		this.pointer = this.data.position();
		return res;
	}
	
	public byte readByte() { return this.<Byte>read( Types.BYTE ); }
	public Byte[] readBytes( int num ) { return this.<Byte>read( Types.BYTE, new Byte[num] ); }
	
	public char readChar() { return this.<Character>read( Types.CHAR ); }
	public Character[] readChars( int num ) { return this.<Character>read( Types.CHAR, new Character[num] ); }
	
	public short readShort() { return this.<Short>read( Types.SHORT ); }
	public Short[] readShorts( int num ) { return this.<Short>read( Types.SHORT, new Short[num] ); }
	
	public int readInt() { return this.<Integer>read( Types.INT ); }
	public Integer[] readInts( int num ) { return this.<Integer>read( Types.INT, new Integer[num] ); }
	
	public float readFloat() { return this.<Float>read( Types.FLOAT ); }
	public Float[] readFloats( int num ) { return this.<Float>read( Types.FLOAT, new Float[num] ); }
	
	public double readDouble() { return this.<Double>read( Types.DOUBLE ); }
	public Double[] readDoubles( int num ) { return this.<Double>read( Types.DOUBLE, new Double[num] ); }
	
	public long readLong() { return this.<Long>read( Types.LONG ); }
	public Long[] readLongs( int num ) { return this.<Long>read( Types.LONG, new Long[num] ); }
}
