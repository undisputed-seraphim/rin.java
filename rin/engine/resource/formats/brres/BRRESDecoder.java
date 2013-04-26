package rin.engine.resource.formats.brres;

import rin.engine.resource.Resource;
import rin.util.bio.BaseBinaryReader;

public class BRRESDecoder extends BaseBinaryReader {
	
	private void header() {
		System.out.println( "BRRES file: " + length() );
		printChar( 4 );
		printInt32( 10 );
	}
	
	public BRRESDecoder( Resource resource ) {
		super( resource );
		
		header();
	}
	
}
