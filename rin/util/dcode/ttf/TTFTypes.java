package rin.util.dcode.ttf;

import java.nio.ByteBuffer;

import rin.util.bio.BIOTypes.Types;

public class TTFTypes {
	public static final Types<Float> FIXED = new Types<Float>() {
		@Override public Float[] allocate( int amount ) { return new Float[amount]; }
		@Override public Float getData( ByteBuffer bb ) {
			return Float.parseFloat( new String( bb.getShort() + "." + bb.getShort() ) );
		}
		@Override public String toString() { return "TTF_FIXED"; }
	};
	
	public static final Types<String> TAG = new Types<String>() {
		@Override public String[] allocate( int amount ) { return new String[amount]; }
		@Override public String getData( ByteBuffer bb ) {
			char c1 = new String( new byte[] { bb.get() } ).charAt( 0 );
			char c2 = new String( new byte[] { bb.get() } ).charAt( 0 );
			char c3 = new String( new byte[] { bb.get() } ).charAt( 0 );
			char c4 = new String( new byte[] { bb.get() } ).charAt( 0 );
			return String.valueOf( c1 ) + String.valueOf( c2 ) + String.valueOf( c3 ) + String.valueOf( c4 );
		}
		@Override public String toString() { return "TTF_TAG"; }
	};
}
