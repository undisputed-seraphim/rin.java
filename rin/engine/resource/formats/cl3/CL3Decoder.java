package rin.engine.resource.formats.cl3;

import java.io.File;
import java.nio.ByteBuffer;

import rin.engine.resource.ResourceIdentifier;
import rin.engine.resource.ResourceManager;
import rin.engine.util.FileUtils;
import rin.util.bio.BinaryReader;

public class CL3Decoder extends BinaryReader {

	private ByteBuffer buffer;
	long size;
	int offset = 0;
	
	@Override
	public ByteBuffer getBuffer() {
		return buffer;
	}
	
	private void header() {
		System.out.println( "CL3 file, size: " + length() );
		char[] c;
		
		while( offset == 0 )
			if( readChar() == 'I' )
				if( readChar() == 'S' )
					if( readChar() == 'M' )
						if( readChar() == '2' )
							offset = position() - 4;
		
		// get ISM files filesize
		position( offset );
		printChar( 4 );
		advance( 12 );
		size = readUInt32();
		System.out.println( "ISM starts at: " + offset + " and is size " + size );
	}
	
	public CL3Decoder( ResourceIdentifier resource ) {
		buffer = ByteBuffer.wrap( resource.asByteArray() );
		File res = FileUtils.createFile( resource.getPath(), resource.getBaseName() + ".ism2" );
		header();
		position( offset );
		FileUtils.writeBytes( res, readInt8( (int)size ) );
	}
}
