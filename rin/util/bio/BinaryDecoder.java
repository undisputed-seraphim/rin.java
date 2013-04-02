package rin.util.bio;

import java.nio.ByteBuffer;

import rin.util.IO;

public abstract class BinaryDecoder extends BinaryReader {
	
    private ByteBuffer data;
    public ByteBuffer getBuffer() { return this.data; }
    
    public BinaryDecoder( String file ) {
        this.data = ByteBuffer.wrap( IO.file.asByteArray( file ) );
    }

    public abstract void read();
}
