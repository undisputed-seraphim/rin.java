package rin.engine.resource.model.brres;

import java.nio.ByteBuffer;

import rin.engine.resource.Resource;
import rin.engine.resource.model.ModelContainer;
import rin.engine.resource.model.ModelDecoder;
import rin.engine.resource.model.ModelOptions;
import static rin.engine.resource.model.brres.BrresSpec.*;
import rin.util.bio.BinaryReader;

public class BrresDecoder extends BinaryReader implements ModelDecoder {

	
	private void header() {
		boolean valid = true;
		for( int i = 0; i < 4; i++ )
			valid &= readChar() == MAGIC[i];
		if( !valid ) exitWithError( "Not a valid BRRES file." );
		
		int byteOrder = readInt16();
		switch( byteOrder ) {
		case 0xFEFF: setBigEndian(); break;
		case 0xFFFE: setLittleEndian(); break;
		}
	}
	
	private ByteBuffer buffer;
	
	@Override
	public ByteBuffer getBuffer() {
		return buffer;
	}
	
	@Override
	public String getExtensionName() { return "brres"; }
	
	@Override
	public ModelContainer decode( Resource resource, ModelOptions opts ) {
		buffer = ByteBuffer.wrap( resource.asByteArray() );
		
		header();
		
		return null;
	}

}
