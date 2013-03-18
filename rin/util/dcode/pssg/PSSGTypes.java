package rin.util.dcode.pssg;

import java.nio.ByteBuffer;

import static rin.util.bio.BIOTypes.*;
import rin.util.bio.BIOTypes.Type;

public class PSSGTypes {
	public static final Type<String> PSSGSTRING = new Type<String>() {
		@Override public String[] allocate( int amount ) { return new String[amount]; }
		@Override public String getData( ByteBuffer bb ) {
			long length = INT32.getData( bb );
			String res = "";
			
			for( int i = 0; i < length; i++ )
				res += CHAR8.getData( bb );
			
			return res;
		}
		@Override public String toString() { return "PSSGSTRING"; }
	};
}
