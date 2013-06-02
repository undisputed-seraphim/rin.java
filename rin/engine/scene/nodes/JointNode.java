package rin.engine.scene.nodes;

import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import rin.engine.util.TreeNode;
import rin.engine.view.gl.GL;
import rin.util.math.Mat4;
import rin.util.math.Quat4;
import rin.util.math.Vec3;

public class JointNode extends TreeNode<JointNode> {
	
	protected Mat4 joint = new Mat4();
	protected Mat4 world = new Mat4();
	protected Quat4 qWorld = new Quat4();
	protected Mat4 inverse = new Mat4();
	protected Mat4 transform = new Mat4();
	protected Mat4 poseAbsolute = new Mat4();
	
	protected float orientX = 0.0f;
	protected float orientY = 0.0f;
	protected float orientZ = 0.0f;
	
	protected Mat4 translate = new Mat4();
	protected Quat4 orient = new Quat4( 0, 0, 0, 1 );
	protected Mat4 rotate = new Mat4();
	
	public JointNode( String id ) { super( id ); }
	
	public void setJointMatrix( float[] m ) { joint = new Mat4( m ); }
	public void setJointTranslation( float[] t ) { tLocal.add( t[0], t[1], t[2] ); }
	public void setJointRotateX( float[] r ) { orient.applyOrientationDeg( Vec3.X_AXIS, r[3] ); }
	public void setJointRotateY( float[] r ) { orient.applyOrientationDeg( Vec3.Y_AXIS, r[3] ); }
	public void setJointRotateZ( float[] r ) { orient.applyOrientationDeg( Vec3.Z_AXIS, r[3] ); }	
	public void setInverseBindMatrix( float[] m ) { inverse = new Mat4( m ); }
	
	protected Quat4 rLocal = new Quat4( 0, 0, 0, 1 );
	protected Quat4 rGlobal = new Quat4( 0, 0, 0, 1 );
	protected Vec3 tLocal = new Vec3( 0, 0, 0 );
	protected Vec3 tGlobal = new Vec3( 0, 0, 0 );
	
	protected Quat4 rBaseGlobal = new Quat4( 0, 0, 0, 1 );
	protected Vec3 tBaseGlobal = new Vec3( 0, 0, 0 );
	
	public void applyBone( int index ) {
		world.redefine( poseAbsolute ).multiply( inverse );
		world.intoQuat4( qWorld );
		glUniform4f( GL.getUniform( "quats["+index+"]" ), qWorld.x, qWorld.y, qWorld.z, qWorld.w );
		glUniform3f( GL.getUniform( "trans["+index+"]" ), world.m[3], world.m[7], world.m[11] );
	}
	
	public void update( double dt ) {
		Animation cAnimation = ((Skeleton)tree).getCurrentAnimation();
		
		if( cAnimation != null ) {
			Frame cFrame = cAnimation.getFrame( getId() );
			if( cFrame != null ) {
				tLocal = cFrame.getCurrentTranslation();
				rLocal = cFrame.getCurrentRotation();
			}
		}
		
		if( parent != null ) {
			rGlobal.redefine( parent.rGlobal ).multiply( rLocal );
			tGlobal.redefine( tLocal ).rotate( parent.rGlobal ).add( parent.tGlobal );
		} else {
			rGlobal.redefine( rLocal );
			tGlobal.redefine( tLocal );
		}
		
		rGlobal.intoMat4( rotate );
		translate.identity().translate( tGlobal );
		Mat4.multiplyInto( translate, rotate, poseAbsolute );
	}
}

