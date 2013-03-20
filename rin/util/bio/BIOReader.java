package rin.util.bio;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;

import rin.util.bio.BIOTypes.Type;
import static rin.util.bio.BIOTypes.*;

public abstract class BIOReader {
	public abstract BIOBuffer getBuffer();
	private ByteBuffer actual() { return this.getBuffer().actual(); }
	
	public int position() { return this.getBuffer().actual().position(); }
	public void position( long position ) { this.getBuffer().actual().position( (int)position ); }
	
	public void reset() { this.position( 0 ); }
	
	public void advance() { this.advance( 1 ); }
	public void advance( long bytes ) { this.position( this.position() + bytes ); }
	
	public void rewind() { this.rewind( 1 ); }
	public void rewind( long bytes ) { this.position( this.position() - bytes ); }
	
	public <T> T preview( Type<T> type ) { return this.read( type, true ); }
	public <T> T[] preview( Type<T> type, long amount ) { return this.read( type, amount, true ); }
	
	public byte previewInt8() { return this.readBytes( INT8, 1, true )[0]; }
	public byte[] previewInt8s( long amount ) { return this.readBytes( INT8, amount, true ); }
	public short previewUInt8() { return this.read( UINT8, 1, true )[0]; }
	public short[] previewUInt8s( long amount ) { return this.toPrimitive( new short[(int)amount], this.read( UINT8, amount, true ) ); }
	
	public short previewInt16() { return this.readShorts( INT16, 1, true )[0]; }
	public short[] previewInt16s( long amount ) { return this.readShorts( INT16, amount, true ); }
	public int previewUInt16() { return this.readInts(UINT16, 1, true )[0]; }
	public int[] previewUInt16s( long amount ) { return this.readInts( UINT16, amount, true ); }
	
	public int previewInt32() { return this.readInts( INT32, 1, true )[0]; }
	public int[] previewInt32s( long amount ) { return this.readInts( INT32, amount, true ); }
	public long previewUInt32() { return this.readLongs( UINT32, 1, true )[0]; }
	public long[] previewUInt32s( long amount ) { return this.readLongs( UINT32, amount, true ); }
	
	public long previewInt64() { return this.readLongs( INT64, 1, true )[0]; }
	public long[] previewInt64s( long amount ) { return this.readLongs( INT64, amount, true ); }
	
	public float previewFloat() { return this.readFloats( FLOAT, 1, true )[0]; }
	public float[] previewFloats( long amount ) { return this.readFloats( FLOAT, amount, true ); }
	
	public double previewDouble() { return this.readDoubles( DOUBLE, 1, true )[0]; }
	public double[] previewDoubles( long amount ) { return this.readDoubles( DOUBLE, amount, true ); }
	
	public <T> T read( Type<T> type ) { return this.read( type, false ); }
	private <T> T read( Type<T> type, boolean preview ) {
		int start = this.getBuffer().position();
		T res = type.getData( this.getBuffer().actual() );
		
		if( preview )
			this.getBuffer().position( start );
		return res;
	}
	
	public <T> T[] read( Type<T> type, long amount ) { return this.read( type, amount, false ); }
	private <T> T[] read( Type<T> type, long amount, boolean preview ) {
		int start = this.getBuffer().position();
		
		T[] res = type.allocate( (int)amount );
		for( int i = 0; i < amount; i++ )
			res[i] = type.getData( this.getBuffer().actual() );
		
		if( preview )
			this.getBuffer().position( start );
		return res;
	}
	
	private byte[] readBytes( PrimitiveByte type, long amount ) { return this.readBytes( type, amount, false ); }
	private byte[] readBytes( PrimitiveByte type, long amount, boolean preview ) {
		int start = this.getBuffer().position();
		
		byte[] res = new byte[(int)amount];
		for( int i = 0; i < amount; i++ )
			res[i] = type.getData( this.actual() );
		
		if( preview )
			this.getBuffer().position( start );
		return res;
	}
	
	private char[] readChars( PrimitiveChar type, long amount ) { return this.readChars( type, amount, false ); }
	private char[] readChars( PrimitiveChar type, long amount, boolean preview ) {
		int start = this.getBuffer().position();
		
		char[] res = new char[(int)amount];
		for( int i = 0; i < amount; i++ )
			res[i] = type.getData( this.actual() );
		
		if( preview )
			this.getBuffer().position( start );
		return res;
	}
	
