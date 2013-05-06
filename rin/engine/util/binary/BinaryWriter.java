package rin.engine.util.binary;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import rin.engine.resource.Resource;
import rin.engine.util.FileUtils;

public class BinaryWriter {

	private DataOutputStream stream;
	
	public void load( Resource resource ) {
		stream = new DataOutputStream( FileUtils.getOutputStream( resource.getTarget() ) );
	}
	
	public int length() { return stream.size(); }
	public void close() {
		try {
			stream.flush();
			stream.close();
		} catch( IOException ex ) {
			System.out.println( "BinaryWriter#close(): IOException." );
		}
	}
	
	public void writeBuffer( ByteBuffer bb ) {
		bb.rewind();
		for( byte b : bb.array() )
			writeInt8( b );
	}
	
	public void writeString( String s ) {
		for( int i = 0; i < s.length(); i++ )
			writeChar( s.charAt( i ) );
	}
	
	public void writeString( String[] ss ) {
		for( String s : ss )
			writeString( s );
	}
	
	public void writeChar( char c ) {
		writeInt8( (byte)c );
	}
	
	public void writeInt8( byte b ) {
		try {
			stream.writeByte( b );
		} catch( IOException ex ) {
			System.out.println( "BinaryWriter#writeInt8(byte): IOException." );
		}
	}
	
	public void writeInt8( byte[] bs ) {
		for( byte b : bs )
			writeInt8( b );
	}
	
	public void writeUInt8( short s ) {
		try {
			stream.writeByte( (byte)s );
		} catch( IOException ex ) {
			System.out.println( "BinaryWriter#writeUInt8(byte): IOException." );
		}
	}
	
	public void writeUInt8( short[] ss ) {
		for( short s : ss )
			writeUInt8( s );
	}
	
	public void writeInt16( short s ) {
		try {
			stream.writeShort( s );
		} catch( IOException ex ) {
			System.out.println( "BinaryWriter#writeInt16(short): IOException." );
		}
	}
	
	public void writeInt16( short[] ss ) {
		for( short s : ss )
			writeInt16( s );
	}
	
	public void writeUInt16( int i ) {
		try {
			stream.writeShort( (short)i );
		} catch( IOException ex ) {
			System.out.println( "BinaryWriter#writeUInt16(int): IOException." );
		}
	}
	
	public void writeUInt16( int[] ii ) {
		for( int i : ii )
			writeUInt16( i );
	}
	
	public void writeInt32( int i ) {
		try {
			stream.writeInt( i );
		} catch( IOException ex ) {
			System.out.println( "BinaryWriter#writeInt32(int): IOException." );
		}
	}
	
	public void writeInt32( int[] ii ) {
		for( int i : ii )
			writeInt32( i );
	}
	
	public void writeUInt32( long l ) {
		try {
			stream.writeInt( (int)l );
		} catch( IOException ex ) {
			System.out.println( "BinaryWriter#writeUInt32(long): IOException." );
		}
	}
	
	public void writeUInt32( long[] ll ) {
		for( long l : ll )
			writeUInt32( l );
	}
	
	public void writeInt64( long l ) {
		try {
			stream.writeLong( l );
		} catch( IOException ex ) {
			System.out.println( "BinaryWriter#writeInt64(long): IOException." );
		}
	}
	
	public void writeInt64( long[] ll ) {
		for( long l : ll )
			writeInt64( l );
	}
	
	public void writeFloat32( float f ) {
		try {
			stream.writeFloat( f );
		} catch( IOException ex ) {
			System.out.println( "BinaryWriter#writeFloat32(float): IOException." );
		}
	}
	
	public void writeFloat32( float[] ff ) {
		for( float f : ff )
			writeFloat32( f );
	}
	
	public void writeFloat64( double d ) {
		try {
			stream.writeDouble( d );
		} catch( IOException ex ) {
			System.out.println( "BinaryWriter#writeFloat64(double): IOException." );
		}
	}
	
	public void writeFloat64( double[] dd ) {
		for( double d : dd )
			writeFloat64( d );
	}
	
}
