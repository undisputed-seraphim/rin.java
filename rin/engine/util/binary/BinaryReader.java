package rin.engine.util.binary;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import rin.engine.resource.Resource;
import rin.engine.util.ArrayUtils;

public class BinaryReader {

	public static class BinaryBufferPositionException extends Error {
		private static final long serialVersionUID = 7L;

		public BinaryBufferPositionException( int length, int position ) {
			System.err.println( length + " " + position );
			this.printStackTrace();
		}
	}
	
	private ByteBuffer buffer;
	
	public void load( Resource resource ) {
		buffer = ByteBuffer.wrap( resource.toByteArray() );
	}
	
    public int position() { return buffer.position(); }
    public void position( int pos ) {
    	if( pos > buffer.capacity() ) {
    		throw new BinaryBufferPositionException( length(), pos );
    	}
    	
    	buffer.position( pos );
    }

    public void rewind( int amount ) { this.position( this.position() - amount ); }
    public void advance( int amount ) { this.position( this.position() + amount ); }

    public int length() { return buffer.capacity(); }
    
    public void setBigEndian() { buffer.order( ByteOrder.BIG_ENDIAN ); }
    public void setLittleEndian() { buffer.order( ByteOrder.LITTLE_ENDIAN ); }
    
    private int createMask( int length ) {
    	String res = "";
    	for( int i = 0; i < length; i++ )
    		res += "1";
    	return Integer.parseInt( res, 2 );
    }
    
    public BinaryChunk getChunk( int bytes ) {
    	return new BinaryChunk( readInt8( bytes ) );
    }
    
    public int getBits( int value, int bits, int index ) {
    	return ( value & ( createMask( bits ) << index ) ) >> index;
    }
    
    public String readString( int length ) {
        String res = "";
        for( int i = 0; i < length; i++ )
            res += this.readChar();
        return res;
    }

    public BinaryReader printString( int length ) {
    	System.out.println( readString( length ) );
    	return this;
    }
    
    public byte[] readBits() {
    	byte[] res = new byte[8];
    	
    	byte b = buffer.get();
    	for( int i = 0; i < 8; i++ )
    		res[i] = ((b >> i) & 1) == 1 ? (byte)1 : (byte)0;
    	return ArrayUtils.flip( res );
    }
    
    public byte[][] readBits( int amount ) {
    	byte[][] res = new byte[amount][];
    	for( int i = 0; i < amount; i++ )
    		res[i] = readBits();
    	
    	return res;
    }
    
    public int readHex() { return Integer.parseInt( String.format( "%02x", buffer.get() ), 16 ); }
    public int[] readHex( int amount ) {
    	int[] res = new int[amount];
    	for( int i = 0; i < amount; i++ )
    		res[i] = this.readHex();
    	return res;
    }
    
    public String readHex8() { return String.format( "0x%02x", buffer.get() ); }
    public String[] readHex8( int amount ) {
    	String[] res = new String[amount];
    	for( int i = 0; i < amount; i++ )
    		res[i] = this.readHex8();
    	return res;
    }
    
    public BinaryReader printHex8() { return printHex8( 1 ); }
    public BinaryReader printHex8( int amount ) {
    	for( int i = 0; i < amount; i++ )
    		System.out.println( readHex8() );
    	return this;
    }
    
    public String readHex16() { return String.format( "0x%04x", buffer.getShort() ); }
    
