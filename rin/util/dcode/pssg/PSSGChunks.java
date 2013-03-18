package rin.util.dcode.pssg;

import rin.util.bio.BIOChunks.Chunk;
import static rin.util.bio.BIOTypes.*;

public class PSSGChunks {
	public static final Chunk HEADER = new Chunk( "HEADER" ) {
		@Override public void define( Chunk c ) {
			c.addPart( CHAR, 4, true );
			c.addPart( UINT32, 1, true );
			c.addPart( UINT32, 1, true );
			c.addPart( UINT32, 1, true );
		}
	};
	
	public static final Chunk PSSGDATABASE = new Chunk( "PSSGDATABASE" ) {
		@Override public void define( Chunk c ) {
			
		}
	};
}
