package rin.math;

public class Vector3f {

	public float x;
	public float y;
	public float z;
	
	public Vector3f() {
		this.x = 0.0f;
		this.y = 0.0f;
		this.z = 0.0f;
	}
	
	public Vector3f( float x, float y, float z ) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	
	
	public Vector3f add( Vector3f v ) {
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
		
		return this;
	}
	
	
	
	public static Vector3f add( Vector3f v, Vector3f w ) {
		float x = v.x + w.x;
		float y = v.y + w.y;
		float z = v.z + w.z;
		
		return new Vector3f( x, y, z );
	}
	
}