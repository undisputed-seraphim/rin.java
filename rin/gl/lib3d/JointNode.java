package rin.gl.lib3d;

import static org.lwjgl.opengl.GL20.*;
import rin.engine.math.Matrix4x4;
import rin.engine.math.Quaternion;
import rin.engine.math.Vector3;
import rin.gl.GL;
import rin.math.Vector3f;
import rin.util.math.Mat4;
import rin.util.math.Quat4;
import rin.util.math.Vec3;

public class JointNode extends Node {
	
	private String bone;
	
	public static interface ReturnFloat {
		public float get();
	}
	
	private float[] q = new float[4];
	private float[] t = new float[3];
	
	protected Mat4 joint = new Mat4();
	protected Mat4 inverse = new Mat4();
	protected Mat4 skin = new Mat4();
	
	public JointNode( String id ) { super( id ); }
	
	@Override
	protected JointNode actual() { return this; }
	
	public String getBoneId() { return bone; }
	public JointNode setBoneId( String id ) {
		bone = id;
		return this;
	}
	
	public void setJointMatrix( float[] m ) {
		joint = new Mat4( m );
	}
	
	public void setInverseBindMatrix( float[] m ) {
		inverse = new Mat4( m );
	}
	
	public float[] getQuatArray() { return q; }
	
	public Quat4 getQuat() {
		return new Quat4( q[0], q[1], q[2], q[3] );
	}
	
	public float[] getTransArray() { return t; }
	
	public Vec3 getTrans() {
		return new Vec3( t[0], t[1], t[2] );
	}
	
	public void applyBone( int index, Mat4 skinTransform ) {
		super.update();
		//transform = Mat4.multiply( transform, joint );
		//if( parent != null ) transform = Mat4.multiply( parent.transform, transform );
		//skin = transform;
		//skin = Mat4.multiply( skin, transform );
		//wMat = Mat4.multiply( parent.wMat, base );
		//skin = Mat4.multiply( skin, skinTransform );
		//wMat = Mat4.multiply( parent.transform, transform );
		//skin = Mat4.multiply( wMat, inverse );
		skin = wMat;
		Quat4 qu = skin.toQuat4();
		q[0] = qu.x;
		q[1] = qu.y;
		q[2] = qu.z;
		q[3] = qu.w;
		
		t[0] = skin.m[ 3];
		t[1] = skin.m[ 7];
		t[2] = skin.m[11];
		glUniform4f( GL.getUniform( "quats["+index+"]" ), q[0], q[1], q[2], q[3] );
		glUniform3f( GL.getUniform( "trans["+index+"]" ), t[0], t[1], t[2] );
	}
	
	@Override
	public void update( double dt ) {
		super.update( dt );
		if( parent instanceof JointNode )
			wMat = Mat4.multiply( parent.wMat, base );
		else wMat = base;
		
		//wMat = Mat4.multiply( parent.base, base );
		/* update bones world transform based on parents world transform */
			
		/*Animation a = scene.getCurrentAnimation();
		if( a != null ) {
			FrameSet fs = a.findFrames( name );
			if( fs != null ) {
				Mat4 tmp = new Mat4();
				
				// apply scale of current frame
				float[] sdata = fs.getScaleData();
				if( sdata.length > 0 ) {
					tmp = Mat4.multiply( tmp, Mat4.scale( new Mat4(), new Vec3( sdata[0], sdata[1], sdata[2] ) ) );
				}
				
				// apply rotation of current frame
				float[] rdata = fs.getRotationData();
				if( rdata.length > 0 ) {
					tmp = Mat4.multiply( tmp, new Quat4( rdata[0], rdata[1], rdata[2], rdata[3] ).toMat4() );
				}
				
				// apply translation of current frame
				float[] tdata = fs.getTranslationData();
				if( tdata.length > 0 ) {
					tmp = Mat4.translate( tmp, new Vec3( tdata[0], tdata[1], tdata[2] ) );
				}
				
				transform = tmp;
			}
		}*/
	}
}
