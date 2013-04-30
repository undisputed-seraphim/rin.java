package rin.engine.resource.model;

import rin.engine.resource.Resource;

public interface ModelEncoder {
	
	public String getExtensionName();
	public boolean encode( ModelContainer from, Resource to, ModelOptions opts );
	
}
