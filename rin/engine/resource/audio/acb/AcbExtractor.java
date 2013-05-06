package rin.engine.resource.audio.acb;

import rin.engine.resource.ExtractOptions;
import rin.engine.resource.Resource;
import rin.engine.resource.ResourceExtractor;
import rin.engine.util.binary.BinaryReader;

public class AcbExtractor extends BinaryReader implements ResourceExtractor {

	@Override
	public String getExtensionName() { return "acb"; }
	
	@Override
	public ExtractOptions getDefaultOptions() {
		return new ExtractOptions();
	}
	
	@Override
	public boolean extract( Resource resource, ExtractOptions opts ) {
		load( resource );
		int start = 0;
		int end = 0;
		
		// find the HCA header and the end of the hca data
		while( position() < length() ) {
			if( readChar() == 'H' )
				if( readChar() == 'C' )
					if( readChar() == 'A' ) {
						rewind( 3 );
						start = position();
						/*while( position() < length() ) {
							if( readChar() == '@' )
								if( readChar() == 'U' )
									if( readChar() == 'T' )
										if( readChar() == 'F' ) {
											rewind( 4 );
											end = position();
											break;
										}
						}*/
						if( end == 0 ) end = length();
						break;
					}
		}
		
		// if there is no data between start and end, finish
		if( start == 0 && end == 0 ) return false;
		
		// create resource to store file (stored as .bin)
		Resource hca = resource.getDirectory().createResource( resource.getBaseName() + ".hca", true );
		if( hca == null ) return false;
		
		// write the data to the new resource
		position( start );
		if( !hca.writeBytes( readInt8( end - start ) ) ) return false;
		if( opts.getNotify() ) System.out.println( hca.getName() + " extracted successfully." );
		
		// if the options dictate to delete source file after extraction, do so
		if( opts.getDeleteAfterExtract() ) {
			if( !resource.delete() ) {
				System.out.println( "AcbExtractor#extract(Resource,ExtractOptions): Failed to delete resource." );
				return false;
			}
			if( opts.getNotify() ) System.out.println( resource.getName() + " successfully deleted." );
		}
		
		return true;
	}
}
