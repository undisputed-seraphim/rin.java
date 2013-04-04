package rin.util.bio;

import java.nio.ByteBuffer;

public abstract class BinaryChunk extends BinaryReader {
	
	private BinaryReader parent;
	public void setParent( BinaryReader br ) { this.parent = br; }
	
	@Override
	public ByteBuffer getBuffer() { return this.parent.getBuffer(); }

    public abstract void define();

}