    public byte readInt8() { return buffer.get(); }
    public byte[] readInt8( int amount ) {
        byte[] res = new byte[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readInt8();
        return res;
    }
    
    public short readUInt8() { return (short)(buffer.get() & 0xFF); }
    public short[] readUInt8( int amount ) {
        short[] res = new short[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readUInt8();
        return res;
    }
    
    public BinaryReader printInt8() { return printInt8( 1 ); }
    public BinaryReader printInt8( int amount ) {
    	for( int i = 0; i < amount; i++ )
    		System.out.println( readInt8() );
    	return this;
    }
    
    public BinaryReader printUInt8() { return printUInt8( 1 ); }
    public BinaryReader printUInt8( int amount ) {
    	for( int i = 0; i < amount; i++ )
    		System.out.println( readUInt8() );
    	return this;
    }
    
    public char readChar() { return new String( new byte[] { buffer.get() } ).charAt( 0 ); }
    public char[] readChar( int amount ) {
        char[] res = new char[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readChar();
        return res;
    }

    public BinaryReader printChar() { return printChar( 1 ); }
    public BinaryReader printChar( int amount ) {
    	for( int i = 0; i < amount; i++ )
    		System.out.println( readChar() );
    	return this;
    }
    
    public short readInt16() { return buffer.getShort(); }
    public short[] readInt16( int amount ) {
        short[] res = new short[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readInt16();
        return res;
    }
    
    public BinaryReader printInt16() { return printInt16( 1 ); }
    public BinaryReader printInt16( int amount ) {
    	for( int i = 0; i < amount; i++ )
    		System.out.println( readInt16() );
    	return this;
    }
    
    public int readUInt16() { return buffer.getShort() & 0xFFFF; }
    public int[] readUInt16( int amount ) {
        int[] res = new int[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readUInt16();
        return res;
    }
    
    public BinaryReader printUInt16() { return printUInt16( 1 ); }
    public BinaryReader printUInt16( int amount ) {
    	for( int i = 0; i < amount; i++ )
    		System.out.println( readUInt16() );
    	return this;
    }
    
    public int readInt32() { return buffer.getInt(); }
    public int[] readInt32( int amount ) {
        int[] res = new int[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readInt32();
        return res;
    }
    
    public BinaryReader printInt32() { return printInt32( 1 ); }
    public BinaryReader printInt32( int amount ) {
    	for( int i = 0; i < amount; i++ )
    		System.out.println( readInt32() );
    	return this;
    }
    
    public long readUInt32() { return buffer.getInt() & 0xFFFFFFFFL; }
    public long[] readUInt32( int amount ) {
        long[] res = new long[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readUInt32();
        return res;
    }

    public BinaryReader printUInt32() { return printUInt32( 1 ); }
    public BinaryReader printUInt32( int amount ) {
    	for( int i = 0; i < amount; i++ )
    		System.out.println( readUInt32() );
    	return this;
    }
    
    public long readInt64() { return buffer.getLong(); }
    public long[] readInt64( int amount ) {
        long[] res = new long[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readInt64();
        return res;
    }

    public BinaryReader printInt64() { return printInt64( 1 ); }
    public BinaryReader printInt64( int amount ) {
    	for( int i = 0; i < amount; i++ )
    		System.out.println( readInt64() );
    	return this;
    }
    
    public float readFloat16() {
    	int v = readUInt16();
    	int s = (v & 0x8000);
    	int e = (v & 0x7C00) >> 10;
    	int m = (v & 0x03FF);
    	
    	float sign = ( s != 0 ? -1.0f : 1.0f );
    	if( e == 0 ) return sign * ( m == 0 ? 0.0f : (float)Math.pow(2.0f, -14.0f) * ((float)m/1024.0f));
    	if( e < 32 ) return sign * (float)Math.pow( 2.0f, (float)e - 15.0f) * (1.0f + (float)m/1024.0f);
    	return Float.NaN;
    }
    
    public float[] readFloat16( int amount ) {
    	float[] res = new float[amount];
    	for( int i = 0; i < amount; i++ )
    		res[i] = readFloat16();
    	return res;
    }
    
    public BinaryReader printFloat16() { return printFloat16( 1 ); }
    public BinaryReader printFloat16( int amount ) {
    	for( int i = 0; i < amount; i++ )
    		System.out.println( readFloat16() );
    	return this;
    }
    
    public float readFloat32() { return buffer.getFloat(); }
    public float[] readFloat32( int amount ) {
        float[] res = new float[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readFloat32();
        return res;
    }
    
    public BinaryReader printFloat32() { return printFloat32( 1 ); }
    public BinaryReader printFloat32( int amount ) {
    	for( int i = 0; i < amount; i++ )
    		System.out.println( readFloat32() );
    	return this;
    }
    
    public double readFloat64() { return buffer.getDouble(); }
    public double[] readFloat64( int amount ) {
        double[] res = new double[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readFloat64();
        return res;
    }
    
    public BinaryReader printFloat64() { return printFloat64( 1 ); }
    public BinaryReader printFloat64( int amount ) {
    	for( int i = 0; i < amount; i++ )
    		System.out.println( readFloat64() );
    	return this;
    }
    
    protected <T> T exitWithError( String error ) {
		System.err.println( error );
		return null;
	}
	
}
