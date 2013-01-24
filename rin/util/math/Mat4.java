package rin.util.math;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import rin.util.Buffer;
import rin.util.math.Vec3;

public class Mat4 {
	/* array that holds the values of the matrix */
	public float[] m = new float[16];
	
	/* create an identity matrix */
	public Mat4() {
		this(	1.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 1.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 1.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 1.0f );
	}
	
	/* create a matrix from 16 floats */
	public Mat4(	float m00, float m01, float m02, float m03,
					float m04, float m05, float m06, float m07,
					float m08, float m09, float m10, float m11,
					float m12, float m13, float m14, float m15 ) {
		
		this.m = new float[]{	m00, m01, m02, m03,
								m04, m05, m06, m07,
								m08, m09, m10, m11,
								m12, m13, m14, m15 };
	}
	
	/* create a matrix from another matrix */
	public Mat4( Mat4 m ) {
		this(	m.m[ 0], m.m[ 1], m.m[ 2], m.m[ 3],
				m.m[ 4], m.m[ 5], m.m[ 6], m.m[ 7],
				m.m[ 8], m.m[ 9], m.m[10], m.m[11],
				m.m[12], m.m[13], m.m[14], m.m[15] );
	}
	
	/* return the matrix flattened (column/row swap) */
	public static Mat4 flatten( Mat4 m ) {
		return new Mat4(	m.m[ 0], m.m[ 4], m.m[ 8], m.m[12],
				    		m.m[ 1], m.m[ 5], m.m[ 9], m.m[13],
				    		m.m[ 2], m.m[ 6], m.m[10], m.m[14],
				    		m.m[ 3], m.m[ 7], m.m[11], m.m[15] );
	}

	/* return a matrix dealing with perspective */
	public static Mat4 frustum( float left, float right, float bottom, float top, float znear, float zfar ) {
		float	X = 2 * znear / ( right - left );
		float	Y = 2 * znear / ( top - bottom );
	    float	A = ( right + left ) / ( right - left );
		float	B = ( top + bottom ) / ( top - bottom );
	    float	C = -( zfar + znear ) / ( zfar - znear );
		float	D = -2 * zfar * znear / ( zfar - znear );
		
		return new Mat4(	X, 0,  A, 0,
		        			0, Y,  B, 0,
		        			0, 0,  C, D,
		        			0, 0, -1, 0 );
	}
	
	/* using frustum, return a matrix that tells how the world is viewed */
	public static Mat4 perspective( float fovy, float aspect, float znear, float zfar ) {
    	float	ymax = (float)( znear * java.lang.Math.tan( fovy * java.lang.Math.PI / 360.0f ) );
    	float	ymin = -ymax;
    	float	xmin = ymin * aspect;
    	float	xmax = ymax * aspect;
    	
    	return	Mat4.frustum( xmin, xmax, ymin, ymax, znear, zfar );
	}
	
	public static Vec3 multVec4( Mat4 mat, Vec3 v ) {
		float[] res = new float[4];
		for( int i = 0; i < 4; i++ )
			res[i] = v.x * mat.m[0*4+1] +
					 v.y * mat.m[1*4+i] +
					 v.z * mat.m[2*4+i] + mat.m[3*4+1] ;
		return new Vec3( res[0], res[1], res[2] );
	}
	
	public static Mat4 mult( Mat4 a, Mat4 b ) {
		float[] mat = new float[16];
		for( int i = 0; i < 4; i++ )
			for( int j = 0; j < 4; j++ ) {
				mat[ i*4+j ] =
						a.m[i*4+0] * b.m[0*4+j] +
						a.m[i*4+1] * b.m[1*4+j] +
						a.m[i*4+2] * b.m[2*4+j] +
						a.m[i*4+3] * b.m[3*4+j] ;
			}
		return new Mat4(mat[ 0], mat[ 1], mat[ 2], mat[ 3], mat[ 4], mat[ 5], mat[ 6], mat[ 7],
						mat[ 8], mat[ 9], mat[10], mat[11], mat[12], mat[13], mat[14], mat[15] );
	}
	
	public static float[] unProject( int x, int y, int z, Mat4 mod, Mat4 cam, IntBuffer viewport ) {
		Mat4 mat;
		float[] in = new float[4];
		float[] out = new float[4];
		
		mat = Mat4.mult( mod, cam );
		
		in[0] = x;
		in[1] = y;
		in[2] = z;
		in[3] = 1.0f;
		
		in[0] = ( in[0] - viewport.get( 0 ) ) / viewport.get( 2 );
	    in[1] = ( in[1] - viewport.get( 1 ) ) / viewport.get( 3 );

	    in[0] = in[0] * 2 - 1;
	    in[1] = in[1] * 2 - 1;
	    in[2] = in[2] * 2 - 1;
		
	   // out = Mat4.multVec4( mat, in );
	    System.out.println( Buffer.toString( out ) );
	    if( out[3] == 0 )
	    	return new float[]{ 0.0f, 0.0f, 0.0f, 0.0f };
	    
	    out[0] /= out[3];
	    out[1] /= out[3];
	    out[2] /= out[3];
	    
	    return out;
	}
	
