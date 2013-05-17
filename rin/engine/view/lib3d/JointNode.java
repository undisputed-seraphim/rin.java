package rin.engine.view.lib3d;

import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import rin.gl.GL;
import rin.util.math.Mat4;
import rin.util.math.Quat4;
import rin.util.math.Vec3;

public class JointNode extends SceneNode<JointNode> {
	
	protected Mat4 joint = new Mat4();
	protected Mat4 world = new Mat4();
	protected Mat4 inverse = new Mat4();
	protected Mat4 skin = new Mat4();
	
	protected float[] trans = new float[3];
	protected float[] rotateX = new float[4];
	protected float[] rotateY = new float[4];
	protected float[] rotateZ = new float[4];
	
	protected Quat4 orient = new Quat4();
	protected Quat4 rotate = new Quat4();
	
	public JointNode( String id ) { super( id ); }
	@Override public JointNode actual() { return this; }
	
	public void setJointMatrix( float[] m ) { joint = new Mat4( m ); }
	public void setJointTranslation( float[] t ) { trans = t; }
	public void setJointRotateX( float[] r ) { rotateX = r; }
	public void setJointRotateY( float[] r ) { rotateY = r; }
	public void setJointRotateZ( float[] r ) { rotateZ = r; }
	public void setInverseBindMatrix( float[] m ) { inverse = new Mat4( m ); }
	
	public void applyBone( int index ) {
		//skin = Mat4.multiply( orient.toMat4(), inverse );
		//skin = Mat4.multiply( skin, Mat4.translate( new Mat4(), new Vec3( trans[0], trans[1], trans[2] ) ) );
		//Quat4 q = skin.toQuat4();
		//Vec3 t = new Vec3( skin.m[3], skin.m[7], skin.m[11] );
		//w = Mat4.multiply( w, inverse );
		world = rotate.toMat4();
		//world = Mat4.multiply( joint, world );
		Quat4 q = world.toQuat4();
		glUniform4f( GL.getUniform( "quats["+index+"]" ), rotate.x, rotate.y, rotate.z, rotate.w );
		glUniform3f( GL.getUniform( "trans["+index+"]" ), world.m[3], world.m[7], world.m[11] );
		
		
		
		/*Animation cAnimation = ((Skeleton)tree).getCurrentAnimation();
		if( cAnimation != null ) {
			Frame cFrame = cAnimation.getFrame( getId() );
			if( cFrame != null ) {
				cFrame.getCurrentTranslation();
			}
		}

		Mat4 m = orient.toMat4();
		glUniform4f( GL.getUniform( "quats["+index+"]" ), orient.x, orient.y, orient.z, orient.w );
		glUniform3f( GL.getUniform( "trans["+index+"]" ), m.m[3], m.m[7], m.m[11] );*/
	}
	
	public void finish() {
		orient = Quat4.multiply( Quat4.multiply( new Quat4( rotateX ), new Quat4( rotateY ) ), new Quat4( rotateZ ) );
		//joint = rotate.toMat4();
		joint = Mat4.translate( new Mat4(), new Vec3( trans[0], trans[1], trans[2] ) );
		if( parent != null && parent != tree.getRoot() ) {
			orient = Quat4.multiply( parent.orient, orient );
			joint = Mat4.translate( new Mat4(), Vec3.add( new Vec3( trans[0], trans[1], trans[2] ) , new Vec3( parent.trans[0], parent.trans[1], parent.trans[2] ) ) );
		}
	}
	
	public void update( double dt ) {
		Animation cAnimation = ((Skeleton)tree).getCurrentAnimation();
		if( cAnimation != null ) {
			Frame cFrame = cAnimation.getFrame( getId() );
			if( cFrame != null ) {
				rotate = Quat4.multiply( orient, cFrame.getCurrentRotation() );
				if( parent != null && parent != tree.getRoot() ) {
					rotate = Quat4.multiply( parent.rotate, rotate );
				}
				//w = Mat4.multiply( new Mat4(), cFrame.getCurrentRotation().toMat4() );
				//world = Mat4.multiply( world, Mat4.translate( new Mat4(), cFrame.getCurrentTranslation() ) );
			}
		}
		//world = Mat4.multiply( world, inverse );
	}
}
