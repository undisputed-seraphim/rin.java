package rin.engine.resource.formats.cl3;

import rin.engine.resource.Directory;
import rin.engine.resource.ForEach;
import rin.engine.resource.Resource;

public class CL3Extractor {

	public CL3Extractor( final Directory dir ) {
		dir.forEachResource( ".cl3", new ForEach<Resource>() {
			@Override public void each( Resource res ) {
				if( !dir.containsResource( res.getBaseName() + "ism2" ) ) {
					CL3Decoder tmp = new CL3Decoder( res );
					if( tmp.getData() != null ) {
						Resource rtmp = dir.createResource( res.getBaseName() + ".ism2" );
						if( rtmp != null ) {
							rtmp.writeBytes( tmp.getData() );
						}
					}
				}
			}
		});
	}
	
}
