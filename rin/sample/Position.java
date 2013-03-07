package rin.sample;

public class Position implements Transitionable<Position> {

	public float x = 0.0f, y = 0.0f, z = 0.0f;
	
	public Position( float x, float y, float z ) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Position actual() { return this; }
	
	@Override public Position getFrame( Position end, long dt ) {
		float x = this.x * ( 1 - dt ) + end.x * dt;
		float y = this.y * ( 1 - dt ) + end.y * dt;
		float z = this.z * ( 1 - dt ) + end.z * dt;
		return new Position( x, y, z ); 
	}

}
