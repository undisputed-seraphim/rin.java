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
				public T[] allocate( int amount ) { return t.allocate( amount ); }
				public T getData( ByteBuffer bb ) { return t.getData( bb ); }
				public String toString() { return id; }
			};
		}
		public abstract T[] allocate( int amount );
		public abstract T getData( ByteBuffer bb );
		public abstract String toString();
	}
	
	public static final Type<Short> UINT8 = new Type<Short>() {
		public Short[] allocate( int amount ) { return new Short[amount]; }
		public Short getData( ByteBuffer bb ) { return (short)(bb.get() & 0xff); }
		public String toString() { return "UINT8"; }
	};
	
	public static final Type<Byte> INT8 = new Type<Byte>() {
		public Byte[] allocate( int amount ) { return new Byte[amount]; }
		public Byte getData( ByteBuffer bb ) { return bb.get(); }
		public String toString() { return "INT8"; }
	};
	
	public static final Type<Character> CHAR8 = new Type<Character>() {
		public Character[] allocate( int amount ) { return new Character[amount]; }
		public Character getData( ByteBuffer bb ) { return new String( new byte[] { bb.get() } ).charAt( 0 ); }
		public String toString() { return "CHAR8"; }
	};
	
	public static final Type<Integer> UINT16 = new Type<Integer>() {
		public Integer[] allocate( int amount ) { return new Integer[amount]; }
		public Integer getData( ByteBuffer bb ) { return bb.getShort() & 0xffff; }
		public String toString() { return "UINT16"; }
	};
	
	public static final Type<Short> INT16 = new Type<Short>() {
		public Short[] allocate( int amount ) { return new Short[amount]; }
		public Short getData( ByteBuffer bb ) { return bb.getShort(); }
		public String toString() { return "INT16"; }
	};
	
	public static final Type<Long> UINT32 = new Type<Long>() {
		public Long[] allocate( int amount ) { return new Long[amount]; }
		public Long getData( ByteBuffer bb ) { return (long)bb.getInt() & 0xffffffffL; }
		public String toString() { return "UINT32"; }
	};
	
	public static final Type<Integer> INT32 = new Type<Integer>() {
		public Integer[] allocate( int amount ) { return new Integer[amount]; }
		public Integer getData( ByteBuffer bb ) { return bb.getInt(); }
		public String toString() { return "INT32"; }
	};
	
	public static final Type<Short> UBYTE = Type.copy( UINT8, "UBYTE" );
	public static final Type<Byte> BYTE = Type.copy( INT8, "BYTE" );
	
	public static final Type<Character> CHAR = Type.copy( CHAR8, "CHAR" );
	
	public static final Type<Integer> USHORT = Type.copy( UINT16, "USHORT" );
	public static final Type<Short> SHORT = Type.copy( INT16, "SHORT" );
	
	public static final Type<Long> UINT = Type.copy( UINT32, "UINT" );
	public static final Type<Integer> INT = Type.copy( INT32, "INT" );
	
	public static final Type<Float> FLOAT = new Type<Float>() {
		public Float[] allocate( int amount ) { return new Float[amount]; }
		public Float getData( ByteBuffer bb ) { return bb.getFloat(); }
		public String toString() { return "FLOAT"; }
	};
	
	public static final Type<Double> DOUBLE = new Type<Double>() {
		public Double[] allocate( int amount ) { return new Double[amount]; }
		public Double getData( ByteBuffer bb ) { return bb.getDouble(); }
		public String toString() { return "DOUBLE"; }
	};
	
	public static final Type<Long> LONG = new Type<Long>() {
		public Long[] allocate( int amount ) { return new Long[amount]; }
		public Long getData( ByteBuffer bb ) { return bb.getLong(); }
		public String toString() { return "LONG"; }
	};
}
