package rin.util.bio;

import java.io.File;
import java.nio.ByteBuffer;

import rin.engine.resource.Resource;
import rin.engine.util.FileUtils;

public class BaseBinaryReader extends BinaryReader {
	
	private ByteBuffer buffer;
	
	@Override
	public ByteBuffer getBuffer() {
		return buffer;
	}

	public void load( Resource resource ) {
		buffer = ByteBuffer.wrap( resource.toByteArray() );
	}
	
	public void load( File file ) {
		load( new Resource( file ) );
	}
	
}
