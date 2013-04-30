package rin.engine.resource.format;

import rin.engine.resource.Resource;

public interface ImageEncoder {
	
	public boolean encode( ImageContainer from, Resource to, ImageOptions options );
	
}
