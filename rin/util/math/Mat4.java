package rin.util.math;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import rin.util.Buffer;
import rin.util.math.Vec3;

public class Mat4 {
	public static final Mat4 IDENTITY = new Mat4();
	public static final Mat4 GUI = Mat4.orthographic( -1, 1, -1, 1, 1, -10 );
	
	/* array that holds the values of the matrix */
	public float[] m = new float[16];
	
	/* buffer for operations */
	private float[] b = new float[16];
	
	/* floatbuffer just in case... */
	private FloatBuffer glBuffer = Buffer.toBuffer( new float[16] );
	
	/* create an identity matrix */
	public Mat4() {
		identity();
	}
	
	/* create a matrix from 16 floats */
	public Mat4(	float m00, float m01, float m02, float m03,
					float m04, float m05, float m06, float m07,
					float m08, float m09, float m10, float m11,
					float m12, float m13, float m14, float m15 ) {
		m[ 0] = m00; m[ 1] = m01; m[ 2] = m02; m[ 3] = m03;
		m[ 4] = m04; m[ 5] = m05; m[ 6] = m06; m[ 7] = m07;
		m[ 8] = m08; m[ 9] = m09; m[10] = m10; m[11] = m11;
		m[12] = m12; m[13] = m13; m[14] = m14; m[15] = m15;
	}
	
	/* create a matrix from another matrix */
	public Mat4( Mat4 n ) {
		redefine( n );
	}
	
	public Mat4( FloatBuffer n ) {
		m[ 0] = n.get( 0); m[ 1] = n.get( 1); m[ 2] = n.get( 2); m[ 3] = n.get( 3);
		m[ 4] = n.get( 4); m[ 5] = n.get( 5); m[ 6] = n.get( 6); m[ 7] = n.get( 7);
		m[ 8] = n.get( 8); m[ 9] = n.get( 9); m[10] = n.get(10); m[11] = n.get(11);
		m[12] = n.get(12); m[13] = n.get(13); m[14] = n.get(14); m[15] = n.get(15);
	}
	
	public Mat4( float[] n ) {
		redefine( n );
	}
	
	public Mat4 identity() {
		m[ 0] = 1; m[ 1] = 0; m[ 2] = 0; m[ 3] = 0;
		m[ 4] = 0; m[ 5] = 1; m[ 6] = 0; m[ 7] = 0;
		m[ 8] = 0; m[ 9] = 0; m[10] = 1; m[11] = 0;
		m[12] = 0; m[13] = 0; m[14] = 0; m[15] = 1;
		return this;
	}
	
	public Mat4 redefine( float[] n ) {
		m[ 0] = n[ 0]; m[ 1] = n[ 1]; m[ 2] = n[ 2]; m[ 3] = n[ 3];
		m[ 4] = n[ 4]; m[ 5] = n[ 5]; m[ 6] = n[ 6]; m[ 7] = n[ 7];
		m[ 8] = n[ 8]; m[ 9] = n[ 9]; m[10] = n[10]; m[11] = n[11];
		m[12] = n[12]; m[13] = n[13]; m[14] = n[14]; m[15] = n[15];
		return this;
	}
	
	public Mat4 redefine( Mat4 n ) {
		m[ 0] = n.m[ 0]; m[ 1] = n.m[ 1]; m[ 2] = n.m[ 2]; m[ 3] = n.m[ 3];
		m[ 4] = n.m[ 4]; m[ 5] = n.m[ 5]; m[ 6] = n.m[ 6]; m[ 7] = n.m[ 7];
		m[ 8] = n.m[ 8]; m[ 9] = n.m[ 9]; m[10] = n.m[10]; m[11] = n.m[11];
		m[12] = n.m[12]; m[13] = n.m[13]; m[14] = n.m[14]; m[15] = n.m[15];
		return this;
	}
	
