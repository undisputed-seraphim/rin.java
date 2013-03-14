package rin.util.dcode.ttf;

import rin.util.bio.BIOChunks.Chunk;
import rin.util.bio.BIOFile;
import rin.util.bio.BIOTypes;

public class TTFFile extends BIOFile {
	public TTFFile( String file ) { super( file ); }
	
	@Override public void read() {
		this.addChunk( TTFChunks.HEADER, true );
		for( int i = 0; i < this.getUShort( "numTables" ); i++ ) {
			this.addChunk( new Chunk( "table_" + i ) {
				@Override public void define() {
					this.addPart( TTFTypes.TAG, 1, this.id + "_tag" );
					this.addPart( BIOTypes.INT, 1, this.id + "_checksum" );
					this.addPart( BIOTypes.INT, 1, this.id + "_offset" );
					this.addPart( BIOTypes.INT, 1, this.id + "_length" );
				}
			}, true );
		}
	}
	
	@Override public void write() {}
}
