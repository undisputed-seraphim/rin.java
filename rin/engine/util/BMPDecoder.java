package rin.engine.util;

import java.nio.ByteBuffer;

import rin.engine.resource.ResourceIdentifier;
import rin.engine.resource.ResourceManager;
import rin.util.bio.BinaryReader;

public class BMPDecoder extends BinaryReader {

	private ByteBuffer data;
	
	@Override
	public ByteBuffer getBuffer() {
		return data;
	}

	public BMPDecoder( ResourceIdentifier resource ) {
		data = ByteBuffer.wrap( resource.asByteArray() );
		//setLittleEndian();
		System.out.println( "bmp file is size " + length() );
		printInt16( 10 );
	}
	
	public static void main( String[] args ) {
		new BMPDecoder( ResourceManager.getPackResource( "rin", "test.bmp" ) );
	}
}
