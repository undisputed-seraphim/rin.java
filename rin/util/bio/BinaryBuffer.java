package rin.util.bio;

import java.nio.ByteBuffer;

public class BinaryBuffer extends BinaryReader {

	private ByteBuffer buffer;
	
	public BinaryBuffer( byte[] data ) {
		buffer = ByteBuffer.wrap( data );
	}
	
	@Override
	public ByteBuffer getBuffer() {
		return buffer;
	}
	
}
