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
		
		public abstract int sizeof();
		public abstract T[] allocate( int amount );
		public abstract T getData( ByteBuffer bb );
		
		@Override
		public abstract String toString();
		
	}
	
	public static final Type<String> BIT8 = new Type<String>() {
		
		@Override
		public int sizeof() { return 1; }

		@Override
		public String[] allocate( int amount ) { return new String[amount]; }
		
		@Override
		public String getData( ByteBuffer bb ) { return String.format( "%08d", Integer.parseInt( Integer.toBinaryString( bb.get() ) ) ); }
		
		@Override
		public String toString() { return "BIT8"; }
		
	};
	
	public static final Type<String> HEX8 = new Type<String>() {
		
		@Override
		public int sizeof() { return 1; }

		@Override
		public String[] allocate( int amount ) { return new String[amount]; }
		
		@Override
		public String getData( ByteBuffer bb ) { return String.format( "%08x", Integer.parseInt( Integer.toBinaryString( bb.get() ) ) ); }
		
		@Override
		public String toString() { return "HEX8"; }
		
	};
	
	public static final Type<Byte> INT8 = new Type<Byte>() {
		
		@Override
		public int sizeof() { return 1; }
		
		@Override
		public Byte[] allocate( int amount ) { return new Byte[amount]; }
		
		@Override
		public Byte getData( ByteBuffer bb ) { return bb.get(); }
		
		@Override
		public String toString() { return "INT8"; }
		
	};
	
	public static final Type<Short> UINT8 = new Type<Short>() {
		
		@Override
		public int sizeof() { return 1; }
		
		@Override
		public Short[] allocate( int amount ) { return new Short[amount]; }
		
		@Override
		public Short getData( ByteBuffer bb ) { return (short)(bb.get() & 0xFF); }
		
		@Override
		public String toString() { return "UINT8"; }
		
	};
	
	public static final Type<Character> CHAR8 = new Type<Character>() {
		
		@Override
		public int sizeof() { return 1; }
		
		@Override
		public Character[] allocate( int amount ) { return new Character[amount]; }
		
		@Override
		public Character getData( ByteBuffer bb ) { return new String( new byte[] { bb.get() } ).charAt( 0 ); }
		
		@Override
		public String toString() { return "CHAR8"; }
		
	};
	
	public static final Type<Character> CHAR = CHAR8;
	
	public static final Type<Short> INT16 = new Type<Short>() {
		
		@Override
		public int sizeof() { return 2; }
		
		@Override
		public Short[] allocate( int amount ) { return new Short[amount]; }
		
		@Override
		public Short getData( ByteBuffer bb ) { return bb.getShort(); }
		
		@Override
		public String toString() { return "INT16"; }
		
	};
	
	public static final Type<Integer> UINT16 = new Type<Integer>() {
		
		@Override
		public int sizeof() { return 2; }
		
		@Override
		public Integer[] allocate( int amount ) { return new Integer[amount]; }
		
		@Override
		public Integer getData( ByteBuffer bb ) { return bb.getShort() & 0xFFFF; }
		
		@Override
		public String toString() { return "UINT16"; }
		
	};
	
	public static final Type<Integer> INT32 = new Type<Integer>() {
		
		@Override
		public int sizeof() { return 4; }
		
		@Override
		public Integer[] allocate( int amount ) { return new Integer[amount]; }
		
		@Override
		public Integer getData( ByteBuffer bb ) { return bb.getInt(); }
		
		@Override
		public String toString() { return "INT32"; }
		
	};
	
	public static final Type<Long> UINT32 = new Type<Long>() {
		
		@Override
		public int sizeof() { return 4; }
		
		@Override
		public Long[] allocate( int amount ) { return new Long[amount]; }
		
		@Override
		public Long getData( ByteBuffer bb ) { return (long)bb.getInt() & 0xFFFFFFFFL; }
		
		@Override
		public String toString() { return "UINT32"; }
	};
	
	public static final Type<Long> INT64 = new Type<Long>() {
		
		@Override
		public int sizeof() { return 8; }
		
		@Override
		public Long[] allocate( int amount ) { return new Long[amount]; }
		
		@Override
		public Long getData( ByteBuffer bb ) { return bb.getLong(); }
		
		@Override
		public String toString() { return "INT64"; }
		
	};
	
	public static final Type<Float> FLOAT32 = new Type<Float>() {
		
		@Override
		public int sizeof() { return 4; }
		
		@Override
		public Float[] allocate( int amount ) { return new Float[amount]; }
		
		@Override
		public Float getData( ByteBuffer bb ) { return bb.getFloat(); }
		
		@Override
		public String toString() { return "FLOAT32"; }
		
	};
	
	public static final Type<Float> FLOAT = FLOAT32;
	
	public static final Type<Double> FLOAT64 = new Type<Double>() {
		
		@Override
		public int sizeof() { return 8; }
		
		@Override
		public Double[] allocate( int amount ) { return new Double[amount]; }
		
		@Override
		public Double getData( ByteBuffer bb ) { return bb.getDouble(); }
		
		@Override
		public String toString() { return "FLOAT64"; }
		
	};
	
	public static final Type<Double> DOUBLE = FLOAT64;
	
	/* -------------- array types ------------- */
	
	public static abstract class ArrayType<T> {
		
		public abstract int sizeof( int amount );
		public abstract T allocate( int amount );
		public abstract T getData( ByteBuffer bb, int amount );
		
		@Override
		public abstract String toString();
		
	}
	
	public static final ArrayType<byte[]> INT8s = new ArrayType<byte[]>() {
		
		@Override
		public int sizeof( int amount ) { return amount * INT8.sizeof(); }
		
		@Override
		public byte[] allocate( int amount ) { return new byte[amount]; }
		
		@Override
		public byte[] getData( ByteBuffer bb, int amount ) {
			byte[] res = new byte[amount];
			for( int i = 0; i < amount; i++ )
				res[i] = bb.get();
			return res;
		}
		
		@Override
		public String toString() { return "INT8[]"; }
		
	};
	
	public static final ArrayType<short[]> UINT8s = new ArrayType<short[]>() {
		
		@Override
		public int sizeof( int amount ) { return amount * UINT8.sizeof(); }
		
		@Override
		public short[] allocate( int amount ) { return new short[amount]; }
		
		@Override
		public short[] getData( ByteBuffer bb, int amount ) {
			short[] res = new short[amount];
			for( int i = 0; i < amount; i++ )
				res[i] = (short)(bb.get() & 0xFF);
			return res;
		}
		
		@Override
		public String toString() { return "UINT8[]"; }
		
	};
	
	public static final ArrayType<char[]> CHAR8s = new ArrayType<char[]>() {
		
		@Override
		public int sizeof( int amount ) { return amount * CHAR8.sizeof(); }
		
		@Override
		public char[] allocate( int amount ) { return new char[amount]; }
		
		@Override
		public char[] getData( ByteBuffer bb, int amount ) {
			char[] res = new char[amount];
			for( int i = 0; i < amount; i++ )
				res[i] = new String( new byte[] { bb.get() } ).charAt( 0 );
			return res;
		}
		
		@Override
		public String toString() { return "CHAR8[]"; }
		
	};
	
	public static final ArrayType<short[]> INT16s = new ArrayType<short[]>() {
		
		@Override
		public int sizeof( int amount ) { return amount * INT16.sizeof(); }
		
		@Override
		public short[] allocate( int amount ) { return new short[amount]; }
		
		@Override
		public short[] getData( ByteBuffer bb, int amount ) {
			short[] res = new short[amount];
			for( int i = 0; i < amount; i++ )
				res[i] = bb.getShort();
			return res;
		}
		
		@Override
		public String toString() { return "INT16[]"; }
		
	};
	
	public static final ArrayType<int[]> UINT16s = new ArrayType<int[]>() {
		
		@Override
		public int sizeof( int amount ) { return amount * UINT16.sizeof(); }
		
		@Override
		public int[] allocate( int amount ) { return new int[amount]; }
		
		@Override
		public int[] getData( ByteBuffer bb, int amount ) {
			int[] res = new int[amount];
			for( int i = 0; i < amount; i++ )
				res[i] = bb.getShort() & 0xFFFF;
			return res;
		}
		
		@Override
		public String toString() { return "UINT16[]"; }
		
	};
	
	public static final ArrayType<int[]> INT32s = new ArrayType<int[]>() {
		
		@Override
		public int sizeof( int amount ) { return amount * INT32.sizeof(); }
		
		@Override
		public int[] allocate( int amount ) { return new int[amount]; }
		
		@Override
		public int[] getData( ByteBuffer bb, int amount ) {
			int[] res = new int[amount];
			for( int i = 0; i < amount; i++ )
				res[i] = bb.getInt();
			return res;
		}
		
		@Override
		public String toString() { return "INT32[]"; }
		
	};
	
	public static final ArrayType<long[]> UINT32s = new ArrayType<long[]>() {
		
		@Override
		public int sizeof( int amount ) { return amount * UINT32.sizeof(); }
		
		@Override
		public long[] allocate( int amount ) { return new long[amount]; }
		
		@Override
		public long[] getData( ByteBuffer bb, int amount ) {
			long[] res = new long[amount];
			for( int i = 0; i < amount; i++ )
				res[i] = (long)bb.getInt() & 0xFFFFFFFFL;
			return res;
		}
		
		@Override
		public String toString() { return "UINT32[]"; }
		
	};
	
	public static final ArrayType<long[]> INT64s = new ArrayType<long[]>() {
		
		@Override
		public int sizeof( int amount ) { return amount * INT64.sizeof(); }
		
		@Override
		public long[] allocate( int amount ) { return new long[amount]; }
		
		@Override
		public long[] getData( ByteBuffer bb, int amount ) {
			long[] res = new long[amount];
			for( int i = 0; i < amount; i++ )
				res[i] = bb.getLong();
			return res;
		}
		
		@Override
		public String toString() { return "INT64[]"; }
		
	};
	
	public static final ArrayType<float[]> FLOAT32s = new ArrayType<float[]>() {
		
		@Override
		public int sizeof( int amount ) { return amount * FLOAT32.sizeof(); }
		
		@Override
		public float[] allocate( int amount ) { return new float[amount]; }
		
		@Override
		public float[] getData( ByteBuffer bb, int amount ) {
			float[] res = new float[amount];
			for( int i = 0; i < amount; i++ )
				res[i] = bb.get();
			return res;
		}
		
		@Override
		public String toString() { return "FLOAT32[]"; }
		
	};
	
	public static final ArrayType<double[]> FLOAT64s = new ArrayType<double[]>() {
		
		@Override
		public int sizeof( int amount ) { return amount * FLOAT64.sizeof(); }
		
		@Override
		public double[] allocate( int amount ) { return new double[amount]; }
		
		@Override
		public double[] getData( ByteBuffer bb, int amount ) {
			double[] res = new double[amount];
			for( int i = 0; i < amount; i++ )
				res[i] = bb.get();
			return res;
		}
		
		@Override
		public String toString() { return "FLOAT64[]"; }
		
	};
	
}
