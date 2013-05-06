package rin.engine.resource.image.png;

import rin.engine.resource.image.ImageOptions;

public class PngOptions extends ImageOptions {
	
	private boolean indexed = false;
	private boolean interlaced = false;
	
	public boolean isIndexed() { return indexed; }
	
	public PngOptions setIndexed( boolean val ) {
		indexed = val;
		return this;
	}
	
	public boolean isInterlaced() { return interlaced; }
	
	public PngOptions setInterlaced( boolean val ) {
		interlaced = val;
		return this;
	}
}
