package rin.engine.resource.image;

public enum PixelFormat {

	RGB888		( 3 ),
	RGBA8888	( 4 );
	
	private int s;
	
	public int getStride() {
		return s;
	}
	
	private PixelFormat( int stride ) {
		s = stride;
	}
}
