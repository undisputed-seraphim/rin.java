package rin.engine.resource.format;

import rin.engine.resource.Resource;

public interface ModelDecoder {
	
	public ModelContainer decode( Resource resource, ModelOptions opts );

}
