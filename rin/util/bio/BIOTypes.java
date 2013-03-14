package rin.util.bio;

import java.nio.ByteBuffer;

public class BIOTypes {
	public static abstract class Types<T> {
		public abstract T[] allocate( int amount );
		public abstract T getData( ByteBuffer bb );
		public abstract String toString();
	}
	
	public static final Types<Byte> BYTE = new Types<Byte>() {
		public Byte[] allocate( int amount ) { return new Byte[amount]; }
		public Byte getData( ByteBuffer bb ) { return bb.get(); }
		public String toString() { return "BYTE"; }
	};
	
	public static final Types<Character> CHAR = new Types<Character>() {
		public Character[] allocate( int amount ) { return new Character[amount]; }
		public Character getData( ByteBuffer bb ) { return new String( new byte[] { bb.get() } ).charAt( 0 ); }
		public String toString() { return "CHAR"; }
	};
	
	public static final Types<Short> SHORT = new Types<Short>() {
		public Short[] allocate( int amount ) { return new Short[amount]; }
		public Short getData( ByteBuffer bb ) { return bb.getShort(); }
		public String toString() { return "SHORT"; }
	};
	
	public static final Types<Integer> UINT32 = new Types<Integer>() {
		public Integer[] allocate( int amount ) { return new Integer[amount]; }
		public Integer getData( ByteBuffer bb ) { return bb.get() << 24 | bb.get() << 16 | bb.get() << 8 | bb.get(); }
		public String toString() { return "UINT32"; }
	};
	
	public static final Types<Integer> INT = new Types<Integer>() {
		public Integer[] allocate( int amount ) { return new Integer[amount]; }
		public Integer getData( ByteBuffer bb ) { return bb.getInt(); }
		public String toString() { return "INT"; }
	};
	
	public static final Types<Float> FLOAT = new Types<Float>() {
		public Float[] allocate( int amount ) { return new Float[amount]; }
		public Float getData( ByteBuffer bb ) { return bb.getFloat(); }
		public String toString() { return "FLOAT"; }
	};
	
	public static final Types<Double> DOUBLE = new Types<Double>() {
		public Double[] allocate( int amount ) { return new Double[amount]; }
		public Double getData( ByteBuffer bb ) { return bb.getDouble(); }
		public String toString() { return "DOUBLE"; }
	};
	
	public static final Types<Long> LONG = new Types<Long>() {
		public Long[] allocate( int amount ) { return new Long[amount]; }
		public Long getData( ByteBuffer bb ) { return bb.getLong(); }
		public String toString() { return "LONG"; }
	};
}
