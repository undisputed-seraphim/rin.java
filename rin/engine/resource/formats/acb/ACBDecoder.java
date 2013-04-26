package rin.engine.resource.formats.acb;

import java.nio.ByteBuffer;

import rin.engine.resource.Resource;
import rin.util.bio.BinaryReader;

public class ACBDecoder extends BinaryReader {

	private ByteBuffer buffer;
	
	@Override
	public ByteBuffer getBuffer() {
		return buffer;
	}
	
	private void header() {
		printChar( 4 );  //@UTF
		int size = readInt32(); //size of remaining file

		printInt32( 153 );
		advance( 2 );
		System.out.println( position() );
		printChar( 30 );
		System.out.println( position() );
		
	}
	
	public ACBDecoder( Resource resource ) {
		buffer = ByteBuffer.wrap( resource.asByteArray() );
		
		System.out.println( "ACB file: " + length() );
		header();
	}
	
}
