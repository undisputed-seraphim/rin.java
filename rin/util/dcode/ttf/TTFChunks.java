package rin.util.dcode.ttf;

import rin.util.bio.BIOTypes;
import rin.util.bio.BIOChunks.Chunk;;

public class TTFChunks {
	public static final Chunk HEADER = new Chunk( "header" ) {
		@Override public void define() {
			this.addPart( TTFTypes.FIXED, 1, "version" );
			this.addPart( BIOTypes.USHORT, 1, "numTables" );
			this.addPart( BIOTypes.USHORT, 1, "searchRange" );
			this.addPart( BIOTypes.USHORT, 1, "entrySelector" );
			this.addPart( BIOTypes.USHORT, 1, "rangeShift" );
		}
	};
	
	public static final Chunk DSIG = new Chunk( "dsig" ) {
		@Override public void define() {
			this.addPart( TTFTypes.TAG, 1, "tag" );
			this.addPart( BIOTypes.UINT, 1, "version" );
			this.addPart( BIOTypes.USHORT, 1, "num" );
			this.addPart( BIOTypes.USHORT, 1, "flags" );
		}
	};
}
