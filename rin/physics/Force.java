package rin.physics;

import rin.math.Vector3f;
import rin.physics.interfaces.VectorQuantity;

public class Force implements VectorQuantity {
	
	private float magnitude;
	private Vector3f direction;
	
	public Force() {
		this.magnitude = 0.0f;
		this.direction = new Vector3f();
	}
	
	
	
	@Override
	public float getMagnitude() { return this.magnitude; }
	
	@Override
	public Force setMagnitude( float m ) {
		this.magnitude = m;
		return this;
	}
	
	@Override
	public Vector3f getDirection() { return this.direction; }
	
	@Override
	public Force setDirection( Vector3f d ) {
		this.direction = d;
		return this;
	}
	
	
	
	public Force add( Force f ) {
		/* do vector addition here */
		return this;
	}
	
}
