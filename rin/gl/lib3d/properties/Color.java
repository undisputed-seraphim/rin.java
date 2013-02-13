package rin.gl.lib3d.properties;

public class Color {
	private static float defaultR = 1.0f;
	private static float defaultG = 0.0f;
	private static float defaultB = 0.0f;
	private static float defaultA = 1.0f;
	
	protected float r;
	protected float g;
	protected float b;
	protected float a;
	
	public Color() { this( defaultR, defaultG, defaultB, defaultA ); }
	public Color( float r, float g, float b ) { this( r, g, b, defaultA ); }
	public Color( float r, float g, float b, float a ) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public float[] getColor() { return new float[] { r, g, b, a }; }
	public Color setColor( float r, float g, float b ) { return this.setColor( r, g, b, this.a ); }
	public Color setColor( float r, float g, float b, float a ) { this.r = r; this.g = g; this.b = b; this.a = a; return this; }
	
	public Color setR( float r ) { this.r = r; return this; }
	public Color setG( float g ) { this.g = g; return this; }
	public Color setB( float b ) { this.b = b; return this; }
	public Color setA( float a ) { this.a = a; return this; }

}
