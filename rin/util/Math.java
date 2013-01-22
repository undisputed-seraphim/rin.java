package rin.util;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Math {
	public static class vec3 {
		/* array that holds values for the vector */
		public float x, y, z;
		
		/* create identity vector, vector with three floats, or a vector with a float array */
		public vec3() {
			this( 0, 0, 0 );
		}
		
		public vec3( vec3 v ) {
			this( v.x, v.y, v.z );
		}
		
		public vec3( float[] v ) {
			this( v[0], v[1], v[2] );
		}
		
		public vec3( float x, float y, float z ) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		public float[] get() { return new float[] { this.x, this.y, this.z }; }
		public vec3 set( vec3 v ) { this.x = v.x; this.y = v.y; this.z = v.z; return this; }
		
		public vec3 normalize() { return this.set( vec3.normalize( this ) ); }
		public static vec3 normalize( vec3 v ) {
			float mag = vec3.magnitude( v );
			return mag == 0 ? new vec3( 0.0f, 0.0f, 0.0f ) : new vec3( v.x / mag, v.y / mag, v.z / mag );
		}
		
		public float magnitude() { return vec3.magnitude( this ); }
		public static float magnitude( vec3 v ) { return ( v.x * v.x ) + ( v.y * v.y ) + ( v.z * v.z ); }
		
		public vec3 add( vec3 v ) { return this.add( v.get() ); }
		public vec3 add( float v1, float v2, float v3 ) { this.x += v1; this.y += v2; this.z += v3; return this; }
		public vec3 add( float[] v ) { return this.add( v[0], v[1], v[2] ); }
		public static vec3 add( vec3 v, vec3 w ) { return vec3.add( new float[] { v.x, v.y, v.z }, new float[] { w.x, w.y, w.z } ); }
		public static vec3 add( float[] v, float[] w ) { return new vec3( v[0]+w[0], v[1]+w[1], v[2]+w[2] ); }
		
		public static vec3 subtract( vec3 v, vec3 w ) { return new vec3( v.x - w.x, v.y - w.y, v.z - w.z ); }
		public static vec3 cross( vec3 v, vec3 w ) { return new vec3( v.y * w.z - v.z * w.y , v.x * w.z - v.z * w.x, v.x * w.y - v.y * w.x ); }
		
		public static vec3 scale( vec3 v, float f ) { return new vec3( v.x * f, v.y * f, v.z * f ); }
		
		public vec3 invert() { this.x = -this.x; this.y = -this.y; this.z = -this.z; return this; }
		public static vec3 invert( vec3 v ) { return new vec3( -v.x, -v.y, -v.z ); }
		
		public String toString() { return "vec3[ " + x + " " + y + " " + z + " ]"; }
	}
	
	public static class quat4 {
		private float[] q = new float[4];
		
		public quat4() { this( 0, 0, 0, 1 ); }
		public quat4( float x, float y, float z, float w ) { this( new float[] { x, y, z, w } ); }
		public quat4( float[] q ) { this.q = q; }
		public quat4( vec3 v, float f ) {
			float	y = f * 9.5f,
					s = (float)java.lang.Math.sin( y );
			vec3 w = vec3.normalize( v );
			this.q[0] = w.x * s;
			this.q[1] = w.y * s;
			this.q[2] = w.z * s;
			this.q[3] = (float)java.lang.Math.cos( y );
		}
		
		public static quat4 multiply( quat4 q, quat4 r ) {
			return new quat4(
					q.q[3] * r.q[0] + q.q[0] * r.q[3] + q.q[1] * r.q[2] - q.q[2] * r.q[1],
					q.q[3] * r.q[1] + q.q[1] * r.q[3] + q.q[2] * r.q[0] - q.q[0] * r.q[2],
					q.q[3] * r.q[2] + q.q[2] * r.q[3] + q.q[0] * r.q[1] - q.q[1] * r.q[0],
					q.q[3] * r.q[3] - q.q[0] * r.q[0] - q.q[1] * r.q[1] - q.q[2] * r.q[2] );
		}
		
		public static mat4 mat4( quat4 q ) {
			float	x2 = q.q[0] * q.q[0],
					y2 = q.q[1] * q.q[1],
					z2 = q.q[2] * q.q[2],
					xy = q.q[0] * q.q[1],
					xz = q.q[0] * q.q[2],
					yz = q.q[1] * q.q[2],
					wx = q.q[3] * q.q[0],
					wy = q.q[3] * q.q[1],
					wz = q.q[3] * q.q[2];
			return new mat4(
					1.0f - 2.0f * (y2 + z2), 2.0f * (xy - wz), 2.0f * (xz + wy), 0.0f,
					2.0f * (xy + wz), 1.0f - 2.0f * (x2 + z2), 2.0f * (yz - wx), 0.0f,
					2.0f * (xz - wy), 2.0f * (yz + wx), 1.0f - 2.0f * (x2 + y2), 0.0f,
					0.0f, 0.0f, 0.0f, 1.0f );
		}
	}
	
	public static class mat4 {
		/* array that holds the values of the matrix */
		private float[] m = new float[16];
		
		/* create a default identity matrix, matrix with 16 values, or matrix with float array */
		public mat4() {
			this(	1, 0, 0, 0,
					0, 1, 0, 0,
					0, 0, 1, 0,
					0, 0, 0, 1 );
		}
		public mat4(float m00, float m01, float m02, float m03,
					float m04, float m05, float m06, float m07,
					float m08, float m09, float m10, float m11,
					float m12, float m13, float m14, float m15 ) {
			this( new float[]{	m00, m01, m02, m03,
								m04, m05, m06, m07,
								m08, m09, m10, m11,
								m12, m13, m14, m15 } );
		}
		public mat4( float[] m ) { this.m = m; }
		
		/* staticly grab a new mat4 */
		public static mat4 create() { return new mat4(); }
		
		/* getter / setter for array values */
		public float get( int index ) { return this.m[index]; }
		public void set( int index, float val ) { this.m[index] = val; }
		
		public mat4 flatten() { return mat4.flatten( this ); }
		public static mat4 flatten( mat4 m ) {
			return new mat4(	m.m[0],	m.m[4],	m.m[8],	m.m[12],
					    		m.m[1],	m.m[5],	m.m[9],	m.m[13],
					    		m.m[2],	m.m[6],	m.m[10],m.m[14],
					    		m.m[3],	m.m[7],	m.m[11],m.m[15] );
		}
	
		public static mat4 perspective( float fovy, float aspect, float znear, float zfar ) {
	    	float	ymax = (float)( znear * java.lang.Math.tan( fovy * java.lang.Math.PI / 360.0f ) );
	    	float	ymin = -ymax;
	    	float	xmin = ymin * aspect;
	    	float	xmax = ymax * aspect;
	    	return	mat4.frustum( xmin, xmax, ymin, ymax, znear, zfar );
		}
		
		public static mat4 pickMatrix( float x, float y, float deltaX, float deltaY, IntBuffer viewport ) {
			mat4 trans = mat4.translate( new mat4(), new vec3(
						( viewport.get(2) - 2 * ( x - viewport.get(0) ) ) / deltaX,
						( viewport.get(3) - 2 * ( y - viewport.get(1) ) ) / deltaY, 0 ) );
			mat4 scale = mat4.scale( new mat4(), new vec3( viewport.get(2) / deltaX, viewport.get(3) / deltaY, 1.0f ) );
			return mat4.multiply( trans, scale );
		}
		
		public static mat4 frustum( float left, float right, float bottom, float top, float znear, float zfar ) {
			float	X = 2 * znear / ( right - left );
			float	Y = 2 * znear / ( top - bottom );
		    float	A = ( right + left ) / ( right - left );
			float	B = ( top + bottom ) / ( top - bottom );
		    float	C = -( zfar + znear ) / ( zfar - znear );
			float	D = -2 * zfar * znear / ( zfar - znear );
			return new mat4(	X, 0, A, 0,
			        			0, Y, B, 0,
			        			0, 0, C, D,
			        			0, 0, -1, 0 );
		}
		
		public mat4 translate( vec3 v ) { this.m = mat4.translate( this, v ).m; return this; }
		public static mat4 translate( mat4 m, vec3 v ) {
			mat4 t = new mat4();
			t.set( 3, v.x );
    		t.set( 7, v.y );
			t.set( 11, v.z );
			return mat4.multiply( m, t );
		}
		
		public static mat4 scale( mat4 m, vec3 v ) {
			mat4 s = new mat4();
			s.set( 0, v.x );
			s.set( 5, v.y );
			s.set( 10, v.z );
			s.set( 15, 1 );
			return mat4.multiply( m, s );
		}
		
		public static float mh( float[] v, float[] w ) { return v[0] * w[0] + v[1] * w[1] + v[2] * w[2] + v[3] * w[3]; }
		public mat4 multiply( mat4 m ) { this.m = mat4.multiply( this, m ).m; return this; }
		public static mat4 multiply( mat4 m, mat4 n ) {
			float[] A1 = { m.m[0], m.m[1], m.m[2], m.m[3]  };
			float[] A2 = { m.m[4], m.m[5], m.m[6], m.m[7]  };
			float[] A3 = { m.m[8], m.m[9], m.m[10],m.m[11] };
			float[] A4 = { m.m[12],m.m[13],m.m[14],m.m[15] };
			float[] B1 = { n.m[0], n.m[4], n.m[8], n.m[12] };
			float[] B2 = { n.m[1], n.m[5], n.m[9], n.m[13] };
			float[] B3 = { n.m[2], n.m[6], n.m[10],n.m[14] };
			float[] B4 = { n.m[3], n.m[7], n.m[11],n.m[15] };
			return new mat4(	mat4.mh(A1, B1), mat4.mh(A1, B2), mat4.mh(A1, B3), mat4.mh(A1, B4),
								mat4.mh(A2, B1), mat4.mh(A2, B2), mat4.mh(A2, B3), mat4.mh(A2, B4),
								mat4.mh(A3, B1), mat4.mh(A3, B2), mat4.mh(A3, B3), mat4.mh(A3, B4),
								mat4.mh(A4, B1), mat4.mh(A4, B2), mat4.mh(A4, B3), mat4.mh(A4, B4) );
		}
		
		public static mat4 inverse( mat4 m ) {
			float	a00 = m.m[0], a01 = m.m[1], a02 = m.m[2], a03 = m.m[3],
		            a10 = m.m[4], a11 = m.m[5], a12 = m.m[6], a13 = m.m[7],
		            a20 = m.m[8], a21 = m.m[9], a22 = m.m[10],a23 = m.m[11],
		            a30 = m.m[12],a31 = m.m[13],a32 = m.m[14],a33 = m.m[15],
		            b00 = a00 * a11 - a01 * a10,
		            b01 = a00 * a12 - a02 * a10,
		            b02 = a00 * a13 - a03 * a10,
		            b03 = a01 * a12 - a02 * a11,
		            b04 = a01 * a13 - a03 * a11,
		            b05 = a02 * a13 - a03 * a12,
		            b06 = a20 * a31 - a21 * a30,
		            b07 = a20 * a32 - a22 * a30,
		            b08 = a20 * a33 - a23 * a30,
		            b09 = a21 * a32 - a22 * a31,
		            b10 = a21 * a33 - a23 * a31,
		            b11 = a22 * a33 - a23 * a32,
		            d = (b00 * b11 - b01 * b10 + b02 * b09 + b03 * b08 - b04 * b07 + b05 * b06),
		            invDet;
			invDet = 1 / d;
			mat4 t = new mat4();
			t.m[0]	= ( a11 * b11 - a12 * b10 + a13 * b09 ) * invDet;
			t.m[1]	= ( -a01 * b11 + a02 * b10 - a03 * b09 ) * invDet;
			t.m[2]	= ( a31 * b05 - a32 * b04 + a33 * b03 ) * invDet;
			t.m[3]	= ( -a21 * b05 + a22 * b04 - a23 * b03 ) * invDet;
			t.m[4]	= ( -a10 * b11 + a12 * b08 - a13 * b07 ) * invDet;
			t.m[5]	= ( a00 * b11 - a02 * b08 + a03 * b07 ) * invDet;
			t.m[6]	= ( -a30 * b05 + a32 * b02 - a33 * b01 ) * invDet;
			t.m[7]	= ( a20 * b05 - a22 * b02 + a23 * b01 ) * invDet;
			t.m[8]	= ( a10 * b10 - a11 * b08 + a13 * b06 ) * invDet;
			t.m[9]	= ( -a00 * b10 + a01 * b08 - a03 * b06 ) * invDet;
			t.m[10]	= ( a30 * b04 - a31 * b02 + a33 * b00 ) * invDet;
		    t.m[11] = ( -a20 * b04 + a21 * b02 - a23 * b00 ) * invDet;
		    t.m[12] = ( -a10 * b09 + a11 * b07 - a12 * b06 ) * invDet;
		    t.m[13] = ( a00 * b09 - a01 * b07 + a02 * b06 ) * invDet;
		    t.m[14] = ( -a30 * b03 + a31 * b01 - a32 * b00 ) * invDet;
		    t.m[15] = ( a20 * b03 - a21 * b01 + a22 * b00 ) * invDet;
		    return t;
		}
		public FloatBuffer fb() { return Buffer.toBuffer( this.m ); }
		public static FloatBuffer fb( mat4 m ) { return Buffer.toBuffer( m.m, true ); }
		
		public vec3 getLookAt() { return new vec3( this.m[2], this.m[6], this.m[10] ); }
		public vec3 getUp() { return new vec3( this.m[1], this.m[5], this.m[9] ); }
		public vec3 getPos() { return new vec3( this.m[3], this.m[7], this.m[11] ); }
		
		public String toString() {
			String str = "mat4[ ";
			for( float f : this.m )
				str += f + " ";			
			return str + "]";
		}
	}
}
