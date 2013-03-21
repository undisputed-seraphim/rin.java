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
	protected static abstract class PrimitiveType {
		public abstract String toString();
	}
	
	public static abstract class PrimitiveByte extends PrimitiveType {
		public byte[] allocate( int amount ) { return new byte[amount]; }
		public int sizeof() { return 1; }
		public abstract byte getData( ByteBuffer bb );
	}
	
	public static abstract class PrimitiveChar extends PrimitiveType {
		public char[] allocate( int amount ) { return new char[amount]; }
		public int sizeof() { return 1; }
		public abstract char getData( ByteBuffer bb );
	}
	
	public static abstract class PrimitiveShort extends PrimitiveType {
		public short[] allocate( int amount ) { return new short[amount]; }
		public int sizeof() { return 2; }
		public abstract short getData( ByteBuffer bb );
	}
	
	public static abstract class PrimitiveInt extends PrimitiveType {
		public int[] allocate( int amount ) { return new int[amount]; }
		public int sizeof() { return 4; }
		public abstract int getData( ByteBuffer bb );
	}
	
	public static abstract class PrimitiveLong extends PrimitiveType {
		public long[] allocate( int amount ) { return new long[amount]; }
		public int sizeof() { return 8; }
		public abstract long getData( ByteBuffer bb );
	}
	
	public static abstract class PrimitiveFloat extends PrimitiveType {
		public float[] allocate( int amount ) { return new float[amount]; }
		public int sizeof() { return 4; }
		public abstract float getData( ByteBuffer bb );
	}
	
	public static abstract class PrimitiveDouble extends PrimitiveType {
		public double[] allocate( int amount ) { return new double[amount]; }
		public int sizeof() { return 4; }
		public abstract double getData( ByteBuffer bb );
	}
	
	/* ------------------ default primitive types ----------------- */
	
	public static final PrimitiveShort pUINT8 = new PrimitiveShort() {
		public String toString() { return "UINT8"; }
		public short getData( ByteBuffer bb ) { return (short)(bb.get() & 0xFF); }
	};
	
	public static final Type<Short> UINT8 = new Type<Short>() {
		public String toString() { return "UINT8"; }
		public int sizeof() { return 1; }
		public Short[] allocate( int amount ) { return new Short[amount]; }
		public Short getData( ByteBuffer bb ) { return (short)(bb.get() & 0xFF); }
	};
	
	public static final PrimitiveByte INT8 = new PrimitiveByte() {
		public String toString() { return "INT8"; }
		public byte getData( ByteBuffer bb ) { return bb.get(); }
	};
	
	public static final PrimitiveChar CHAR8 = new PrimitiveChar() {
		public String toString() { return "CHAR8"; }
		public char getData( ByteBuffer bb ) { return new String( new byte[] { bb.get() } ).charAt( 0 ); }
	};
	
	public static final Type<Character> CHAR = new Type<Character>() {
		public String toString() { return "CHAR"; }
		public int sizeof() { return 1; }
		public Character[] allocate( int amount ) { return new Character[amount]; }
		public Character getData( ByteBuffer bb ) { return new String( new byte[] { bb.get() } ).charAt( 0 ); }
	};
	
	public static final PrimitiveInt UINT16 = new PrimitiveInt() {
		public String toString() { return "UINT16"; }
		public int getData( ByteBuffer bb ) { return bb.getShort() & 0xFFFF; }
	};
	
	public static final PrimitiveShort INT16 = new PrimitiveShort() {
		public String toString() { return "INT16"; }
		public short getData( ByteBuffer bb ) { return bb.getShort(); }
	};
	
	public static final PrimitiveLong pUINT32 = new PrimitiveLong() {
		public String toString() { return "UINT32"; }
		public long getData( ByteBuffer bb ) { return (long)bb.getInt() & 0xFFFFFFFFL; }
	};
	
	public static final Type<Long> UINT32 = new Type<Long>() {
		public String toString() { return "UINT32"; }
		public int sizeof() { return 4; }
		public Long[] allocate( int amount ) { return new Long[amount]; }
		public Long getData( ByteBuffer bb ) { return (long)bb.getInt() & 0xFFFFFFFFL; }
	};
	
	public static final PrimitiveInt INT32 = new PrimitiveInt() {
		public String toString() { return "INT32"; }
		public int getData( ByteBuffer bb ) { return bb.getInt(); }
	};
	
	public static final PrimitiveLong INT64 = new PrimitiveLong() {
		public String toString() { return "INT64"; }
		public long getData( ByteBuffer bb ) { return bb.getLong(); }
	};
	
	public static final PrimitiveFloat FLOAT = new PrimitiveFloat() {
		public String toString() { return "FLOAT"; }
		public float getData( ByteBuffer bb ) { return bb.getFloat(); }
	};
	
	public static final PrimitiveDouble DOUBLE = new PrimitiveDouble() {
		public String toString() { return "DOBLE"; }
		public double getData( ByteBuffer bb ) { return bb.getDouble(); }
	};
	
	/*public static final Type<short[]> UINT8 = new Type<short[]>() {
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
	};*/
	
	public static abstract class Type<T> {
		
		public abstract String toString();
		public abstract int sizeof();
		public abstract T[] allocate( int amount );
		public abstract T getData( ByteBuffer bb );
		
		public Type<T> copy() { return this.copy( this.toString() + "_COPY" ); }
		public Type<T> copy( final String id ) {
			final Type<T> t = this;
			return new Type<T>() {
				@Override public String toString() { return id; }
				public int sizeof() { return t.sizeof(); }
				@Override public T[] allocate( int amount ) { return t.allocate( amount ); }
				@Override public T getData( ByteBuffer bb ) { return t.getData( bb ); }
			};
		}
	}
	
	/* --------------- default object types --------------- */
	
	public static final Type<String> BIT8 = new Type<String>() {
		public String toString() { return "BIT8"; }
		public int sizeof() { return 1; }
		public String[] allocate( int amount ) { return new String[amount]; }
		public String getData( ByteBuffer bb ) {
			return String.format( "%08d", Integer.parseInt( Integer.toBinaryString( bb.get() ) ) );
		}
	};
	
	public static final Type<String> HEX8 = new Type<String>() {
		public String toString() { return "HEX8"; }
		public int sizeof() { return 1; }
		public String[] allocate( int amount ) { return new String[amount]; }
		public String getData( ByteBuffer bb ) {
			return String.format( "%08x", Integer.parseInt( Integer.toBinaryString( bb.get() ) ) );
		}
	};
}
