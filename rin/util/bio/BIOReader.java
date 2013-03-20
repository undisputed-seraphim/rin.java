package rin.util.bio;

import rin.util.bio.BIOTypes.Type;
import static rin.util.bio.BIOTypes.*;

public abstract class BIOReader {
	public abstract BIOBuffer getBuffer();
	
	public int position() { return this.getBuffer().actual().position(); }
	public void position( long position ) { this.getBuffer().actual().position( (int)position ); }
	
	public void reset() { this.position( 0 ); }
	
	public void advance() { this.advance( 1 ); }
	public void advance( long bytes ) { this.position( this.position() + bytes ); }
	
	public void rewind() { this.rewind( 1 ); }
	public void rewind( long bytes ) { this.position( this.position() - bytes ); }
	
	public <T> T preview( Type<T> type, long amount ) {
		int start = this.getBuffer().position();
		T res = type.getData( this.getBuffer().actual(), (int)amount );
		this.getBuffer().position( start );
		return res;
	}
	
	public byte previewInt8() { return this.preview( INT8, 1 )[0]; }
	public byte[] previewInt8s( long amount ) { return this.preview( INT8, amount ); }
	public short previewUInt8() { return this.preview( UINT8, 1 )[0]; }
	public short[] previewUInt8s( long amount ) { return this.preview( UINT8, amount ); }
	
	public short previewInt16() { return this.preview( INT16, 1 )[0]; }
	public short[] previewInt16s( long amount ) { return this.preview( INT16, amount ); }
	public int previewUInt16() { return this.preview(UINT16, 1 )[0]; }
	public int[] previewUInt16s( long amount ) { return this.preview( UINT16, amount ); }
	
	public int previewInt32() { return this.preview( INT32, 1 )[0]; }
	public int[] previewInt32s( long amount ) { return this.preview( INT32, amount ); }
	public long previewUInt32() { return this.preview( UINT32, 1 )[0]; }
	public long[] previewUInt32s( long amount ) { return this.preview( UINT32, amount ); }
	
	public long previewInt64() { return this.preview( INT64, 1 )[0]; }
	public long[] previewInt64s( long amount ) { return this.preview( INT64, amount ); }
	
	public float previewFloat() { return this.preview( FLOAT, 1 )[0]; }
	public float[] previewFloats( long amount ) { return this.preview( FLOAT, amount ); }
	
	public <T> T read( Type<T> type ) { return type.getData( this.getBuffer().actual(), 1 ); }
	public <T> T read( Type<T> type, long amount ) { return type.getData( this.getBuffer().actual(), (int)amount ); }
	
	public byte readInt8() { return this.read( INT8, 1 )[0]; }
	public byte[] readInt8s( long amount ) { return this.read( INT8, amount ); }
	public short readUInt8() { return this.read( UINT8, 1 )[0]; }
	public short[] readUInt8s( long amount ) { return this.read( UINT8, amount ); }
	
	public char readChar() { return this.read( CHAR, 1 )[0]; }
	public char[] readChars( long amount ) { return this.read( CHAR, amount ); }
	
	public String readString( long length ) { return this.read( STRING8, length )[0]; }
	
	public short readInt16() { return this.read( INT16, 1 )[0]; }
	public short[] readInt16s( long amount ) { return this.read( INT16, amount ); }
	public int readUInt16() { return this.read(UINT16, 1 )[0]; }
	public int[] readUInt16s( long amount ) { return this.read( UINT16, amount ); }
	
	public int readInt32() { return this.read( INT32, 1 )[0]; }
	public int[] readInt32s( long amount ) { return this.read( INT32, amount ); }
	public long readUInt32() { return this.read( UINT32, 1 )[0]; }
	public long[] readUInt32s( long amount ) { return this.read( UINT32, amount ); }
	
	public long readInt64() { return this.read( INT64, 1 )[0]; }
	public long[] readInt64s( long amount ) { return this.read( INT64, amount ); }
	
	public float readFloat() { return this.read( FLOAT, 1 )[0]; }
	public float[] readFloats( long amount ) { return this.read( FLOAT, amount ); }
	
	public double readDouble() { return this.read( DOUBLE, 1 )[0]; }
	public double[] readDoubles( long amount ) { return this.read( DOUBLE, amount ); }
}
