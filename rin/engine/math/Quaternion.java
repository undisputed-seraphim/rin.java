package rin.engine.math;

import rin.util.math.Quat4;

public class Quaternion {

	private float[] q = new float[]
			{ 0.0f, 0.0f, 0.0f, 1.0f };
	
	
	
	public Quaternion( float ... quat ) {
		for( int i = 0; i < quat.length && i < 4; i++ )
			q[i] = quat[i];
	}
	
	
	
	public float x() {
		return q[0];
	}
	
	public float y() {
		return q[1];
	}
	
	public float z() {
		return q[2];
	}
	
	public float w() {
		return q[3];
	}
	
	public float[] getArray() {
		return q;
	}
	
	
	
	public static Quaternion multiply( Quaternion q, Quaternion r ) {
		return new Quaternion(q.w() * r.x() + q.x() * r.w() + q.y() * r.z() - q.z() * r.y(),
							  q.w() * r.y() + q.y() * r.w() + q.z() * r.x() - q.x() * r.z(),
							  q.w() * r.z() + q.z() * r.w() + q.x() * r.y() - q.y() * r.x(),
							  q.w() * r.w() - q.x() * r.x() - q.y() * r.y() - q.z() * r.z() );
	}
	
	public Matrix4x4 toRotationMatrix() {
		float yy = y() * y();
		float zz = z() * z();
		float xy = x() * y();
		float zw = z() * w();
		float xz = x() * z();
		float yw = y() * w();
		float xx = x() * x();
		float yz = y() * z();
		float xw = x() * w();
		
		return new Matrix4x4( 1.0f - 2.0f * yy - 2.0f * zz, 2.0f * xy + 2.0f * zw, 2.0f * xz - 2.0f * yw, 0.0f,
							  2.0f * xy - 2.0f * zw, 1.0f - 2.0f * xx - 2.0f * zz, 2.0f * yz + 2.0f * xw, 0.0f,
							  2.0f * xz + 2.0f * yw, 2.0f * yz - 2.0f * xw, 1.0f - 2.0f * xx - 2.0f * yy, 0.0f,
							  0.0f, 0.0f, 0.0f, 1.0f );
	}
}
