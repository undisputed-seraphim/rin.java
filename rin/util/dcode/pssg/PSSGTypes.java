package rin.util.dcode.pssg;

import java.nio.ByteBuffer;

import static rin.util.bio.BIOTypes.*;
import rin.util.bio.BIOTypes.Type;

public class PSSGTypes {
	public static final Type<String[]> PSSGSTRING = new Type<String[]>() {
		@Override public String toString() { return "PSSGSTRING"; }
		@Override public String[] getData( ByteBuffer bb, int amount ) {
			String[] res = new String[ amount ];
			
			for( int i = 0; i < amount; i++ ) {
				long length = INT32.getData( bb, 1 )[0];
				String tmp = "";
			
				for( int j = 0; j < length; j++ )
					tmp += CHAR8.getData( bb, 1 )[0];
			
				res[i] = tmp;
			}
			
			return res;
		}
	};
}
