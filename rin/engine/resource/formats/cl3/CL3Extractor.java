package rin.engine.resource.formats.cl3;

import rin.engine.resource.Directory;
import rin.engine.resource.Resource;

public class CL3Extractor {

	public CL3Extractor( Directory dir ) {
		for( Resource r : dir.getResources( ".cl3" ) ) {
			CL3Decoder tmp = new CL3Decoder( r );
			if( tmp.getData() != null ) {
				Resource rtmp = dir.createResource( r.getBaseName() + ".ism2" );
				if( rtmp != null ) {
					rtmp.writeBytes( tmp.getData() );
				}
			}
		}
	}
	
}
