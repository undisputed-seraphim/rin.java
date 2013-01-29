package rin.gl.lib3d;

public class Collidable extends Boundable {
	protected float weight = 10.0f;
	protected float height = 5.0f;
	
	public float getHeight() { return this.height; }
	public Collidable setHeight( float height ) { this.height = height; return this; }
}
