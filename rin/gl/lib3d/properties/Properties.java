package rin.gl.lib3d.properties;

import rin.util.math.Vec3;

public class Properties {
	private Transformation t = new Transformation();
	private Color c = new Color();
	
	public Properties() {}
	public Properties( Transformation t ) { this.t = t; }
	public Properties( Transformation t, Color c ) { this.t = t; this.c = c; }
	public Properties( Color c ) { this.c = c; }
	
	public Transformation getTransformation() { return this.t; }
	public Properties setTransformation( Transformation t ) { this.t = t; return this;}
	
	public Vec3 getPosition() { return this.t.position; }
	public Properties setPosition( Vec3 position ) { this.t.position = position; return this; }
	public Properties setPosition( float x, float y, float z ) { this.t.position = new Vec3( x, y, z ); return this; }
	
	public Vec3 getRotation() { return this.t.rotation; }
	public Properties setRotation( Vec3 rotation ) { this.t.rotation = rotation; return this; }
	public Properties setRotation( float x, float y, float z ) { this.t.rotation = new Vec3( x, y, z ); return this; }
	
	public Vec3 getScale() { return this.t.scale; }
	public Properties setScale( Vec3 scale ) { this.t.scale = scale; return this; }
	public Properties setScale( float x, float y, float z ) { this.t.scale = new Vec3( x, y, z ); return this; }
	
	
	public Color getColor() { return this.c; }
	public Properties setColor( Color c ) { this.c = c; return this; }
	public Properties setColor( float r, float g, float b ) { return this.setColor( r, g, b, this.c.a ); }
	public Properties setColor( float r, float g, float b, float a ) { this.c.setColor( r, g, b, a ); return this; }
	
	public float[] getColorAsArray() { return new float[] { this.c.r, this.c.g, this.c.b, this.c.a }; }
	
	public float getR() { return this.c.r; }
	public float getG() { return this.c.g; }
	public float getB() { return this.c.b; }
	public float getA() { return this.c.a; }
}
