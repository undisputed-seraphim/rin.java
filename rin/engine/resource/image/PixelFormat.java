package rin.engine.resource.image;

public enum PixelFormat {

	RGB888		( 3 );
	
	private int s;
	
	public int getStride() {
		return s;
	}
	
	private PixelFormat( int stride ) {
		s = stride;
	}
}
