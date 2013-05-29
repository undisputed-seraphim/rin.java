package rin.util.math;

public class Vec3 {
	public static final Vec3 X_AXIS = new Vec3( 1.0f, 0.0f, 0.0f );
	public static final Vec3 Y_AXIS = new Vec3( 0.0f, 1.0f, 0.0f );
	public static final Vec3 Z_AXIS = new Vec3( 0.0f, 0.0f, 1.0f );
	
	/* values for each axis */
	public float x, y, z;
	
	/* create an empty vector */
	public Vec3() {
		this( 0.0f, 0.0f, 0.0f );
	}
	
	public Vec3( float[] v ) {
		x = v[0];
		y = v[1];
		z = v[2];
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
	
	/* redefine the values within without creating a new object */
	public Vec3 redefine( float x, float y, float z ) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	public Vec3 redefine( Vec3 w ) {
		x = w.x;
		y = w.y;
		z = w.z;
		return this;
	}
	
	public Vec3 zero() {
		x = 0;
		y = 0;
		z = 0;
		return this;
	}
	
	public float get( int index ) {
		if( index == 0 ) return x;
		if( index == 1 ) return y;
		if( index == 2 ) return z;
		return 0.0f;
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
	
	public static float dot( Vec3 v, Vec3 w ) { return v.x * w.x + v.y * w.y + v.z * w.z; }
	
	/* translate towards or away from matrix m's rotation */
	public static Vec3 step( Vec3 v, Mat4 m, float d ) {
		return new Vec3( v.x += m.m[8] * d, v.y += m.m[9] * d, v.z += m.m[10] * d );
	}
	
	/* translate to the left or right of matrix m's rotation */
	public static Vec3 strafe( Vec3 v, Mat4 m, float d ) {
		return new Vec3( v.x += m.m[0] * d, v.y += m.m[1] * d, v.z += m.m[2] * d );
	}
	
	/* translate up or down in relation to matrix m's rotation */
	public static Vec3 elevate( Vec3 v, Mat4 m, float d ) {
		return new Vec3( v.x += m.m[4] * d, v.y += m.m[5] * d, v.z += m.m[6] * d );
	}
	
	/* returns the sum of two vectors */
	public static Vec3 add( Vec3 v, Vec3 w ) {
		return new Vec3( v.x + w.x, v.y + w.y, v.z + w.z );
	}
	
	public Vec3 add( float X, float Y, float Z ) {
		x += X;
		y += Y;
		z += Z;
		return this;
	}
	
	public Vec3 add( Vec3 w ) {
		x += w.x;
		y += w.y;
		z += w.z;
		return this;
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
	
	public static Vec3 multiply( Vec3 v, Mat4 m ) {
		Vec3 res = new Vec3();
		for( int i = 0; i < 4; i++ ) {
			for( int j = 0; j < 4; j++ ) {
				if( i == 0 ) {
					res.x += m.m[i*4+j] * v.get( j );
				} else if( i == 1 ) {
					res.y += m.m[i*4+j] * v.get( j );
				} else if( i == 2 ) {
					res.z += m.m[i*4+j] * v.get( j );
				}
			}
		}
		return res;
	}
	
	public static Vec3 inverse( Vec3 v ) { return new Vec3( -v.x, -v.y, -v.z ); }
	
	public static float distance( Vec3 v, Vec3 w ) {
		float x = v.x - w.x;
		float y = v.y - w.y;
		float z = v.z - w.z;
		return (float)java.lang.Math.sqrt( x * x + y * y + z * z );
	}
	
	/* returns a string representation of the vector */
	public String toString() {
		return "vec3[ " + x + " " + y + " " + z + " ]";
	}
	
	public static Vec3 lerp( Vec3 v, Vec3 w, float dt ) {
		return new Vec3( v.x*(1-dt) + w.x*dt, v.y*(1-dt) + w.y*dt, v.z*(1-dt) + w.z*dt );
	}
	
	public static Vec3 rotate( Vec3 v, Quat4 q ) {
		Quat4 res = Quat4.multiply( Quat4.multiply( q, new Quat4( v.x, v.y, v.z, 0 ) ), Quat4.inverse( q ) );
		return new Vec3( res.x, res.y, res.z );
	}
	
	public Vec3 rotate( Quat4 r ) {
		Quat4.multiplyInto( r.x, r.y, r.z, r.w, x, y, z, 0, r.b );
		Quat4.multiplyInto( r.b[0], r.b[1], r.b[2], r.b[3], -r.x, -r.y, -r.z, r.w, r.b );
		x = r.b[0];
		y = r.b[1];
		z = r.b[2];
		return this;
	}
}
