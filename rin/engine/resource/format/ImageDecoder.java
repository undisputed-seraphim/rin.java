package rin.engine.resource.format;

import rin.engine.resource.format.ImageContainer;
import rin.engine.resource.Resource;

public interface ImageDecoder {

	public abstract ImageContainer decode( Resource resource, ImageOptions opts );
	
}
