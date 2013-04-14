package rin.engine.resource.formats.pssg;

import rin.engine.resource.Resource;
import rin.engine.resource.ResourceIdentifier;

public class PSSGResource implements Resource {

	private ResourceIdentifier id;
	private PSSGData data;
	
	public PSSGResource( ResourceIdentifier id ) {
		this.id = id;
	}
	
	@Override
	public PSSGData getData() { return this.data; }
	
	@Override
	public ResourceIdentifier getIdentifier() { return this.id; }
	
}
