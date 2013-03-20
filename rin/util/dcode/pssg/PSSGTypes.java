package rin.util.dcode.pssg;

import java.nio.ByteBuffer;

import static rin.util.bio.BIOTypes.*;
import rin.util.bio.BIOTypes.Type;

public class PSSGTypes {
	public static final Type<String> PSSGSTRING = new Type<String>() {
		@Override public String toString() { return "PSSGSTRING"; }
		public int sizeof() { return 1; }
		@Override public String[] allocate( int amount ) { return new String[amount]; }
		@Override public String getData( ByteBuffer bb ) {
			String res = "";
			
			long length = INT32.getData( bb );
			for( int i = 0; i < length; i++ )
				res += CHAR8.getData( bb );
			
			return res;
		}
	};
	
	public static final Type<Float> PSSGFLOAT = new Type<Float>() {
		public String toString() { return "PSSGFLOAT"; }
		public int sizeof() { return Float.SIZE / 8; }
		public Float[] allocate( int amount ) { return new Float[amount]; }
		public Float getData( ByteBuffer bb ) { return bb.getFloat(); }
	};
}
