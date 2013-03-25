package rin.physics.interfaces;

import rin.math.Vector3f;

public interface VectorQuantity {
	
	public float getMagnitude();
	public VectorQuantity setMagnitude( float m );
	
	public Vector3f getDirection();
	public VectorQuantity setDirection( Vector3f d );
	
}
