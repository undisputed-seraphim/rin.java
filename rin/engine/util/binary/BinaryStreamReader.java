package rin.engine.util.binary;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

import rin.engine.resource.Resource;
import rin.engine.util.FileUtils;

public class BinaryStreamReader {

	private final ByteBuffer INT8  = ByteBuffer.allocate( 1 );
	private final ByteBuffer INT16 = ByteBuffer.allocate( 2 );
	private final ByteBuffer INT32 = ByteBuffer.allocate( 4 );
	private final ByteBuffer INT64 = ByteBuffer.allocate( 8 );
	
	private File file;
	private FileChannel channel;
	
	public void load( Resource resource ) {
		channel = FileUtils.getInputStream( resource.getTarget() ).getChannel();
		file = resource.getTarget();
	}
	
    public void setBigEndian() {
    	INT8.order( ByteOrder.BIG_ENDIAN );
    	INT16.order( ByteOrder.BIG_ENDIAN );
    	INT32.order( ByteOrder.BIG_ENDIAN );
    	INT64.order( ByteOrder.BIG_ENDIAN );
    }
    
    public void setLittleEndian() {
    	INT8.order( ByteOrder.LITTLE_ENDIAN );
    	INT16.order( ByteOrder.LITTLE_ENDIAN );
    	INT32.order( ByteOrder.LITTLE_ENDIAN );
    	INT64.order( ByteOrder.LITTLE_ENDIAN );
    }
    
    public long position() {
    	long position = -1L;
    	try {
    		position = channel.position();
		} catch( IOException ex ) {
			System.err.println( "BinaryStreamReader#length(): IOException raised." );
		}
    	return position;
    }
    
    public void position( long position ) {
    	try {
			channel.position( position );
		} catch (IOException e) {
			System.err.println( "BinaryStreamReader#position(long): IOException raised." );
		}
    }
    
    public void advance( long bytes ) { position( position() + bytes ); }
    public void rewind( long bytes ) { position( position() - bytes ); }
    public long length() { return file.length(); }
    
	private ByteBuffer read( ByteBuffer data ) {
		data.clear();
		try {
			channel.read( data );
		} catch (IOException e) {
			System.err.println( "BinaryStreamReader#read(ByteBuffer): IOException raised." );
		}
		data.flip();
		return data;
	}
	
	private byte getByte() { return read( INT8 ).get(); }
	private short getShort() { return read( INT16 ).getShort(); }
	private int getInt() { return read( INT32 ).getInt(); }
	private long getLong() { return read( INT64 ).getLong(); }
	
	private float getFloat() { return read( INT32 ).getFloat(); }
	private double getDouble() { return read( INT64 ).getDouble(); }
	
	public char readChar() { return new String( new byte[] { getByte() } ).charAt( 0 ); }
	public char[] readChar( long amount ) {
		char[] res = new char[(int)amount];
		for( int i = 0; i < res.length; i++ )
			res[i] = readChar();
		return res;
	}
	
	public void printChar() { System.out.println( readChar() ); }
	public void printChar( long amount ) {
		for( int i = 0; i < amount; i++ )
			printChar();
	}
	
	public int readHex8() { return Integer.parseInt( String.format( "%02x", getByte() ), 16 ); }
	public int[] readHex8( long amount ) {
		int[] res = new int[(int)amount];
		for( int i = 0; i < amount; i++ )
			res[i] = readHex8();
		return res;
	}
	
    public String readHexString8() { return String.format( "0x%02x", getByte() ); }
    public String[] readHexString8( long amount ) {
    	String[] res = new String[(int)amount];
    	for( int i = 0; i < res.length; i++ )
    		res[i] = readHexString8();
    	return res;
    }
    
	public void printHex8() { System.out.println( readChar() ); }
	public void printHex8( long amount ) {
		for( int i = 0; i < amount; i++ )
			printHex8();
	}
	
