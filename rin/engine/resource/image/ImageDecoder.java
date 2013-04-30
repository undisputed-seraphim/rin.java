package rin.engine.resource.image;

import rin.engine.resource.Resource;

public interface ImageDecoder {

	public String getExtensionName();
	public ImageContainer decode( Resource resource, ImageOptions opts );
	
}
