package rin.util.bio;

import java.nio.ByteBuffer;
/**
 * <p>
 * java types
 * <ul>
 * <li>byte - 8bit signed</li>
 * <li>char - 16bit unsigned</li>
 * <li>short - 16bit signed</li>
 * <li>int - 32bit signed</li>
 * <li>long - 64bit signed</li>
 * <li>float - 32bit single precision</li>
 * <li>double - 64bit double precision</li>
 * </ul>
 */
public class BIOTypes {
	public static abstract class Type<T> {
		public static <T> Type<T> copy( final Type<T> t, final String id ) {
			return new Type<T>() {
				@Override public String toString() { return id; }
				@Override public T getData( ByteBuffer bb, int amount ) { return t.getData( bb, amount ); }
			};
		}
		
		public abstract String toString();
		public abstract T getData( ByteBuffer bb, int amount );
		
		public Type<T> copy() { return this.copy( this.toString() + "_COPY" ); }
		public Type<T> copy( final String id ) {
			final Type<T> t = this;
			return new Type<T>() {
				@Override public String toString() { return id; }
				@Override public T getData( ByteBuffer bb, int amount ) { return t.getData( bb, amount ); }
			};
		}
	}
	
	public static final Type<String[]> STRING8 = new Type<String[]>() {
		public String toString() { return "STRING8"; }
		public String[] getData( ByteBuffer bb, int length ) {
			return new String[] { BIOBuffer.asString( CHAR8.getData( bb, length ), true ) };
		}
	};
	
	public static final Type<String[]> BIT8 = new Type<String[]>() {
		public String toString() { return "BIT8"; }
		public String[] getData( ByteBuffer bb, int amount ) {
			String[] res = new String[amount];
			for( int i = 0; i < amount; i++ )
				res[i] = String.format( "%08d", Integer.parseInt( Integer.toBinaryString( bb.get() ) ) );
			return res;
		}
	};
	
	public static final Type<String[]> HEX8 = new Type<String[]>() {
		public String toString() { return "HEX8"; }
		public String[] getData( ByteBuffer bb, int amount ) {
			String[] res = new String[ amount ];
			for( int i = 0; i < amount; i++ )
				res[i] = String.format( "%08x", Integer.parseInt( Integer.toBinaryString( bb.get() ) ) );
			return res;
		}
	};
	
	public static final Type<short[]> UINT8 = new Type<short[]>() {
		public String toString() { return "UINT8"; }
		public short[] getData( ByteBuffer bb, int amount ) {
			short[] res = new short[ amount ];
			for( int i = 0; i < amount; i++ )
				res[i] = (short)(bb.get() & 0xFF);
			return res;
		}
	};
	
	public static final Type<byte[]> INT8 = new Type<byte[]>() {
		public String toString() { return "INT8"; }
		public byte[] getData( ByteBuffer bb, int amount ) {
			byte[] res = new byte[ amount ];
			for( int i = 0; i < amount; i++ )
				res[i] = bb.get();
			return res;
		}
	};
	
	public static final Type<char[]> CHAR8 = new Type<char[]>() {
		public String toString() { return "CHAR8"; }
		public char[] getData( ByteBuffer bb, int amount ) {
			char[] res = new char[ amount ];
			for( int i = 0; i < amount; i++ )
				res[i] = new String( new byte[] { bb.get() } ).charAt( 0 );
			return res;
		}
	};
	
	public static final Type<int[]> UINT16 = new Type<int[]>() {
		public String toString() { return "UINT16"; }
		public int[] getData( ByteBuffer bb, int amount ) {
			int[] res = new int[ amount ];
			for( int i = 0; i < amount; i++ )
				res[i] = bb.getShort() & 0xFFFF;
			return res;
		}
	};
	
	public static final Type<short[]> INT16 = new Type<short[]>() {
		public String toString() { return "INT16"; }
		public short[] getData( ByteBuffer bb, int amount ) {
			short[] res = new short[ amount ];
			for( int i = 0; i < amount; i++ )
				res[i] = bb.getShort();
			return res;
		}
	};
	
	public static final Type<long[]> UINT32 = new Type<long[]>() {
		@Override public String toString() { return "UINT32"; }
		@Override public long[] getData( ByteBuffer bb, int amount ) {
			long[] res = new long[ amount ];
			for( int i = 0; i < amount; i++ )
				res[i] = (long)bb.getInt() & 0xFFFFFFFFL;
			return res;
		}
	};
	
	public static final Type<int[]> INT32 = new Type<int[]>() {
		public String toString() { return "INT32"; }
		public int[] getData( ByteBuffer bb, int amount ) {
			int[] res = new int[ amount ];
			for( int i = 0; i < amount; i++ )
				res[i] = bb.getInt();
			return res;
		}
	};
	
	public static final Type<long[]> INT64 = new Type<long[]>() {
		public String toString() { return "INT64"; }
		public long[] getData( ByteBuffer bb, int amount ) {
			long[] res = new long[ amount ];
			for( int i = 0; i < amount; i++ )
				res[i] = bb.getLong();
			return res;
		}
	};
	
	public static final Type<short[]> UBYTE = Type.copy( UINT8, "UBYTE" );
	public static final Type<byte[]> BYTE = Type.copy( INT8, "BYTE" );
	
	public static final Type<char[]> CHAR = Type.copy( CHAR8, "CHAR" );
	public static final Type<String[]> STRING = Type.copy( STRING8, "STRING" );
	
	public static final Type<int[]> USHORT = Type.copy( UINT16, "USHORT" );
	public static final Type<short[]> SHORT = Type.copy( INT16, "SHORT" );
	
	public static final Type<long[]> UINT = Type.copy( UINT32, "UINT" );
	public static final Type<int[]> INT = Type.copy( INT32, "INT" );
	
	public static final Type<long[]> LONG = INT64.copy( "LONG" );
	
	public static final Type<float[]> FLOAT = new Type<float[]>() {
		public String toString() { return "FLOAT"; }
		public float[] getData( ByteBuffer bb, int amount ) {
			float[] res = new float[ amount ];
			for( int i = 0; i < amount; i++ )
				res[i] = bb.getFloat();
			return res;
		}
	};
	
	public static final Type<double[]> DOUBLE = new Type<double[]>() {
		public String toString() { return "DOUBLE"; }
		public double[] getData( ByteBuffer bb, int amount ) {
			double[] res = new double[ amount ];
			for( int i = 0; i < amount; i++ )
				res[i] = bb.getDouble();
			return res;
		}
	};
}
