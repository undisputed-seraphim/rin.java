package rin.util.bio;

import java.nio.ByteBuffer;

import rin.engine.resource.Resource;

public class BaseBinaryReader extends BinaryReader {
	
	private ByteBuffer buffer;
	
	@Override
	public ByteBuffer getBuffer() {
		return buffer;
	}

	public BaseBinaryReader( Resource resource ) {
		buffer = ByteBuffer.wrap( resource.asByteArray() );
	}
	
}
