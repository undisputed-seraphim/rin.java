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
	
	public Mat4( FloatBuffer m ) {
		this(	m.get(0), m.get(1), m.get(2), m.get(3),
				m.get(4), m.get(5), m.get(6), m.get(7),
				m.get(8), m.get(9), m.get(10), m.get(11),
				m.get(12), m.get(13), m.get(14), m.get(15) );
	}
	
	public Mat4( float[] m ) {
		this(	m[ 0], m[ 1], m[ 2], m[ 3],
				m[ 4], m[ 5], m[ 6], m[ 7],
				m[ 8], m[ 9], m[10], m[11],
				m[12], m[13], m[14], m[15] );
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
	/*void gluLookAt(GLfloat eyex, GLfloat eyey, GLfloat eyez,
                  GLfloat centerx, GLfloat centery, GLfloat centerz,
                  GLfloat upx, GLfloat upy, GLfloat upz)
{
        GLfloat m[16];
        GLfloat x[3], y[3], z[3];
        GLfloat mag;
        
        z[0] = eyex - centerx;
        z[1] = eyey - centery;
        z[2] = eyez - centerz;
        mag = sqrt(z[0] * z[0] + z[1] * z[1] + z[2] * z[2]);
        if (mag) {                     
                z[0] /= mag;
                z[1] /= mag;
                z[2] /= mag;
        }
        
        y[0] = upx;
        y[1] = upy;
        y[2] = upz;
        
        x[0] = y[1] * z[2] - y[2] * z[1];
        x[1] = -y[0] * z[2] + y[2] * z[0];
        x[2] = y[0] * z[1] - y[1] * z[0];
        
        y[0] = z[1] * x[2] - z[2] * x[1];
        y[1] = -z[0] * x[2] + z[2] * x[0];
        y[2] = z[0] * x[1] - z[1] * x[0];

        
        mag = sqrt(x[0] * x[0] + x[1] * x[1] + x[2] * x[2]);
        if (mag) {
                x[0] /= mag;
                x[1] /= mag;
                x[2] /= mag;
        }
        
        mag = sqrt(y[0] * y[0] + y[1] * y[1] + y[2] * y[2]);
        if (mag) {
                y[0] /= mag;
                y[1] /= mag;
                y[2] /= mag;
        }
        
#define M(row,col)  m[col*4+row]
        M(0, 0) = x[0];
        M(0, 1) = x[1];
        M(0, 2) = x[2];
        M(0, 3) = 0.0;
        M(1, 0) = y[0];
        M(1, 1) = y[1];
        M(1, 2) = y[2];
        M(1, 3) = 0.0;
        M(2, 0) = z[0];
        M(2, 1) = z[1];
        M(2, 2) = z[2];
        M(2, 3) = 0.0;
        M(3, 0) = 0.0;
        M(3, 1) = 0.0;
        M(3, 2) = 0.0;
        M(3, 3) = 1.0;
#undef M
        glMultMatrixf(m);
        
        glTranslatef(-eyex, -eyey, -eyez);*/
	public static Mat4 lookAt( float eyex, float eyey, float eyez, float centerx, float centery, float centerz, float upx, float upy, float upz ) {
		/*float mag;
        
        Vec3 z = new Vec3( eyex - centerx, eyey - centery, eyez - centerz );
        mag = Vec3.magnitude( z );
        if( mag != 0 ) {                     
                z.x /= mag;
                z.y /= mag;
                z.z /= mag;
        }
        
        Vec3 y = new Vec3( upx, upy, upz );
        
        Vec3 x = new Vec3(y.y * z.z - y.z * z.y, -y.x * z.z + y.z * z.x, y.x * z.y - y.y * z.x );
        y.redefine( z.y * x.z - z.z * x.y, -z.x * x.z + z.z * x.x, z.x * x.y - z.y * x.x );

        
        mag = Vec3.magnitude( x );
        if( mag != 0 ) {
                x.x /= mag;
                x.y /= mag;
                x.z /= mag;
        }
        
        mag = Vec3.magnitude( y );
        if( mag != 0 ) {
                y.x /= mag;
                y.y /= mag;
                y.z /= mag;
        }
        
        Mat4 m = new Mat4( x.x, x.y, x.z, 0.0f,
        				y.x, y.y, y.z, 0.0f,
        				z.x, z.y, z.z, 0.0f,
        				0.0f, 0.0f, 0.0f, 1.0f );
        
        Mat4 t = Mat4.translate( new Mat4(), new Vec3( -eyex, -eyey, -eyez ) );
        
        return Mat4.multiply( t, m );*/
		
		/*zaxis = normal(At - Eye)
xaxis = normal(cross(Up, zaxis))
yaxis = cross(zaxis, xaxis)

 xaxis.x           yaxis.x           zaxis.x          0
 xaxis.y           yaxis.y           zaxis.y          0
 xaxis.z           yaxis.z           zaxis.z          0
-dot(xaxis, eye)  -dot(yaxis, eye)  -dot(zaxis, eye)  l*/
		
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
	
	public static Mat4 apply( Mat4 m, float f, Vec3 dir ) {
		return Mat4.multiply( new Mat4(), Mat4.translate( new Mat4(), Vec3.scale( dir, f ) ) );
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