	public byte readInt8() { return getByte(); }
	public byte[] readInt8( long amount ) {
        byte[] res = new byte[(int)amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = readInt8();
        return res;
    }
	
	public void printInt8() { System.out.println( readInt8() ); }
	public void printInt8( long amount ) {
		for( int i = 0; i < amount; i++ )
			printInt8();
	}
	
    public short readUInt8() { return (short)(getByte() & 0xFF); }
    public short[] readUInt8( long amount ) {
        short[] res = new short[(int)amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = readUInt8();
        return res;
    }
    
	public void printUInt8() { System.out.println( readUInt8() ); }
	public void printUInt8( long amount ) {
		for( int i = 0; i < amount; i++ )
			printUInt8();
	}
	
    public short readInt16() { return getShort(); }
    public short[] readInt16( long amount ) {
        short[] res = new short[(int)amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = readInt16();
        return res;
    }
    
	public void printInt16() { System.out.println( readInt16() ); }
	public void printInt16( long amount ) {
		for( int i = 0; i < amount; i++ )
			printInt16();
	}
	
    public int readUInt16() { return getShort() & 0xFFFF; }
    public int[] readUInt16( long amount ) {
        int[] res = new int[(int)amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = readUInt16();
        return res;
    }
    
	public void printUInt16() { System.out.println( readUInt16() ); }
	public void printUInT16( long amount ) {
		for( int i = 0; i < amount; i++ )
			printUInt16();
	}
	
    public int readInt32() { return getInt(); }
    public int[] readInt32( long amount ) {
        int[] res = new int[(int)amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = readInt32();
        return res;
    }
    
	public void printInt32() { System.out.println( readInt32() ); }
	public void printInt32( long amount ) {
		for( int i = 0; i < amount; i++ )
			printInt32();
	}
	
    public long readUInt32() { return getInt() & 0xFFFFFFFFL; }
    public long[] readUInt32( long amount ) {
        long[] res = new long[(int)amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = readUInt32();
        return res;
    }
    
	public void printUInt32() { System.out.println( readUInt32() ); }
	public void printUInt32( long amount ) {
		for( int i = 0; i < amount; i++ )
			printUInt32();
	}
	
    public long readInt64() { return getLong(); }
    public long[] readInt64( long amount ) {
        long[] res = new long[(int)amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = readInt64();
        return res;
    }
    
	public void printInt64() { System.out.println( readInt64() ); }
	public void printInt64( long amount ) {
		for( int i = 0; i < amount; i++ )
			printInt64();
	}
	
    //TODO: wat.
    public float readFloat16() {
    	short s = getShort();
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
    
    public float[] readFloat16( long amount ) {
    	float[] res = new float[(int)amount];
    	for( int i = 0; i < amount; i++ )
    		res[i] = readFloat16();
    	return res;
    }
    
	public void printFloat16() { System.out.println( readFloat16() ); }
	public void printFloat16( long amount ) {
		for( int i = 0; i < amount; i++ )
			printFloat16();
	}
	
    public float readFloat32() { return getFloat(); }
    public float[] readFloat32( long amount ) {
        float[] res = new float[(int)amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = readFloat32();
        return res;
    }
    
	public void printFloat32() { System.out.println( readFloat32() ); }
	public void printFloat32( long amount ) {
		for( int i = 0; i < amount; i++ )
			printFloat32();
	}
	
    public double readFloat64() { return getDouble(); }
    public double[] readFloat64( long amount ) {
        double[] res = new double[(int)amount];
        for( int i = 0; i < res.length; i++ )
            res[i] = readFloat64();
        return res;
    }
    
	public void printFloat64() { System.out.println( readFloat64() ); }
	public void printFloat64( long amount ) {
		for( int i = 0; i < amount; i++ )
			printFloat64();
	}
	
    private int createMask( int length ) {
    	String res = "";
    	for( int i = 0; i < length; i++ )
    		res += "1";
    	return Integer.parseInt( res, 2 );
    }
    
    public int getBits( int value, int bits, int index ) {
    	return ( value & ( createMask( bits ) << index ) ) >> index;
    }
    
    protected void exitWithError( String error ) {
		System.err.println( error );
		System.exit( 0 );
	}
    
}
