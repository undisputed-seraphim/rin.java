package rin.util.math;

public class Quat4 {
	public static final float PIOVER180 = (float)( java.lang.Math.PI / 180.0 );
	public static final float PIUNDER180 = (float)( 180.0 / java.lang.Math.PI );
	
	public static final float degreeToRadian( float deg ) { return deg * PIOVER180; }
	public static final float radianToDegree( float rad ) { return rad * PIUNDER180; }
	
	// buffer for computations
	protected float[] b = new float[4];
	
	/* values for each axis, then imaginary value */
	public float x, y, z, w;

	public Quat4() {
		identity();
	}

	public Quat4( float[] r ) {
		redefine( r );
	}

	/* constructor used by create methods */
	public Quat4( float x, float y, float z, float w ) {
		redefine( x, y, z, w );
	}
	
	public Quat4 identity() {
		x = 0;
		y = 0;
		z = 0;
		w = 1;
		return this;
	}
	
	public Quat4 redefine( float x, float y, float z, float w ) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		return this;
	}
	
	public Quat4 redefine( float[] r ) {
		x = r[0];
		y = r[1];
		z = r[2];
		w = r[3];
		return this;
	}
	
	public Quat4 redefine( Quat4 r ) {
		x = r.x;
		y = r.y;
		z = r.z;
		w = r.w;
		return this;
	}
	
	protected Quat4 applyBuffer() {
		x = b[0];
		y = b[1];
		z = b[2];
		w = b[3];
		return this;
	}
	
	public static float[] orientRadInto( float xaxis, float yaxis, float zaxis, float rad, float[] $r ) {
		float f2 = rad * 0.5f;
		float s = (float)Math.sin( f2 );
		
		$r[0] = xaxis * s;
		$r[1] = yaxis * s;
		$r[2] = zaxis * s;
		$r[3] = (float)Math.cos( f2 );

		return normalize( $r );
	}
	
	public static float[] orientDegInto( float xaxis, float yaxis, float zaxis, float deg, float[] $r ) {
		return orientRadInto( xaxis, yaxis, zaxis, deg * PIOVER180, $r );
	}
	
	public static Quat4 orientRadInto( float xaxis, float yaxis, float zaxis, float rad, Quat4 $r ) {
		float f2 = rad * 0.5f;
		float s = (float)Math.sin( f2 );
		
		$r.x = xaxis * s;
		$r.y = yaxis * s;
		$r.z = zaxis * s;
		$r.w = (float)Math.cos( f2 );

		return $r.normalize();
	}
	
	public static Quat4 orientDegInto( float xaxis, float yaxis, float zaxis, float deg, Quat4 $r ) {
		return orientRadInto( xaxis, yaxis, zaxis, deg * PIOVER180, $r );
	}
	
	public static Quat4 orientDegInto( Vec3 axis, float deg, Quat4 $r ) {
		return orientDegInto( axis.x, axis.y, axis.z, deg, $r );
	}
	
	public static Quat4 orientRadInto( Vec3 axis, float rad, Quat4 $r ) {
		return orientRadInto( axis.x, axis.y, axis.z, rad, $r );
	}
	
	public Quat4 orient( float xaxis, float yaxis, float zaxis, float deg ) {
		return orientDegInto( xaxis, yaxis, zaxis, deg, this );
	}
	
	public Quat4 orient( Vec3 axis, float deg ) {
		return orientDegInto( axis, deg, this );
	}
	
	public Quat4 applyOrientationDeg( Vec3 axis, float deg ) {
		orientDegInto( axis.x, axis.y, axis.z, deg, b );
		return multiplyInto( x, y, z, w, b[0], b[1], b[2], b[3], this );
	}
	
	public Quat4 applyOrientationRad( Vec3 axis, float rad ) {
		orientRadInto( axis.x, axis.y, axis.z, rad, b );
		return multiplyInto( x, y, z, w, b[0], b[1], b[2], b[3], this );
	}
	
	public Quat4 applyOrientationDeg( float xaxis, float yaxis, float zaxis, float deg ) {
		orientDegInto( xaxis, yaxis, zaxis, deg, b );
		return multiplyInto( x, y, z, w, b[0], b[1], b[2], b[3], this );
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
		/*float f2 = f * 0.5f;

		float result = (float)Math.sin( f2 );
		float x = v.x * result;
		float y = v.y * result;
		float z = v.z * result;
		float w = (float)Math.cos( f2 );

		return new Quat4( x, y, z, w ).normalize();*/
		return orientDegInto( v, f, new Quat4() );
	}

	public static float[] normalize( float[] r ) {
		float mag = r[0] * r[0] + r[1] * r[1] + r[2] * r[2] + r[3] * r[3];
		if( Math.abs( mag ) > 0.001f && Math.abs( mag - 1.0f ) > 0.001f ) {
			float m = 1.0f / mag;
			r[0] = r[0] * m;
			r[1] = r[1] * m;
			r[2] = r[2] * m;
			r[3] = r[3] * m;
		}
		
		return r;
	}
	
	public Quat4 normalizeInto( Quat4 $r ) {
		float mag = $r.x * $r.x + $r.y * $r.y + $r.z * $r.z + $r.w * $r.w;
		if( Math.abs( mag ) > 0.001f && Math.abs( mag - 1.0f ) > 0.001f ) {
			float m = 1.0f / mag;
			$r.x = x * m;
			$r.y = y * m;
			$r.z = z * m;
			$r.w = w * m;
		}
		return $r;
	}
	
	public Quat4 normalize() {
		return normalizeInto( this );
	}
	
	public static float[] multiplyInto( float qx, float qy, float qz, float qw, float rx, float ry, float rz, float rw, float[] $r ) {
		$r[0] = qw * rx + qx * rw + qy * rz - qz * ry;
		$r[1] = qw * ry + qy * rw + qz * rx - qx * rz;
		$r[2] = qw * rz + qz * rw + qx * ry - qy * rx;
		$r[3] = qw * rw - qx * rx - qy * ry - qz * rz;
		return $r;
	}
	
	public static Quat4 multiplyInto( float qx, float qy, float qz, float qw, float rx, float ry, float rz, float rw, Quat4 $r ) {
		$r.x = qw * rx + qx * rw + qy * rz - qz * ry;
		$r.y = qw * ry + qy * rw + qz * rx - qx * rz;
		$r.z = qw * rz + qz * rw + qx * ry - qy * rx;
		$r.w = qw * rw - qx * rx - qy * ry - qz * rz;
		return $r;
	}
	
	public static Quat4 multiplyInto( Quat4 q, Quat4 r, Quat4 $r ) {
		multiplyInto( q.x, q.y, q.z, q.w, r.x, r.y, r.z, r.w, $r.b );
		return $r.applyBuffer();
	}
	
	/* return the product of two quaternions */
	public static Quat4 multiply( Quat4 q, Quat4 r ) {
		return multiplyInto( q.x, q.y, q.z, q.w, r.x, r.y, r.z, r.w, new Quat4() );
	}
	
	public Quat4 multiply( Quat4 r ) {
		multiplyInto( x, y, z, w, r.x, r.y, r.z, r.w, b );
		return applyBuffer();
	}

	public Mat4 intoMat4( Mat4 $r ) {
		float	x2 = x * x,
				y2 = y * y,
				z2 = z * z,
				xy = x * y,
				xz = x * z,
				yz = y * z,
				wx = w * x,
				wy = w * y,
				wz = w * z;
		$r.m[ 0] = 1.0f - 2.0f * (y2 + z2); $r.m[ 1] = 2.0f * (xy - wz); $r.m[ 2] = 2.0f * (xz + wy); $r.m[ 3] = 0.0f;
		$r.m[ 4] = 2.0f * (xy + wz); $r.m[ 5] = 1.0f - 2.0f * (x2 + z2); $r.m[ 6] = 2.0f * (yz - wx); $r.m[ 7] = 0.0f;
		$r.m[ 8] = 2.0f * (xz - wy); $r.m[ 9] = 2.0f * (yz + wx); $r.m[10] = 1.0f - 2.0f * (x2 + y2); $r.m[11] = 0.0f;
		$r.m[12] = 0.0f; $r.m[13] = 0.0f; $r.m[14] = 0.0f; $r.m[15] = 1.0f;
		return $r;
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
		return intoMat4( new Mat4() );
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
		float cosTheta = 0.0f;
		float sinTheta = 0.0f;
		float beta = 0.0f;
		float[] res = new float[4];

		res[0] = r.x;
		res[1] = r.y;
		res[2] = r.z;
		res[3] = r.w;

		cosTheta = q.x * r.x + q.y * r.y + q.z * r.z + q.w * r.w;
		if( cosTheta < 0.0f ) {
			res[0] = -res[0];
			res[1] = -res[1];
			res[2] = -res[2];
			res[3] = -res[3];
			cosTheta = -cosTheta;
		}

		beta = 1.0f - t;
		if( 1.0f - cosTheta > 0.001f ) {
			cosTheta = (float)Math.acos( cosTheta );
			sinTheta = 1.0f / (float)Math.sin( cosTheta );
			beta = (float)Math.sin( cosTheta * beta ) * sinTheta;
			t = (float)Math.sin( cosTheta * t ) * sinTheta;
		}

		return new Quat4( beta * q.x + t * res[0], beta * q.y + t * res[1], beta * q.z + t * res[2], beta * q.w + t * res[3] );
	}
}