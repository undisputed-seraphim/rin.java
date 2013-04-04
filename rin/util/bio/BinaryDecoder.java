package rin.util.bio;

import java.nio.ByteBuffer;

import rin.util.IO;

public abstract class BinaryDecoder extends BinaryReader {
	
    private ByteBuffer data;
    
    @Override
    public ByteBuffer getBuffer() { return this.data; }
    
    public BinaryDecoder( String file ) {
        this.data = ByteBuffer.wrap( IO.file.asByteArray( file ) );
    }

    public abstract void read();
    
    public void read( BinaryChunk chunk ) {
    	chunk.setParent( this );
    	chunk.define();
    }
    
}
