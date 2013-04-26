package rin.engine.resource.formats.gmo;

import java.nio.ByteBuffer;

import static rin.engine.resource.formats.gmo.GMOSpec.*;
import rin.engine.resource.Resource;
import rin.util.bio.BinaryReader;

public class GMODecoder extends BinaryReader {

	private ByteBuffer buffer;
	
	@Override
	public ByteBuffer getBuffer() {
		return buffer;
	}
	
	private void header() {
		System.out.println( "GMO file: " + length() );
		
		// validate header using MAGIC
		boolean valid = true;
		for( int i = 0; i < MAGIC.length; i++ )
			valid &= readChar() == MAGIC[i];
		if( !valid ) exitWithError( "Not a valid GMO Header." );
	}
	
	private void chunkList() {
		short type = readInt16();
		
		switch( type ) {
		
		case C_ROOT:
			short headerSize = readInt16();
			System.out.println( "header size: " + headerSize );
			advance( headerSize - 4 );
			chunkList();
			break;
			
		case C_LIST:
			for( int i = 0; i < 50; i++ ) {
				short tmp = readInt16();
				System.out.println( tmp + "("+String.format( "0x%04x", tmp)+")" );
			}
			break;
			
		default:
			System.out.println( "unkown chunk type: " + type + " ("+String.format( "0x%04x", type)+")" );
			break;
		}
	}
	public GMODecoder( Resource resource ) {
		buffer = ByteBuffer.wrap( resource.asByteArray() );
		
		header();
		chunkList();
	}
	
}
