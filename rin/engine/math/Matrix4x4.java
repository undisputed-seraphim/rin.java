package rin.engine.math;

public class Matrix4x4 {

	private float[] m = new float[]
			{ 1.0f, 0.0f, 0.0f, 0.0f,
			  0.0f, 1.0f, 0.0f, 0.0f,
			  0.0f, 0.0f, 1.0f, 0.0f,
			  0.0f, 0.0f, 0.0f, 1.0f
			};
	
	public Matrix4x4() {}
	
	public Matrix4x4( float[] mat ) {
		for( int i = 0; i < 16; i++ )
			m[i] = mat[ i ];
	}
	
	public Matrix4x4( Matrix4x4 mat ) {		
		for( int i = 0; i < 16; i++ )
			m[i] = mat.get( i );
	}
	
	public float[] getMatrixArray() { return m; }
	public float get( int index ) { return m[index]; }
	
}
