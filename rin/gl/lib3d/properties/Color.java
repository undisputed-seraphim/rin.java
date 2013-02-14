package rin.gl.lib3d.properties;

public class Color {
	public static final float[] WHITE = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
	public static final float[] LIGHT_GRAY = new float[] { 0.9f, 0.9f, 0.9f, 1.0f };
	public static final float[] GRAY = new float[] { 0.6f, 0.6f, 0.6f, 1.0f };
	public static final float[] DARK_GRAY = new float[] { 0.3f, 0.3f, 0.3f, 1.0f };
	
	public static final float[] RED = new float[] { 0.9f, 0.0f, 0.0f, 1.0f };
	public static final float[] DARK_RED = new float[] { 0.6f, 0.0f, 0.0f, 1.0f };
	
	public static final float[] ORANGE = new float[] { 0.9f, 0.6f, 0.0f, 1.0f };
	public static final float[] DARK_ORANGE = new float[] { 0.9f, 0.3f, 0.0f, 1.0f };
	
	public static final float[] GREEN = new float[] { 0.0f, 0.9f, 0.0f, 1.0f };
	public static final float[] DARK_GREEN = new float[] { 0.0f, 0.6f, 0.0f, 1.0f };
	
	public static final float[] BLUE = new float[] { 0.0f, 0.0f, 0.9f, 1.0f };
	public static final float[] DARK_BLUE = new float[] { 0.0f, 0.0f, 0.6f, 1.0f };
	
	public static final float[] YELLOW = new float[] { 0.9f, 0.9f, 0.0f, 1.0f };
	public static final float[] DARK_YELLOW = new float[] { 0.6f, 0.6f, 0.0f, 1.0f };
	
	public static final float[] CYAN = new float[] { 0.0f, 0.9f, 0.9f, 1.0f };
	public static final float[] TURQUOISE = new float[] { 0.0f, 0.6f, 0.6f, 1.0f };
	
	public static final float[] PINK = new float[] { 0.9f, 0.3f, 0.6f, 1.0f };
	public static final float[] MAGENTA = new float[] { 0.9f, 0.0f, 0.9f, 1.0f };
	public static final float[] PURPLE = new float[] { 0.6f, 0.0f, 0.6f, 1.0f };
	
	public static final float[] BLACK = new float[] { 0.0f, 0.0f, 0.0f, 1.0f };
	
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
	
	public float getR() { return this.r; }
	public float getG() { return this.g; }
	public float getB() { return this.b; }
	public float getA() { return this.a; }
	
	public Color setR( float r ) { this.r = r; return this; }
	public Color setG( float g ) { this.g = g; return this; }
	public Color setB( float b ) { this.b = b; return this; }
	public Color setA( float a ) { this.a = a; return this; }

}
