package rin.util.dcode.pssg;

import rin.util.bio.BIOFile;

public class PSSGFile extends BIOFile {
	public PSSGFile( String file ) { super( file ); }
	
	@Override public void read() {
		this.addChunk( PSSGChunks.HEADER );
	}
	
	@Override public void write() {}
}
