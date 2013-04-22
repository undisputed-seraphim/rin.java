package rin.engine.math;

public class Matrix4x4 {

	private float[] m = new float[]
			{ 1.0f, 0.0f, 0.0f, 0.0f,
			  0.0f, 1.0f, 0.0f, 0.0f,
			  0.0f, 0.0f, 1.0f, 0.0f,
			  0.0f, 0.0f, 0.0f, 1.0f };
	
	
	
	public Matrix4x4() {}
	
	public Matrix4x4( float ... mat ) {
		for( int i = 0; i < mat.length && i < 16; i++ )
			m[0] = mat[i];
	}
	
	public Matrix4x4( Matrix4x4 mat ) {		
		for( int i = 0; i < 16; i++ )
			m[i] = mat.get( i );
	}
	
	
	
	public float[] getArray() {
		return m;
	}
	
	public float get( int index ) {
		return m[index];
	}
	
	

	private static float mh( float[] v, float[] w ) { return v[0] * w[0] + v[1] * w[1] + v[2] * w[2] + v[3] * w[3]; }
	public static Matrix4x4 multiply( Matrix4x4 m, Matrix4x4 n ) {
		float[] A1 = { m.m[0], m.m[1], m.m[2], m.m[3]  };
		float[] A2 = { m.m[4], m.m[5], m.m[6], m.m[7]  };
		float[] A3 = { m.m[8], m.m[9], m.m[10],m.m[11] };
		float[] A4 = { m.m[12],m.m[13],m.m[14],m.m[15] };
		float[] B1 = { n.m[0], n.m[4], n.m[8], n.m[12] };
		float[] B2 = { n.m[1], n.m[5], n.m[9], n.m[13] };
		float[] B3 = { n.m[2], n.m[6], n.m[10],n.m[14] };
		float[] B4 = { n.m[3], n.m[7], n.m[11],n.m[15] };
		
		return new Matrix4x4( mh(A1, B1), mh(A1, B2), mh(A1, B3), mh(A1, B4),
							  mh(A2, B1), mh(A2, B2), mh(A2, B3), mh(A2, B4),
							  mh(A3, B1), mh(A3, B2), mh(A3, B3), mh(A3, B4),
							  mh(A4, B1), mh(A4, B2), mh(A4, B3), mh(A4, B4) );
	}
	
	public Matrix4x4 multiply( Matrix4x4 m ) {
		this.m = multiply( this, m ).getArray();
		return this;
	}
	
	public static Matrix4x4 translate( Matrix4x4 m, Vector3 v ) {
		Matrix4x4 t = new Matrix4x4();
		
		t.m[ 3] = v.x();
		t.m[ 7] = v.y();
		t.m[11] = v.z();
		t.m[15] = 1.0f;
		
		return multiply( m, t );
	}
	
	public Matrix4x4 translate( Vector3 v ) {
		this.m = translate( this, v ).getArray();
		return this;
	}
	
	public Vector3 getTranslation() {
		return new Vector3( m[3], m[7], m[11] );
	}
	
	public Quaternion toQuaternion() {
		float t = m[0] + m[5] + m[10];
		float s = 0.0f;
		float x = 0.0f;
		float y = 0.0f;
		float z = 0.0f;
		float w = 1.0f;
		
		if( t > 0.00000001f ) {
			s = (float)(0.5f / Math.sqrt( t + 1 ));
	    	x = ( m[9] - m[6] ) * s;
		    y = ( m[2] - m[8] ) * s;
	    	z = ( m[4] - m[1] ) * s;
	    	w = 0.25f / s;
		} else {
			if ( m[0] > m[5] && m[0] > m[10] ) {
	    		s = (float)(Math.sqrt( 1.0 + m[0] - m[5] - m[10] ) * 2);
			    x = 0.25f * s;
		    	y = ( m[4] + m[1] ) / s;
		    	z = ( m[2] + m[8] ) / s;
		    	w = ( m[9] - m[6] ) / s;
			} else if ( m[5] > m[10] ) {
		    	s = (float)(Math.sqrt( 1.0 + m[5] - m[0] - m[10] ) * 2);
		    	x = ( m[4] + m[1] ) / s;
    			y = 0.25f * s;
		    	z = ( m[9] + m[6] ) / s;
	    		w = ( m[2] - m[8] ) / s;
			} else {
			    s = (float)(Math.sqrt( 1.0 + m[10] - m[0] - m[5] ) * 2);
		    	x = ( m[2] + m[8] ) / s;
			    y = ( m[9] + m[6] ) / s;
			    z = 0.25f * s;
			    w = ( m[4] - m[1] ) / s;
			}
		}
		
		return new Quaternion( x, y, z, w );
	}
	
}
