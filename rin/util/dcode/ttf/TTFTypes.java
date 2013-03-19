package rin.util.dcode.ttf;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.sql.Date;
import java.text.DateFormat;

import rin.util.bio.BIOTypes;
import rin.util.bio.BIOTypes.Type;

public class TTFTypes {
	/*public static final Type<Float> FIXED = new Type<Float>() {
		@Override public Float[] allocate( int amount ) { return new Float[amount]; }
		@Override public Float getData( ByteBuffer bb ) { return Float.parseFloat( new String( bb.getShort() + "." + bb.getShort() ) ); }
		@Override public String toString() { return "TTF_FIXED"; }
	};
	
	public static final Type<String> TAG = new Type<String>() {
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
	
	public static final Type<String> LONGDATETIME = new Type<String>() {
		@Override public String[] allocate( int amount ) { return new String[amount]; }
		@Override public String getData( ByteBuffer bb ) {
			DateFormat local = DateFormat.getDateInstance();
			return local.format( new Date( new Timestamp( BIOTypes.LONG.getData( bb ) ).getTime() ) );
		}
		@Override public String toString() { return "LONGDATETIME"; }
	};
	
	public static final Type<String> FLAG = BIOTypes.BIT8.copy( "FLAG" );
	
	public static final Type<Short> FWORD = BIOTypes.SHORT.copy( "FWORD" );
	public static final Type<Short> UBYTE = BIOTypes.UBYTE.copy( "UBYTE" );
	public static final Type<Short> SHORT = BIOTypes.SHORT.copy( "SHORT" );
	public static final Type<Integer> USHORT = BIOTypes.USHORT.copy( "USHORT" );
	public static final Type<Long> ULONG = BIOTypes.UINT32.copy( "ULONG" );*/
}
