package rin.engine.resource.format;

import rin.engine.resource.Resource;

public interface ModelEncoder {
	
	public abstract boolean encode( ModelContainer from, Resource to, ModelOptions opts );
	
}
