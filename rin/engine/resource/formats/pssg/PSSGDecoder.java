package rin.engine.resource.formats.pssg;

import rin.engine.resource.ResourceDecoder;
import rin.engine.resource.ResourceIdentifier;

public class PSSGDecoder implements ResourceDecoder {
	
	@Override
	public PSSGResource decode( ResourceIdentifier resource ) {
		PSSGResource res = new PSSGResource( resource );
		return res;
	}
	
}
