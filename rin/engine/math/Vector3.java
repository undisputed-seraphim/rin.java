package rin.engine.math;

public class Vector3 {

	private float[] v = new float[]
			{ 0.0f, 0.0f, 0.0f };
	
	public Vector3() {}
	
	public Vector3( float ... vec ) {
		for( int i = 0; i < vec.length && i < 3; i++ )
			v[i] = vec[i];
	}
	
	public float x() {
		return v[0];
	}
	
	public float y() {
		return v[1];
	}
	
	public float z() {
		return v[2];
	}
	
	public float[] getArray() {
		return v;
	}
	
	public static Vector3 add( Vector3 v, Vector3 w ) {
		return new Vector3( v.x() + w.x(), v.y() + w.y(), v.z() + w.z() );
	}
}
