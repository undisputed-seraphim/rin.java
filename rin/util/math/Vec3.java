package rin.util.math;

public class Vec3 {
	/* values for each axis */
	public float x, y, z;
	
	/* create an empty vector */
	public Vec3() {
		this( 0.0f, 0.0f, 0.0f );
	}
	
	/* constructor used by create methods */
	public Vec3( float x, float y, float z ) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/* create a vector from another vector */
	public Vec3( Vec3 v ) {
		this( v.x, v.y, v.z );
	}

	/* returns the magnitude of the vector */
	public static float magnitude( Vec3 v ) {
		return ( v.x * v.x ) + ( v.y * v.y ) + ( v.z * v.z );
	}
	
	/* returns a normalized vector */
	public static Vec3 normalize( Vec3 v ) {
		float mag = Vec3.magnitude( v );
		if( mag == 0 )
			return new Vec3( 0.0f, 0.0f, 0.0f );
		return new Vec3( v.x / mag, v.y / mag, v.z / mag );
	}
	
	/* returns the sum of two vectors */
	public static Vec3 add( Vec3 v, Vec3 w ) {
		return new Vec3( v.x + w.x, v.y + w.y, v.z + w.z );
	}
	
	/* returns the difference between two vectors */
	public static Vec3 subtract( Vec3 v, Vec3 w ) {
		return new Vec3( v.x - w.x, v.y - w.y, v.z - w.z );
	}
	
	/* returns the cross product of two vectors */
	public static Vec3 cross( Vec3 v, Vec3 w ) {
		return new Vec3( v.y * w.z - v.z * w.y , v.x * w.z - v.z * w.x, v.x * w.y - v.y * w.x );
	}
	
	/* returns a vector scaled by a float */
	public static Vec3 scale( Vec3 v, float f ) {
		return new Vec3( v.x * f, v.y * f, v.z * f );
	}
	
	/* returns a string representation of the vector */
	public String toString() {
		return "vec3[ " + x + " " + y + " " + z + " ]";
	}
}