	protected Mat4 applyBuffer() {
		m[ 0] = b[ 0];	m[ 1] = b[ 1];	m[ 2] = b[ 2];	m[ 3] = b[ 3];
		m[ 4] = b[ 4];	m[ 5] = b[ 5];	m[ 6] = b[ 6];	m[ 7] = b[ 7];
		m[ 8] = b[ 8];	m[ 9] = b[ 9];	m[10] = b[10];	m[11] = b[11];
		m[12] = b[12];	m[13] = b[13];	m[14] = b[14];	m[15] = b[15];
		return this;
	}
	
	private FloatBuffer updateFloatBuffer() {
		glBuffer.position( 0 );
		glBuffer.put( m[ 0] ).put( m[ 4] ).put( m[ 8] ).put( m[12] );
		glBuffer.put( m[ 1] ).put( m[ 5] ).put( m[ 9] ).put( m[13] );
		glBuffer.put( m[ 2] ).put( m[ 6] ).put( m[10] ).put( m[14] );
		glBuffer.put( m[ 3] ).put( m[ 7] ).put( m[11] ).put( m[15] );
		glBuffer.flip();
		return glBuffer;
	}
	
	/* return the matrix flattened (column/row swap) */
	public static Mat4 flatten( Mat4 m ) {
		return m.flattenInto( new Mat4() );
	}

	public float[] flattenInto( float[] $r ) {
		$r[ 0] = m[ 0]; $r[ 1] = m[ 4]; $r[ 2] = m[ 8]; $r[ 3] = m[12];
		$r[ 4] = m[ 1]; $r[ 5] = m[ 5]; $r[ 6] = m[ 9]; $r[ 7] = m[13];
		$r[ 8] = m[ 2]; $r[ 9] = m[ 6]; $r[10] = m[10]; $r[11] = m[14];
		$r[12] = m[ 3]; $r[13] = m[ 7]; $r[14] = m[11]; $r[15] = m[15];
		return $r;
	}
	
	public Mat4 flattenInto( Mat4 $m ) {
		flattenInto( $m.m );
		return $m;
	}
	
