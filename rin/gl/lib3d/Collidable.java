package rin.gl.lib3d;

import rin.gl.GL;
import rin.util.math.Vec3;

public class Collidable extends Renderable {
	protected float weight = 10.0f;
	protected float height = 5.0f;
	
	public float getHeight() { return this.height; }
	public Collidable setHeight( float height ) { this.height = height; return this; }
	
	public void applyForce( float f, Vec3 direction ) {
		float dt = GL.getScene().getDt() * 10e-9f;
		System.out.println( "dt in seconds: " + dt );
	}
}
