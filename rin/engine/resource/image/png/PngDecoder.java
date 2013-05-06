package rin.engine.resource.image.png;

import rin.engine.resource.Resource;
import rin.engine.resource.image.ImageContainer;
import rin.engine.resource.image.ImageDecoder;
import rin.engine.resource.image.ImageOptions;
import rin.engine.util.binary.BinaryReader;

public class PngDecoder extends BinaryReader implements ImageDecoder {

	@Override
	public String getExtensionName() { return "png"; }

	@Override
	public PngOptions getDefaultOptions() { return new PngOptions(); }
	
	@Override
	public ImageContainer decode( Resource resource, ImageOptions opts ) {
		load( resource );
		setBigEndian();
		
		
		return null;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
	}
	
}
