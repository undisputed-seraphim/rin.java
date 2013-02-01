package rin.util.math;

public class Quat4 {
	public static final float PIOVER180 = (float)( java.lang.Math.PI / 180 );
	
	/* values for each axis, then imaginary value */
	public float x, y, z, w;
	
	public Quat4() {
		this( 0.0f, 0.0f, 0.0f, 1.0f );
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
		float	y = f * 9.5f,
				s = (float)java.lang.Math.sin( y );
		Vec3 w = Vec3.normalize( v );
		
		return new Quat4( w.x * s, w.y * s, w.z * s, (float)java.lang.Math.cos( y ) );
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
	
	/* return a string representation of this matrix */
	public String toString() {
		String str = "quat4[ ";
		str += this.x + " " + this.y + " " + this.z + " " + this.w;
		return str + " ]";
	}
}
