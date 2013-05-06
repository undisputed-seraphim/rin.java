package rin.engine.resource.image;

public enum PixelFormat {
	G		( 1,  0, -1, -1, -1 ),
	GA		( 2,  0, -1, -1,  1 ),
	RGB		( 3,  0,  1,  2, -1 ),
	BGR		( 3,  2,  1,  0, -1 ),
	BGRA	( 4,  2,  1,  0,  3 ),
	ABGR	( 4,  3,  2,  1,  0 ),
	RGBA	( 4,  0,  1,  2,  3 ),
	ARGB	( 4,  1,  2,  3,  0 );
	
	private int s;
	private int r;
	private int g;
	private int b;
	private int a;
	
	private PixelFormat( int stride, int red, int green, int blue, int alpha ) {
		s = stride;
		r = red;
		g = green;
		b = blue;
		a = alpha;
	}
	
	public int getStride() {
		return s;
	}

	public int r() { return r; }
	public int g() { return g; }
	public int b() { return b; }
	public int a() { return a; }
}
