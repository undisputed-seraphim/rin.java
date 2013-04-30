package rin.engine.resource.model;

import rin.engine.resource.Resource;

public interface ModelDecoder {
	
	public String getExtensionName();
	public ModelContainer decode( Resource resource, ModelOptions opts );

}