	/*(public static mat4 pickMatrix( float x, float y, float deltaX, float deltaY, IntBuffer viewport ) {
		mat4 trans = mat4.translate( new mat4(), new vec3(
					( viewport.get(2) - 2 * ( x - viewport.get(0) ) ) / deltaX,
					( viewport.get(3) - 2 * ( y - viewport.get(1) ) ) / deltaY, 0 ) );
		mat4 scale = mat4.scale( new mat4(), new vec3( viewport.get(2) / deltaX, viewport.get(3) / deltaY, 1.0f ) );
		return mat4.multiply( trans, scale );
	}*/
	
	/* returns a matrix used to translate other matrices */
	public static Mat4 translate( Mat4 m, Vec3 v ) {
		Mat4 t = new Mat4();
		t.m[ 3] = v.x;
		t.m[ 7] = v.y;
		t.m[11] = v.z;
		t.m[15] = 1.0f;
		
		return Mat4.multiply( m, t );
	}
	
	public static Vec3 transform( Mat4 m, Vec3 v ) {
		float x = v.x * m.m[0] + v.y * m.m[4] + v.z * m.m[8] + m.m[12];
		float y = v.x * m.m[1] + v.y * m.m[5] + v.z * m.m[9] + m.m[13];
		float z = v.x * m.m[2] + v.y * m.m[6] + v.z * m.m[10] + m.m[14];
		return new Vec3( x, y, z );
	}
	
	/* returns a matrix used to scale other matrices */
	public static Mat4 scale( Mat4 m, Vec3 v ) {
		Mat4 s = new Mat4();
		s.m[ 0] = v.x;
		s.m[ 5] = v.y;
		s.m[10] = v.z;
		s.m[15] = 1.0f;
		
		return Mat4.multiply( m, s );
	}
	
	/* returns the product of two matrices */
	public static float mh( float[] v, float[] w ) { return v[0] * w[0] + v[1] * w[1] + v[2] * w[2] + v[3] * w[3]; }
	public static Mat4 multiply( Mat4 m, Mat4 n ) {
		float[] A1 = { m.m[0], m.m[1], m.m[2], m.m[3]  };
		float[] A2 = { m.m[4], m.m[5], m.m[6], m.m[7]  };
		float[] A3 = { m.m[8], m.m[9], m.m[10],m.m[11] };
		float[] A4 = { m.m[12],m.m[13],m.m[14],m.m[15] };
		float[] B1 = { n.m[0], n.m[4], n.m[8], n.m[12] };
		float[] B2 = { n.m[1], n.m[5], n.m[9], n.m[13] };
		float[] B3 = { n.m[2], n.m[6], n.m[10],n.m[14] };
		float[] B4 = { n.m[3], n.m[7], n.m[11],n.m[15] };
		
		return new Mat4(	Mat4.mh(A1, B1), Mat4.mh(A1, B2), Mat4.mh(A1, B3), Mat4.mh(A1, B4),
							Mat4.mh(A2, B1), Mat4.mh(A2, B2), Mat4.mh(A2, B3), Mat4.mh(A2, B4),
							Mat4.mh(A3, B1), Mat4.mh(A3, B2), Mat4.mh(A3, B3), Mat4.mh(A3, B4),
							Mat4.mh(A4, B1), Mat4.mh(A4, B2), Mat4.mh(A4, B3), Mat4.mh(A4, B4) );
	}
	
	public static Mat4 inverse( Mat4 m ) {
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
		Mat4 t = new Mat4();
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
	
	/* returns a FloatBuffer representing the matrix */
	public static FloatBuffer fb( Mat4 m ) {
		return Buffer.toBuffer( m.m );
	}
	
	/* returns the direction the matrix is facing */
	public static Vec3 getLookAt( Mat4 m ) {
		return new Vec3( m.m[2], m.m[6], m.m[10] );
	}
	
	/* returns the up vector of the matrix */
	public static Vec3 getUp( Mat4 m ) {
		return new Vec3( m.m[1], m.m[5], m.m[9] );
	}
	
	/* returns the translated position of the matrix */
	public static Vec3 getPos( Mat4 m ) {
		return new Vec3( m.m[3], m.m[7], m.m[11] );
	}
	
	/* returns a flattened floatbuffer of the matrix */
	public FloatBuffer gl() {
		return Mat4.fb( Mat4.flatten( this ) );
	}
	
	/* return a string representation of this matrix */
	public String toString() {
		String str = "mat4[ ";
		for( float f : this.m )
			str += f + " ";			
		return str + "]";
	}
}
