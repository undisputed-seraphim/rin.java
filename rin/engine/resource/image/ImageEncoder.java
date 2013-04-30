package rin.engine.resource.image;

import rin.engine.resource.Resource;

public interface ImageEncoder {
	
	public String getExtensionName();
	public boolean encode( ImageContainer from, Resource to, ImageOptions options );
	
}
