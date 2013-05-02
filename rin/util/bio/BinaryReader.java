package rin.util.bio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import rin.engine.util.ArrayUtils;

import static rin.util.bio.BinaryTypes.*;

public abstract class BinaryReader {

	public static class BinaryBufferPositionException extends Error {
		public BinaryBufferPositionException( int length, int position ) {
			System.err.println( length + " " + position );
			this.printStackTrace();
		}
	}
	
	private final ByteBuffer tmp = ByteBuffer.allocate( 8 );
	
	public abstract ByteBuffer getBuffer();
	
    public int position() { return this.getBuffer().position(); }
    public void position( int pos ) {
    	if( pos > this.getBuffer().capacity() ) {
    		throw new BinaryBufferPositionException( length(), pos );
    	}
    	
    	this.getBuffer().position( pos );
    }

    public void rewind( int amount ) { this.position( this.position() - amount ); }
    public void advance( int amount ) { this.position( this.position() + amount ); }

    public int length() { return this.getBuffer().capacity(); }
    
    public void setBigEndian() { this.getBuffer().order( ByteOrder.BIG_ENDIAN ); }
    public void setLittleEndian() { this.getBuffer().order( ByteOrder.LITTLE_ENDIAN ); }

    private void ensureSize( int bytes, int amount, String type ) {
    	if( this.position() + bytes * amount > this.length() )
    		System.out.println( "BinaryBufferCapacityException" );
    }
    
    private int ensureRange( int bytes, int start, int end, String type ) {
    	int amount = (end - start) / bytes;
    	
    	if( !(amount % bytes == 0) ) {
    		System.out.println( "BinaryRangeException" );
    		amount = 0;
    	}
    	
    	return amount;
    }
    
    private int createMask( int length ) {
    	String res = "";
    	for( int i = 0; i < length; i++ )
    		res += "1";
    	return Integer.parseInt( res, 2 );
    }
    
    public <T> int getBits( int value, int bits, int index ) {
    	return ( value & ( createMask( bits ) << index ) ) >> index;
    }
    
    public <T> T read( BinaryType<T> type ) { return this.read( type, false ); }
    private <T> T read( BinaryType<T> type, boolean preview ) {
        int start = this.position();

        T res = type.getData( this.getBuffer() );

        if( preview ) this.position( start );
        return res;
    }

    public <T> T[] read( BinaryType<T> type, int amount ) { return this.read( type, amount, false ); }
    private <T> T[] read( BinaryType<T> type, int amount, boolean preview ) {
        int start = this.position();

        T[] res = type.allocate( amount );
        for( int i = 0; i < res.length; i++ )
            res[i] = this.read( type );

        if( preview ) this.position( start );
        return res;
    }

    public <T> T readAt( BinaryType<T> type, int pos ) {
    	int start = this.position();
    	this.position( pos );
    	
    	T res = this.read( type );
    	this.position( start );
    	return res;
    }
    
    public <T> T[] readAt( BinaryType<T> type, int pos, int amount ) {
    	return this.readRange( type, pos, pos + (amount * type.sizeof()) );
    }
    