	public Mat4 flatten() {
		flattenInto( b );
		return applyBuffer();
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
	
	public static Mat4 orthographic( float left, float right, float bottom, float top, float znear, float zfar ) {
		float tx = - ( (right + left) / (right - left) );
		float ty = - ( (top + bottom) / (top - bottom) );
		float tz = - ( (zfar + znear) / (zfar - znear) );
		
		float sx = 2 / (right-left);
		float sy = 2 / (top-bottom);
		float sz = -2 / (zfar-znear);
		
		return new Mat4( sx, 0, 0, tx,
						 0, sy, 0, ty,
						 0, 0, sz, tz,
						 0, 0, 0, 1 );
	}
	
	public static Quat4 multVec3( Mat4 mat, Vec3 v ) {
		float[] res = new float[4];
		for( int i = 0; i < 4; i++ )
			res[i] = v.x * mat.m[0*4+i] +
					 v.y * mat.m[1*4+i] +
					 v.z * mat.m[2*4+i] + mat.m[3*4+i] ;
		return new Quat4( res[0], res[1], res[2], res[3] );
	}
	
	public static Mat4 mult( Mat4 a, Mat4 b ) {
		Mat4 r = new Mat4( 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 );
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				r.m[ i*4 + j ] =
					a.m[ i*4 + 0 ] * b.m[ 0*4 + j ] +
					a.m[ i*4 + 1 ] * b.m[ 1*4 + j ] +
					a.m[ i*4 + 2 ] * b.m[ 2*4 + j ] +
					a.m[ i*4 + 3 ] * b.m[ 3*4 + j ] ;
			}
		}
		return r;
	}

	public static Mat4 lookAt( float eyex, float eyey, float eyez, float centerx, float centery, float centerz, float upx, float upy, float upz ) {
		Vec3 eye = new Vec3( eyex, eyey, eyez );
		Vec3 center = new Vec3( centerx, centery, centerz );
		Vec3 zaxis = Vec3.normalize( Vec3.subtract( center, eye ) );
		Vec3 xaxis = Vec3.normalize( Vec3.cross( new Vec3( upx, upy, upz ), zaxis ) );
		Vec3 yaxis = Vec3.cross( zaxis, xaxis );
		
		Mat4 m = new Mat4(
				xaxis.x, xaxis.y, xaxis.z, 0,
				yaxis.x, yaxis.y, yaxis.z, 0,
				zaxis.x, zaxis.y, zaxis.z, 0,
				0, 0, 0, 1 );

		return m;

		}
	
	public static Vec3 unProject( float x, float y, float z, Mat4 mod, Mat4 cam, IntBuffer viewport ) {
		Mat4 mat = new Mat4();
		Vec3 in = new Vec3();
		Quat4 out = new Quat4( 0.0f, 0.0f, 0.0f, 1.0f );
		
		mat = Mat4.mult( mod, cam );
		mat = Mat4.inverse( mat );
		in.x = x;
		in.y = y;
		in.z = z;

		in.x = ( in.x - viewport.get( 0 ) ) / viewport.get( 2 );
	    in.y = ( in.y - viewport.get( 1 ) ) / viewport.get( 3 );

	    in.x = in.x * 2 - 1;
	    in.y = in.y * 2 - 1;
	    in.z = in.z * 2 - 1;

	    out = Mat4.multVec3( mat, in );
	    if( out.w == 0 )
	    	return new Vec3( 0.0f, 0.0f, 0.0f );
	    
	    out.w = 1.0f / out.w;
	    
	    return new Vec3( out.x * out.w, out.y * out.w, out.z * out.w );
	}
	
	public static Mat4 rotate( Mat4 m, Vec3 v ) {
		return Mat4.multiply( m, Quat4.multiply( Quat4.multiply( Quat4.create( Vec3.X_AXIS, v.x ),
				Quat4.create( Vec3.Y_AXIS, v.y ) ), Quat4.create( Vec3.Z_AXIS, v.z ) ).toMat4() );
	}
	
	public Mat4 translateTo( float x, float y, float z ) {
		m[ 0] = 1; m[ 1] = 0; m[ 2] = 0; m[ 3] = x;
		m[ 4] = 0; m[ 5] = 1; m[ 6] = 0; m[ 7] = y;
		m[ 8] = 0; m[ 9] = 0; m[10] = 1; m[11] = z;
		m[12] = 0; m[13] = 0; m[14] = 0; m[15] = 1;
		return this;
	}
	
	public static float[] translateInto( float[] m, float x, float y, float z, float[] $r ) {
		$r[ 0] = m[ 0];
		$r[ 1] = m[ 1];
		$r[ 2] = m[ 2];
		$r[ 3] = m[ 0] * x + m[ 1] * y + m[ 2] * z + m[ 3];
		$r[ 4] = m[ 4];
		$r[ 5] = m[ 5];
		$r[ 6] = m[ 6];
		$r[ 7] = m[ 4] * x + m[ 5] * y + m[ 6] * z + m[ 7];
		$r[ 8] = m[ 8];
		$r[ 9] = m[ 9];
		$r[10] = m[10];
		$r[11] = m[ 8] * x + m[ 9] * y + m[10] * z + m[11];
		$r[12] = m[12];
		$r[13] = m[13];
		$r[14] = m[14];
		$r[15] = m[12] * x + m[13] * y + m[14] * z + m[15];
		return $r;
	}
	
	public static float[] translateInto( float[] m, Vec3 v, float[] $r ) {
		translateInto( m, v.x, v.y, v.z, $r );
		return $r;
	}
	
	public static float[] translateInto( float[] m, float[] v, float[] $r ) {
		translateInto( m, v[0], v[1], v[2], $r );
		return $r;
	}
	
	public static float[] translate( float[] m, float[] v ) {
		return translateInto( m, v, new float[16] );
	}

	public float[] translateInto( float[] v, float[] $r ) {
		translateInto( m, v, $r );
		return $r;
	}
	
	public Mat4 translate( float[] v ) {
		translateInto( m, v, b );
		return applyBuffer();
	}
	
	public static Mat4 translateInto( Mat4 m, Vec3 v, Mat4 $r ) {
		translateInto( m.m, v, $r.m );
		return $r;
	}
	
	/* returns a matrix used to translate other matrices */
	public static Mat4 translate( Mat4 m, Vec3 v ) {
		return translateInto( m, v, new Mat4() );
	}
	
	public Mat4 translate( Vec3 v ) {
		translateInto( m, v, b );
		return applyBuffer();
	}
	
	public Mat4 translateInto( Vec3 v, Mat4 $r ) {
		translateInto( m, v, $r.m );
		return $r;
	}
	
	public static Vec3 transform( Mat4 m, Vec3 v ) {
		float x = v.x * m.m[0] + v.y * m.m[4] + v.z * m.m[8] + m.m[12];
		float y = v.x * m.m[1] + v.y * m.m[5] + v.z * m.m[9] + m.m[13];
		float z = v.x * m.m[2] + v.y * m.m[6] + v.z * m.m[10] + m.m[14];
		return new Vec3( x, y, z );
	}
	
	public Mat4 scaleTo( float x, float y, float z ) {
		m[ 0] = x; m[ 1] = 0; m[ 2] = 0; m[ 3] = 0;
		m[ 4] = 0; m[ 5] = y; m[ 6] = 0; m[ 7] = 0;
		m[ 8] = 0; m[ 9] = 0; m[10] = z; m[11] = 0;
		m[12] = 0; m[13] = 0; m[14] = 0; m[15] = 1;
		return this;
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
	
	public static Mat4 apply( Mat4 m, float f, Vec3 dir ) {
		return Mat4.multiply( new Mat4(), Mat4.translate( new Mat4(), Vec3.scale( dir, f ) ) );
	}
	
	public static float[] multiplyInto( float[] m, float[] n, float[] $r ) {
		$r[ 0] = m[ 0] * n[ 0] + m[ 1] * n[ 4] + m[ 2] * n[ 8] + m[ 3] * n[12];
		$r[ 1] = m[ 0] * n[ 1] + m[ 1] * n[ 5] + m[ 2] * n[ 9] + m[ 3] * n[13];
		$r[ 2] = m[ 0] * n[ 2] + m[ 1] * n[ 6] + m[ 2] * n[10] + m[ 3] * n[14];
		$r[ 3] = m[ 0] * n[ 3] + m[ 1] * n[ 7] + m[ 2] * n[11] + m[ 3] * n[15];
		$r[ 4] = m[ 4] * n[ 0] + m[ 5] * n[ 4] + m[ 6] * n[ 8] + m[ 7] * n[12];
		$r[ 5] = m[ 4] * n[ 1] + m[ 5] * n[ 5] + m[ 6] * n[ 9] + m[ 7] * n[13];
		$r[ 6] = m[ 4] * n[ 2] + m[ 5] * n[ 6] + m[ 6] * n[10] + m[ 7] * n[14];
		$r[ 7] = m[ 4] * n[ 3] + m[ 5] * n[ 7] + m[ 6] * n[11] + m[ 7] * n[15];
		$r[ 8] = m[ 8] * n[ 0] + m[ 9] * n[ 4] + m[10] * n[ 8] + m[11] * n[12];
		$r[ 9] = m[ 8] * n[ 1] + m[ 9] * n[ 5] + m[10] * n[ 9] + m[11] * n[13];
		$r[10] = m[ 8] * n[ 2] + m[ 9] * n[ 6] + m[10] * n[10] + m[11] * n[14];
		$r[11] = m[ 8] * n[ 3] + m[ 9] * n[ 7] + m[10] * n[11] + m[11] * n[15];
		$r[12] = m[12] * n[ 0] + m[13] * n[ 4] + m[14] * n[ 8] + m[15] * n[12];
		$r[13] = m[12] * n[ 1] + m[13] * n[ 5] + m[14] * n[ 9] + m[15] * n[13];
		$r[14] = m[12] * n[ 2] + m[13] * n[ 6] + m[14] * n[10] + m[15] * n[14];
		$r[15] = m[12] * n[ 3] + m[13] * n[ 7] + m[14] * n[11] + m[15] * n[15];
		return $r;
	}
	
	public static float[] multiply( float[] m, float[] n ) {
		return multiplyInto( m, n, new float[16] );
	}
	
	public Mat4 multiply( float[] n ) {
		multiplyInto( m, n, b );
		return applyBuffer();
	}
	
	public float[] multiplyInto( float[] n, float[] $r ) {
		multiplyInto( m, n, $r );
		return $r;
	}
	
	public static Mat4 multiplyInto( Mat4 m, Mat4 n, Mat4 $result ) {
		multiplyInto( m.m, n.m, $result.m );
		return $result;
	}
	
	public static Mat4 multiply( Mat4 m, Mat4 n ) {
		return multiplyInto( m, n, new Mat4() );
	}
	
	public Mat4 multiply( Mat4 n ) {
		multiplyInto( m, n.m, b );
		return applyBuffer();
	}
	
	public Mat4 multiplyInto( Mat4 n, Mat4 $r ) {
		multiplyInto( m, n.m, $r.m );
		return $r;
	}
	
	public static Mat4 inverse( Mat4 m ) {
		float[]	inv = new float[16],
				res = new float[16];
		
		inv[ 0] = m.m[ 5] * m.m[10] * m.m[15] - m.m[ 5] * m.m[11] * m.m[14] - m.m[ 9] * m.m[ 6] * m.m[15] +
				  m.m[ 9] * m.m[ 7] * m.m[14] + m.m[13] * m.m[ 6] * m.m[11] - m.m[13] * m.m[ 7] * m.m[10] ;
		inv[ 4] =-m.m[ 4] * m.m[10] * m.m[15] + m.m[ 4] * m.m[11] * m.m[14] + m.m[ 8] * m.m[ 6] * m.m[15] -
				  m.m[ 8] * m.m[ 7] * m.m[14] - m.m[12] * m.m[ 6] * m.m[11] + m.m[12] * m.m[ 7] * m.m[10] ;
		inv[ 8] = m.m[ 4] * m.m[ 9] * m.m[15] - m.m[ 4] * m.m[11] * m.m[13] - m.m[ 8] * m.m[ 5] * m.m[15] +
				  m.m[ 8] * m.m[ 7] * m.m[13] + m.m[12] * m.m[ 5] * m.m[11] - m.m[12] * m.m[ 7] * m.m[ 9] ;
		inv[12] =-m.m[ 4] * m.m[ 9] * m.m[14] + m.m[ 4] * m.m[10] * m.m[13] + m.m[ 8] * m.m[ 5] * m.m[14] -
				  m.m[ 8] * m.m[ 6] * m.m[13] - m.m[12] * m.m[ 5] * m.m[10] + m.m[12] * m.m[ 6] * m.m[ 9] ;
		inv[ 1] =-m.m[ 1] * m.m[10] * m.m[15] + m.m[ 1] * m.m[11] * m.m[14] + m.m[ 9] * m.m[ 2] * m.m[15] -
				  m.m[ 9] * m.m[ 3] * m.m[14] - m.m[13] * m.m[ 2] * m.m[11] + m.m[13] * m.m[ 3] * m.m[10] ;
		inv[ 5] = m.m[ 0] * m.m[10] * m.m[15] - m.m[ 0] * m.m[11] * m.m[14] - m.m[ 8] * m.m[ 2] * m.m[15] +
				  m.m[ 8] * m.m[ 3] * m.m[14] + m.m[12] * m.m[ 2] * m.m[11] - m.m[12] * m.m[ 3] * m.m[10] ;
		inv[ 9] =-m.m[ 0] * m.m[ 9] * m.m[15] + m.m[ 0] * m.m[11] * m.m[13] + m.m[ 8] * m.m[ 1] * m.m[15] -
				  m.m[ 8] * m.m[ 3] * m.m[13] - m.m[12] * m.m[ 1] * m.m[11] + m.m[12] * m.m[ 3] * m.m[ 9] ;
		inv[13] = m.m[ 0] * m.m[ 9] * m.m[14] - m.m[ 0] * m.m[10] * m.m[13] - m.m[ 8] * m.m[ 1] * m.m[14] +
				  m.m[ 8] * m.m[ 2] * m.m[13] + m.m[12] * m.m[ 1] * m.m[10] - m.m[12] * m.m[ 2] * m.m[ 9] ;
		inv[ 2] = m.m[ 1] * m.m[ 6] * m.m[15] - m.m[ 1] * m.m[ 7] * m.m[14] - m.m[ 5] * m.m[ 2] * m.m[15] +
				  m.m[ 5] * m.m[ 3] * m.m[14] + m.m[13] * m.m[ 2] * m.m[ 7] - m.m[13] * m.m[ 3] * m.m[ 6] ;
		inv[ 6] =-m.m[ 0] * m.m[ 6] * m.m[15] + m.m[ 0] * m.m[ 7] * m.m[14] + m.m[ 4] * m.m[ 2] * m.m[15] -
				  m.m[ 4] * m.m[ 3] * m.m[14] - m.m[12] * m.m[ 2] * m.m[ 7] + m.m[12] * m.m[ 3] * m.m[ 6] ;
		inv[10] = m.m[ 0] * m.m[ 5] * m.m[15] - m.m[ 0] * m.m[ 7] * m.m[13] - m.m[ 4] * m.m[ 1] * m.m[15] +
				  m.m[ 4] * m.m[ 3] * m.m[13] + m.m[12] * m.m[ 1] * m.m[ 7] - m.m[12] * m.m[ 3] * m.m[ 5] ;
		inv[14] =-m.m[ 0] * m.m[ 5] * m.m[14] + m.m[ 0] * m.m[ 6] * m.m[13] + m.m[ 4] * m.m[ 1] * m.m[14] -
				  m.m[ 4] * m.m[ 2] * m.m[13] - m.m[12] * m.m[ 1] * m.m[ 6] + m.m[12] * m.m[ 2] * m.m[ 5] ;
		inv[ 3] =-m.m[ 1] * m.m[ 6] * m.m[11] + m.m[ 1] * m.m[ 7] * m.m[10] + m.m[ 5] * m.m[ 2] * m.m[11] -
				  m.m[ 5] * m.m[ 3] * m.m[10] - m.m[ 9] * m.m[ 2] * m.m[ 7] + m.m[ 9] * m.m[ 3] * m.m[ 6] ;
		inv[ 7] = m.m[ 0] * m.m[ 6] * m.m[11] - m.m[ 0] * m.m[ 7] * m.m[10] - m.m[ 4] * m.m[ 2] * m.m[11] +
				  m.m[ 4] * m.m[ 3] * m.m[10] + m.m[ 8] * m.m[ 2] * m.m[ 7] - m.m[ 8] * m.m[ 3] * m.m[ 6] ;
		inv[11] =-m.m[ 0] * m.m[ 5] * m.m[11] + m.m[ 0] * m.m[ 7] * m.m[ 9] + m.m[ 4] * m.m[ 1] * m.m[11] -
				  m.m[ 4] * m.m[ 3] * m.m[ 9] - m.m[ 8] * m.m[ 1] * m.m[ 7] + m.m[ 8] * m.m[ 3] * m.m[ 5] ;
		inv[15] = m.m[ 0] * m.m[ 5] * m.m[10] - m.m[ 0] * m.m[ 6] * m.m[ 9] - m.m[ 4] * m.m[ 1] * m.m[10] +
				  m.m[ 4] * m.m[ 2] * m.m[ 9] + m.m[ 8] * m.m[ 1] * m.m[ 6] - m.m[ 8] * m.m[ 2] * m.m[ 5] ;

		float det = m.m[ 0] * inv[ 0] + m.m[ 1] * inv[ 4] + m.m[ 2] * inv[ 8] + m.m[ 3] * inv[12];
		if (det == 0)
			return m;

		det = 1.0f / det;

		for (int i = 0; i < 16; i++)
			res[i] = inv[i] * det;
		
		return new Mat4( res );
	}
	
	public Quat4 toQuat4() {
		return intoQuat4( new Quat4() );
	}
	
	public Quat4 intoQuat4( Quat4 $q ) {
		float T = m[0] + m[5] + m[10];
		float S = 0;
		if ( T > 0.00000001 ) {
	    	S = (float)(0.5f / Math.sqrt( T + 1 ));
	    	$q.x = ( m[9] - m[6] ) * S;
		    $q.y = ( m[2] - m[8] ) * S;
	    	$q.z = ( m[4] - m[1] ) * S;
	    	$q.w = 0.25f / S;
		} else {
			if ( m[0] > m[5] && m[0] > m[10] ) {
	    		S = (float)(Math.sqrt( 1.0 + m[0] - m[5] - m[10] ) * 2);
			    $q.x = 0.25f * S;
		    	$q.y = ( m[4] + m[1] ) / S;
		    	$q.z = ( m[2] + m[8] ) / S;
		    	$q.w = ( m[9] - m[6] ) / S;
			} else if ( m[5] > m[10] ) {
		    	S = (float)(Math.sqrt( 1.0 + m[5] - m[0] - m[10] ) * 2);
		    	$q.x = ( m[4] + m[1] ) / S;
    			$q.y = 0.25f * S;
		    	$q.z = ( m[9] + m[6] ) / S;
	    		$q.w = ( m[2] - m[8] ) / S;
			} else {
			    S = (float)(Math.sqrt( 1.0 + m[10] - m[0] - m[5] ) * 2);
		    	$q.x = ( m[2] + m[8] ) / S;
			    $q.y = ( m[9] + m[6] ) / S;
			    $q.z = 0.25f * S;
			    $q.w = ( m[4] - m[1] ) / S;
			}
		}
		return $q;
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
		return updateFloatBuffer();
	}
	
	/*lerp: function( m, n, dt ) {
		return new Float32Array(
			[ m[0]*(1-dt) + n[0]*(dt),m[1]*(1-dt) + n[1]*(dt),m[2]*(1-dt) + n[2]*(dt),m[3]*(1-dt) + n[3]*(dt),
			  m[4]*(1-dt) + n[4]*(dt),m[5]*(1-dt) + n[5]*(dt),m[6]*(1-dt) + n[6]*(dt),m[7]*(1-dt) + n[7]*(dt),
			  m[8]*(1-dt) + n[8]*(dt),m[9]*(1-dt) + n[9]*(dt),m[10]*(1-dt) + n[10]*(dt),m[11]*(1-dt) + n[11]*(dt),
			  m[12]*(1-dt) + n[12]*(dt),m[13]*(1-dt) + n[13]*(dt),m[14]*(1-dt) + n[14]*(dt),m[15]*(1-dt) + n[15]*(dt)] );
	}*/
	
	public static Mat4 lerp( Mat4 m, Mat4 n, float dt ) {
		return new Mat4(
				m.m[0]*(1-dt) + n.m[0]*(dt),m.m[1]*(1-dt) + n.m[1]*(dt),m.m[2]*(1-dt) + n.m[2]*(dt),m.m[3]*(1-dt) + n.m[3]*(dt),
				  m.m[4]*(1-dt) + n.m[4]*(dt),m.m[5]*(1-dt) + n.m[5]*(dt),m.m[6]*(1-dt) + n.m[6]*(dt),m.m[7]*(1-dt) + n.m[7]*(dt),
				  m.m[8]*(1-dt) + n.m[8]*(dt),m.m[9]*(1-dt) + n.m[9]*(dt),m.m[10]*(1-dt) + n.m[10]*(dt),m.m[11]*(1-dt) + n.m[11]*(dt),
				  m.m[12]*(1-dt) + n.m[12]*(dt),m.m[13]*(1-dt) + n.m[13]*(dt),m.m[14]*(1-dt) + n.m[14]*(dt),m.m[15]*(1-dt) + n.m[15]*(dt) );
	}
	
	/* return a string representation of this matrix */
	public String toString() {
		String str = "mat4[ ";
		for( float f : this.m )
			str += f + " ";			
		return str + "]";
	}
}
