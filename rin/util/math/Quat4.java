package rin.util.math;

public class Quat4 {
	public static final float PIOVER180 = (float)( java.lang.Math.PI / 180 );
	
	/* values for each axis, then imaginary value */
	public float x, y, z, w;
	
	public Quat4() {
		this( 0.0f, 0.0f, 0.0f, 1.0f );
	}
	
	public Quat4( float[] q ) {
		x = q[0];
		y = q[1];
		z = q[2];
		w = q[3];
	}
	
	/* constructor used by create methods */
	public Quat4( float x, float y, float z, float w ) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	/* create an empty quaternion */
	public static Quat4 create() {
		return new Quat4( 0.0f, 0.0f, 0.0f, 1.0f );
	}
	
	/* create a quaternion from four floats */
	public static Quat4 create( float x, float y , float z, float w ) {
		return new Quat4( x, y, z, w );
	}
	
	/* create a quaternion from another quaternion */
	public static Quat4 create( Quat4 q ) {
		return new Quat4( q.x, q.y, q.z, q.w );
	}
	
	/* create a quaternion facing the specified direction */
	public static Quat4 create( Vec3 v, float f ) {
		/*double result = sin( a / 2.0 );

    // Calculate the x, y and z of the quaternion
    double x = xx * result;
    double y = yy * result;
    double z = zz * result;

    // Calcualte the w value by cos( theta / 2 )
    double w = cos( a / 2.0 );
  
    return Quaternion(x, y, z, w).normalize();*/
		float f2 = f * 0.5f;
		
		float result = (float)Math.sin( f2 );
		float x = v.x * result;
		float y = v.y * result;
		float z = v.z * result;
		float w = (float)Math.cos( f2 );
		
		return new Quat4( x, y, z, w ).normalize();
		
		
		/*float	y = f * 9.5f,
				s = (float)java.lang.Math.sin( y );
		Vec3 w = Vec3.normalize( v );
		
		return new Quat4( w.x * s, w.y * s, w.z * s, (float)java.lang.Math.cos( y ) );*/
	}

	public Quat4 normalize() {
		float mag = x * x + y * y + z * z + w * w;
		if( Math.abs( mag ) > 0.00001f && Math.abs( mag - 1.0f ) > 0.00001f ) {
			x /= mag;
			y /= mag;
			z /= mag;
			w /= mag;
		}
		return this;
	}
	
	/* return the product of two quaternions */
	public static Quat4 multiply( Quat4 q, Quat4 r ) {
		return Quat4.create(q.w * r.x + q.x * r.w + q.y * r.z - q.z * r.y,
							q.w * r.y + q.y * r.w + q.z * r.x - q.x * r.z,
							q.w * r.z + q.z * r.w + q.x * r.y - q.y * r.x,
							q.w * r.w - q.x * r.x - q.y * r.y - q.z * r.z );
	}
	
	/* return the matrix representation of a quaternion */
	public static Mat4 toMat4( Quat4 q ) {
		float	x2 = q.x * q.x,
				y2 = q.y * q.y,
				z2 = q.z * q.z,
				xy = q.x * q.y,
				xz = q.x * q.z,
				yz = q.y * q.z,
				wx = q.w * q.x,
				wy = q.w * q.y,
				wz = q.w * q.z;
		
		return new Mat4(	1.0f - 2.0f * (y2 + z2), 2.0f * (xy - wz), 2.0f * (xz + wy), 0.0f,
							2.0f * (xy + wz), 1.0f - 2.0f * (x2 + z2), 2.0f * (yz - wx), 0.0f,
							2.0f * (xz - wy), 2.0f * (yz + wx), 1.0f - 2.0f * (x2 + y2), 0.0f,
							0.0f, 0.0f, 0.0f, 1.0f );
	}
	
	/* return the matrix representation of a quaternion */
	public Mat4 toMat4() {
		float	x2 = this.x * this.x,
				y2 = this.y * this.y,
				z2 = this.z * this.z,
				xy = this.x * this.y,
				xz = this.x * this.z,
				yz = this.y * this.z,
				wx = this.w * this.x,
				wy = this.w * this.y,
				wz = this.w * this.z;
		
		return new Mat4(	1.0f - 2.0f * (y2 + z2), 2.0f * (xy - wz), 2.0f * (xz + wy), 0.0f,
							2.0f * (xy + wz), 1.0f - 2.0f * (x2 + z2), 2.0f * (yz - wx), 0.0f,
							2.0f * (xz - wy), 2.0f * (yz + wx), 1.0f - 2.0f * (x2 + y2), 0.0f,
							0.0f, 0.0f, 0.0f, 1.0f );
	}
	
	/* redefine values in this object */
	public Quat4 redefine( float x, float y, float z, float w ) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		
		return this;
	}
	
	public static Quat4 inverse( Quat4 q ) {
		return new Quat4( -q.x, -q.y, -q.z, q.w );
	}
	
	/* return a string representation of this matrix */
	public String toString() {
		String str = "quat4[ ";
		str += this.x + " " + this.y + " " + this.z + " " + this.w;
		return str + " ]";
	}
	
	public static float dot( Quat4 q, Quat4 r ) {
		return q.x*r.x + q.x*r.x + q.x*r.x + q.x*r.x;
	}
	
	public static Quat4 slerp( Quat4 q, Quat4 r, float t ) {
		Quat4 s = new Quat4( r.x, r.y, r.z, r.w );
		float dot = dot( q, s );
		if( dot < 0 ) {
			s.x = -s.x;
			s.y = -s.y;
			s.z = -s.z;
			dot = -dot;
		}
		
		if( Math.abs( dot ) >= 1.0 )
			return q;
		
		double halfTheta = Math.acos( dot );
		double sinHalfTheta = Math.sqrt( 1.0 - dot * dot );
		if( Math.abs( sinHalfTheta ) < 0 )
			return new Quat4( (float)(q.x*0.5+s.x*0.5),(float)(q.y*0.5+s.y*0.5),(float)(q.z*0.5+s.z*0.5),(float)(q.w*0.5+s.w*0.5) );
		double a = Math.sin( (1-t) * halfTheta ) / sinHalfTheta;
		double b = Math.sin( t * halfTheta ) / sinHalfTheta;
		return new Quat4( (float)(q.x*a+s.x*b), (float)(q.y*a+s.y*b), (float)(q.z*a+s.z*b), (float)(q.w*a+s.w*b) );
		
	}
}