    public <T> T[] readRange( BinaryType<T> type, int start, int end ) {
    	int init = this.position();
    	this.position( start );
    	
    	int amount = this.ensureRange( type.sizeof(), start, end, type.getClass().toString() );
    	T[] res = this.read( type, amount );
    	this.position( init );
    	return res;
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
    
    public String readStringAt( int pos, int length ) {
    	int start = this.position();
    	this.position( pos );
    	
    	String res = this.readString( length );
    	this.position( start );
    	return res;
    }
    
    public byte[] readBits() {
    	byte[] res = new byte[8];
    	
    	byte b = this.getBuffer().get();
    	for( int i = 0; i < 8; i++ )
    		res[i] = ((b >> i) & 1) == 1 ? (byte)1 : (byte)0;
    	return ArrayUtils.flip( res );
    }
    
    public int getBitSet( byte value, int bit ) {
    	return ( value & ( 1 << bit ) );
    } 
    
    public byte[] readBits32() {
    	byte[] res = new byte[8*4];
    	//[ 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 0, 0 ]
    	//[ 0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0 ]
    	byte[] bytes = ByteBuffer.allocate( 4 ).putInt( readInt32() ).array();
    	int k = 0;
    	for( int i = 0; i < bytes.length; i++ )
    		for( int j = 0; j < 8; j++ )
    			res[k++] = ((bytes[i] >> j) & 1) == 1 ? (byte)1 : (byte)0;
    	return ArrayUtils.flip( res );
    }
    
    public byte[][] readBits( int amount ) {
    	byte[][] res = new byte[amount][];
    	for( int i = 0; i < amount; i++ )
    		res[i] = readBits();
    	
    	return res;
    }
    
    public String readBit8() { return BIT8.getData( this.getBuffer() ); }
    public String[] readBit8( int amount ) {
    	String[] res = new String[amount];
    	for( int i = 0; i < amount; i++ )
    		res[i] = this.readBit8();
    	return res;
    }
    
    public String readBit8At( int pos ) {
    	int start = this.position();
    	this.position( pos );
    	
    	String res = this.readBit8();
    	this.position( start );
    	return res;
    }
    
    public String[] readBit8At( int pos, int amount ) { return this.readBit8Range( pos, pos + (amount * 1) ); }
    public String[] readBit8Range( int start, int end ) {
    	int init = this.position();
    	this.position( start );
    	
    	int amount = this.ensureRange( 1, start, end, "Bit8" );
    	String[] res = this.readBit8( amount );
    	this.position( init );
    	return res;
    }
    
    public int readHex() { return Integer.parseInt( String.format( "%02x", this.getBuffer().get() ), 16 ); }
    public int[] readHex( int amount ) {
    	int[] res = new int[amount];
    	for( int i = 0; i < amount; i++ )
    		res[i] = this.readHex();
    	return res;
    }
    
    public String readHex8() { return String.format( "0x%02x", this.getBuffer().get() ); }
    public String[] readHex8( int amount ) {
    	String[] res = new String[amount];
    	for( int i = 0; i < amount; i++ )
    		res[i] = this.readHex8();
    	return res;
    }
    
    public String readHex8At( int pos ) {
    	int start = this.position();
    	this.position( pos );
    	
    	String res = this.readHex8();
    	this.position( start );
    	return res;
    }
    
    public String[] readHex8At( int pos, int amount ) { return this.readHex8Range( pos, pos + (amount * 1) ); }
    public String[] readHex8Range( int start, int end ) {
    	int init = this.position();
    	this.position( start );
    	
    	int amount = this.ensureRange( 1, start, end, "Hex8" );
    	String[] res = this.readHex8( amount );
    	this.position( init );
    	return res;
    }
    
    public BinaryReader printHex8() { return printHex8( 1 ); }
    public BinaryReader printHex8( int amount ) {
    	for( int i = 0; i < amount; i++ )
    		System.out.println( readHex8() );
    	return this;
    }
    
    public String readHex16() { return String.format( "0x%04x", this.getBuffer().getShort() ); }
    
    public byte readInt8() { return this.getBuffer().get(); }
    public byte[] readInt8( int amount ) {
        byte[] res = new byte[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readInt8();
        return res;
    }

    public byte readInt8At( int pos ) {
    	int start = this.position();
    	this.position( pos );
    	
    	byte res = this.readInt8();
    	this.position( start );
    	return res;    	
    }

    public byte[] readInt8At( int pos, int amount ) { return this.readInt8Range( pos, pos + (amount * 1) ); }
    public byte[] readInt8Range( int start, int end ) {
    	int init = this.position();
    	this.position( start );
    	
    	int amount = this.ensureRange( 1, start, end, "Int8" );
    	byte[] res = this.readInt8( amount );
    	this.position( init );
    	return res;
    }
    
    public short readUInt8() { return (short)(this.getBuffer().get() & 0xFF); }
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

    public short readUInt8At( int pos ) {
    	int start = this.position();
    	this.position( pos );
    	
    	short res = this.readUInt8();
    	this.position( start );
    	return res;
    }

    public short[] readUInt8At( int pos, int amount ) { return this.readUInt8Range( pos, pos + (amount * 1) ); }
    public short[] readUInt8Range( int start, int end ) {
    	int init = this.position();
    	this.position( start );
    	
    	int amount = this.ensureRange( 1, start, end, "UInt8" );
    	short[] res = this.readUInt8( amount );
    	this.position( init );
    	return res;
    }
    
    public BinaryReader printUInt8() { return printUInt8( 1 ); }
    public BinaryReader printUInt8( int amount ) {
    	for( int i = 0; i < amount; i++ )
    		System.out.println( readUInt8() );
    	return this;
    }
    
    public char readChar() { return new String( new byte[] { this.getBuffer().get() } ).charAt( 0 ); }
    public char[] readChar( int amount ) {
        char[] res = new char[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readChar();
        return res;
    }

    public char readCharAt( int pos ) {
    	int start = this.position();
    	this.position( pos );
    	
    	char res = this.readChar();
    	this.position( start );
    	return res;    
    }

    public BinaryReader printChar() { return printChar( 1 ); }
    public BinaryReader printChar( int amount ) {
    	for( int i = 0; i < amount; i++ )
    		System.out.println( readChar() );
    	return this;
    }
    
    public char[] readCharAt( int pos, int amount ) { return this.readCharRange( pos, pos + (amount * 1) ); }
    public char[] readCharRange( int start, int end ) {
    	int init = this.position();
    	this.position( start );
    	
    	int amount = this.ensureRange( 1, start, end, "Char" );
    	char[] res = this.readChar( amount );
    	this.position( init );
    	return res;
    }
    
    public short readInt16() { return this.getBuffer().getShort(); }
    public short[] readInt16( int amount ) {
        short[] res = new short[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readInt16();
        return res;
    }
    
    public int readUInt16At( int pos ) {
    	int start = this.position();
    	this.position( pos );
    	
    	int res = this.readUInt16();
    	this.position( start );
    	return res;    
    }
    
    public BinaryReader printInt16() { return printInt16( 1 ); }
    public BinaryReader printInt16( int amount ) {
    	for( int i = 0; i < amount; i++ )
    		System.out.println( readInt16() );
    	return this;
    }

    public short[] readInt16Range( int start, int end ) {
    	int init = this.position();
    	this.position( start );
    	
    	int amount = this.ensureRange( 2, start, end, "Int16" );
    	short[] res = this.readInt16( amount );
    	this.position( init );
    	return res;
    }
    
    public int readUInt16() { return this.getBuffer().getShort() & 0xFFFF; }
    public int[] readUInt16( int amount ) {
        int[] res = new int[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readUInt16();
        return res;
    }

    public int[] readUInt16Range( int start, int end ) {
    	int init = this.position();
    	this.position( start );
    	
    	int amount = this.ensureRange( 2, start, end, "UInt16" );
    	int[] res = this.readUInt16( amount );
    	this.position( init );
    	return res;
    }
    
    public BinaryReader printUInt16() { return printUInt16( 1 ); }
    public BinaryReader printUInt16( int amount ) {
    	for( int i = 0; i < amount; i++ )
    		System.out.println( readUInt16() );
    	return this;
    }
    
    public int readInt32() { return this.getBuffer().getInt(); }
    public int[] readInt32( int amount ) {
        int[] res = new int[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readInt32();
        return res;
    }

    public int[] readInt32Range( int start, int end ) {
    	int init = this.position();
    	this.position( start );
    	
    	int amount = this.ensureRange( 4, start, end, "Int32" );
    	int[] res =  this.readInt32( amount );
    	this.position( init );
    	return res;
    }
    
    public BinaryReader printInt32() { return printInt32( 1 ); }
    public BinaryReader printInt32( int amount ) {
    	for( int i = 0; i < amount; i++ )
    		System.out.println( readInt32() );
    	return this;
    }
    
    public long readUInt32() { return this.getBuffer().getInt() & 0xFFFFFFFFL; }
    public long[] readUInt32( int amount ) {
        long[] res = new long[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readUInt32();
        return res;
    }
    
    public long[] readUInt32Range( int start, int end ) {
    	int init = this.position();
    	this.position( start );
    	
    	int amount = this.ensureRange( 4, start, end, "UInt32" );
    	long[] res = this.readUInt32( amount );
    	this.position( init );
    	return res;
    }

    public BinaryReader printUInt32() { return printUInt32( 1 ); }
    public BinaryReader printUInt32( int amount ) {
    	for( int i = 0; i < amount; i++ )
    		System.out.println( readUInt32() );
    	return this;
    }
    
    public long readInt64() { return this.getBuffer().getLong(); }
    public long[] readInt64( int amount ) {
        long[] res = new long[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readInt64();
        return res;
    }
    
    public long[] readInt64Range( int start, int end ) {
    	int init = this.position();
    	this.position( start );
    	
    	int amount = this.ensureRange( 8, start, end, "Int64" );
    	long[] res = this.readInt64( amount );
    	this.position( init );
    	return res;
    }

    public BinaryReader printInt64() { return printInt64( 1 ); }
    public BinaryReader printInt64( int amount ) {
    	for( int i = 0; i < amount; i++ )
    		System.out.println( readInt64() );
    	return this;
    }
    
    public float readFloat16e( int x ) {
    	int sign = x < 0 ? 1 : 0;
    	int absx = (x ^ -sign) + sign;
    	int tmp = absx;
    	int manbits = 0;
    	int exp = 0;
    	int truncated = 0;
    	
    	while( tmp != 0 ) {
    		tmp >>= 1;
    		manbits++;
    	}
    	
    	if( manbits != 0 ) {
    		exp = 10;
    		while( manbits > 11 ) {
    			truncated |= absx & 1;
    			absx >>= 1;
    			manbits--;
    			exp++;
    		}
    		
    		while( manbits < 11 ) {
    			absx <<= 1;
    			manbits++;
    			exp--;
    		}
    	}
    	
    	if( exp + truncated > 15 ) {
    		exp = 31;
    		absx = 0;
    	} else if( manbits != 0 ) {
    		exp += 15;
    	}
    	
    	return ( sign << 15 ) | ((exp & 0xFF) << 10) | (absx & ((1 << 10) - 1) );
    }
    
    //TODO: for the love of god tidy this code up
    public float readFloat16() {
    	short s = this.getBuffer().getShort();
    	int exponent = (s & 0x7C00) >> 10;
    	int fraction = (s & 0x03FF);
    	return (float)(( (s >> 15 != 0) ? -1 : 1) * ( exponent != 0 ?
    			(
    					exponent == 0x1F ?
    							fraction != 0 ?
    									Float.NaN : Float.POSITIVE_INFINITY :
    										Math.pow( 2, exponent - 15) * (1 + fraction / 0x400)
    			) : 0x0400 * (fraction / 0x400) ));
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
    
    public float readFloat32() { return this.getBuffer().getFloat(); }
    public float[] readFloat32( int amount ) {
        float[] res = new float[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readFloat32();
        return res;
    }

    public float[] readFloat32Range( int start, int end ) {
    	int init = this.position();
    	this.position( start );
    	
    	int amount = this.ensureRange( 4, start, end, "Float32" );
    	float[] res = this.readFloat32( amount );
    	this.position( init );
    	return res;
    }
    
    public BinaryReader printFloat32() { return printFloat32( 1 ); }
    public BinaryReader printFloat32( int amount ) {
    	for( int i = 0; i < amount; i++ )
    		System.out.println( readFloat32() );
    	return this;
    }
    
    public double readFloat64() { return this.getBuffer().getDouble(); }
    public double[] readFloat64( int amount ) {
        double[] res = new double[amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = this.readFloat64();
        return res;
    }
    
    public double[] readFloat64Range( int start, int end ) {
    	int init = this.position();
    	this.position( start );
    	
    	int amount = this.ensureRange( 8, start, end, "Float64" );
    	double[] res = this.readFloat64( amount );
    	this.position( init );
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
