package rin.util.bio;

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
	
	public <T> Object[] preview( Type<T> ... types ) {
		Object[] res = new Object[types.length];
		
		for( int i = 0; i < types.length; i++ )
			res[i] = this.read( types[i] );
		
		return res;
	}
	
	public <T> T preview( Type<T> type ) { return this.read( type, true ); }
	public <T> T[] preview( Type<T> type, long amount ) { return this.read( type, amount, true ); }
	public <T> T preview( ArrayType<T> type, long amount ) { return this.read( type, amount, true ); }
	
	public byte previewInt8() { return this.read( INT8, true ); }
	public byte[] previewInt8s( long amount ) { return this.read( INT8s, amount, true ); }
	public short previewUInt8() { return this.read( UINT8, true ); }
	public short[] previewUInt8s( long amount ) { return this.read( UINT8s, amount, true ); }
	
	public short previewInt16() { return this.read( INT16, true ); }
	public short[] previewInt16s( long amount ) { return this.read( INT16s, amount, true ); }
	public int previewUInt16() { return this.read( UINT16, true ); }
	public int[] previewUInt16s( long amount ) { return this.read( UINT16s, amount, true ); }
	
	public int previewInt32() { return this.read( INT32, true ); }
	public int[] previewInt32s( long amount ) { return this.read( INT32s, amount, true ); }
	public long previewUInt32() { return this.read( UINT32, true ); }
	public long[] previewUInt32s( long amount ) { return this.read( UINT32s, amount, true ); }
	
	public long previewInt64() { return this.read( INT64, true ); }
	public long[] previewInt64s( long amount ) { return this.read( INT64s, amount, true ); }
	
	public float previewFloat32() { return this.read( FLOAT32, true ); }
	public float[] previewFloat32s( long amount ) { return this.read( FLOAT32s, amount, true ); }
	public float previewFloat() { return this.previewFloat32(); }
	public float[] previewFloats( long amount ) { return this.previewFloat32s( amount ); }
	
	public double previewFloat64() { return this.read( FLOAT64, true ); }
	public double[] previewFloat64s( long amount ) { return this.read( FLOAT64s, amount, true ); }
	public double previewDouble() { return this.previewFloat64(); }
	public double[] previewDoubles( long amount ) { return this.previewFloat64s( amount ); }
	
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
	
	public <T> T read( ArrayType<T> type, long amount ) { return this.read( type, amount, false ); }
	private <T> T read( ArrayType<T> type, long amount, boolean preview ) {
		int start = this.getBuffer().position();
		T res = type.getData( this.getBuffer().actual(), (int)amount );
		
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
	
	public byte readInt8() { return this.read(INT8 ); }
	public byte[] readInt8s( long amount ) { return this.read( INT8s, amount ); }
	public short readUInt8() { return this.read( UINT8 ); }
	public short[] readUInt8s( long amount ) { return this.read( UINT8s, amount ); }
	
	public char readChar8() { return this.read( CHAR8 ); }
	public char readChar() { return this.readChar8(); }
	public char[] readChar8s( long amount ) { return this.read( CHAR8s, amount ); }
	public char[] readChars( long amount ) { return this.readChar8s( amount ); }
	
	public short readInt16() { return this.read( INT16 ); }
	public short readShort() { return this.readInt16(); }
	public short[] readInt16s( long amount ) { return this.read( INT16s, amount ); }
	public short[] readShorts( long amount ) { return this.readInt16s( amount ); }
	
	public int readUInt16() { return this.read( UINT16 ); }
	public int readUShort() { return this.readUInt16(); }
	public int[] readUInt16s( long amount ) { return this.read( UINT16s, amount ); }
	public int[] readUShorts( long amount ) { return this.readUInt16s( amount ); }
	
	public int readInt32() { return this.read( INT32 ); }
	public int readInt() { return this.readInt32(); }
	public int[] readInt32s( long amount ) { return this.read( INT32s, amount ); }
	public int[] readInts( long amount ) { return this.readInt32s( amount ); }
	
	public long readUInt32() { return this.read( UINT32 ); }
	public long readUInt() { return this.readUInt32(); }
	public long[] readUInt32s( long amount ) { return this.read( UINT32s, amount ); }
	public long[] readUInts( long amount ) { return this.readUInt32s( amount ); }
	
	public long readInt64() { return this.read( INT64 ); }
	public long readLong() { return this.readInt64(); }
	public long[] readInt64s( long amount ) { return this.read( INT64s, amount ); }
	public long[] readLongs( long amount ) { return this.readInt64s( amount ); }
	
	public float readFloat32() { return this.read( FLOAT32 ); }
	public float readFloat() { return this.readFloat32(); }
	public float[] readFloat32s( long amount ) { return this.read( FLOAT32s, amount ); }
	public float[] readFloats( long amount ) { return this.readFloat32s( amount ); }
	
	public double readFloat64() { return this.read( FLOAT64 ); }
	public double readDouble() { return this.readFloat64(); }
	public double[] readFloat64s( long amount ) { return this.read( FLOAT64s, amount ); }
	public double[] readDoubles( long amount ) { return this.readFloat64s( amount ); }
}