	private short[] readShorts( PrimitiveShort type, long amount ) { return this.readShorts( type, amount, false ); }
	private short[] readShorts( PrimitiveShort type, long amount, boolean preview ) {
		int start = this.getBuffer().position();
		
		short[] res = new short[(int)amount];
		for( int i = 0; i < amount; i++ )
			res[i] = type.getData( this.actual() );
		
		if( preview )
			this.getBuffer().position( start );
		return res;
	}
	
	private int[] readInts( PrimitiveInt type, long amount ) { return this.readInts( type, amount, false ); }
	private int[] readInts( PrimitiveInt type, long amount, boolean preview ) {
		int start = this.getBuffer().position();
		
		int[] res = new int[(int)amount];
		for( int i = 0; i < amount; i++ )
			res[i] = type.getData( this.actual() );
		
		if( preview )
			this.getBuffer().position( start );
		return res;
	}
	
	private long[] readLongs( PrimitiveLong type, long amount ) { return this.readLongs( type, amount, false ); }
	private long[] readLongs( PrimitiveLong type, long amount, boolean preview ) {
		int start = this.getBuffer().position();
		
		long[] res = new long[(int)amount];
		for( int i = 0; i < amount; i++ )
			res[i] = type.getData( this.actual() );
		
		if( preview )
			this.getBuffer().position( start );
		return res;
	}
	
	private float[] readFloats( PrimitiveFloat type, long amount ) { return this.readFloats( type, amount, false ); }
	private float[] readFloats( PrimitiveFloat type, long amount, boolean preview ) {
		int start = this.getBuffer().position();
		
		float[] res = new float[(int)amount];
		for( int i = 0; i < amount; i++ )
			res[i] = type.getData( this.actual() );
		
		if( preview )
			this.getBuffer().position( start );
		return res;
	}
	
	private double[] readDoubles( PrimitiveDouble type, long amount ) { return this.readDoubles( type, amount, false ); }
	private double[] readDoubles( PrimitiveDouble type, long amount, boolean preview ) {
		int start = this.getBuffer().position();
		
		double[] res = new double[(int)amount];
		for( int i = 0; i < amount; i++ )
			res[i] = type.getData( this.actual() );
		
		if( preview )
			this.getBuffer().position( start );
		return res;
	}
	
	public String readString( long length ) {
		String res = "";
		for( int i = 0; i < length; i++ )
			res += CHAR8.getData( this.actual() );
		return res;
	}
	
	public <T, R extends Number> T toPrimitive( T arr, R[] from ) {
		for( int i = 0; i < from.length; i++ ) {
			if( from[i] instanceof Short )
				Array.set( arr, i, from[i].shortValue() );
		}
		return arr;
	}
	
	public byte readInt8() { return INT8.getData( this.actual() ); }
	public byte[] readInt8s( long amount ) { return this.readBytes( INT8, amount ); }
	public short readUInt8() { return UINT8.getData( this.actual() ); }
	public short[] readUInt8s( long amount ) { return this.toPrimitive( new short[(int)amount], this.read( UINT8, amount ) ); }
	
	public char readChar() { return CHAR8.getData( this.actual() ); }
	public char[] readChars( long amount ) { return this.readChars( CHAR8, amount ); }
	
	public short readInt16() { return INT16.getData( this.actual() ); }
	public short[] readInt16s( long amount ) { return this.readShorts( INT16, amount ); }
	public int readUInt16() { return UINT16.getData( this.actual() ); }
	public int[] readUInt16s( long amount ) { return this.readInts( UINT16, amount ); }
	
	public int readInt32() { return INT32.getData( this.actual() ); }
	public int[] readInt32s( long amount ) { return this.readInts( INT32, amount ); }
	public long readUInt32() { return UINT32.getData( this.actual() ); }
	public long[] readUInt32s( long amount ) { return this.readLongs( UINT32, amount ); }
	
	public long readInt64() { return INT64.getData( this.actual() ); }
	public long[] readInt64s( long amount ) { return this.readLongs( INT64, amount ); }
	
	public float readFloat() { return FLOAT.getData( this.actual() ); }
	public float[] readFloats( long amount ) { return this.readFloats( FLOAT, amount ); }
	
	public double readDouble() { return DOUBLE.getData( this.actual() ); }
	public double[] readDoubles( long amount ) { return this.readDoubles( DOUBLE, amount ); }
}
