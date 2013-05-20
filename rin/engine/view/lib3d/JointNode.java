package rin.engine.view.lib3d;

import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import rin.engine.lib.gui.RPauseDialog;
import rin.gl.GL;
import rin.util.math.Mat4;
import rin.util.math.Quat4;
import rin.util.math.Vec3;

public class JointNode extends SceneNode<JointNode> {
	
	protected Mat4 joint = new Mat4();
	protected Mat4 world = new Mat4();
	protected Mat4 inverse = new Mat4();
	protected Mat4 skin = new Mat4();
	
	protected Vec3 trans = new Vec3( 0, 0, 0 );
	protected Quat4 orientX = Quat4.create( new Vec3( 1.0f, 0.0f, 0.0f ), 0 );
	protected Quat4 orientY = Quat4.create( new Vec3( 0.0f, 1.0f, 0.0f ), 0 );
	protected Quat4 orientZ = Quat4.create( new Vec3( 0.0f, 0.0f, 1.0f ), 0 );
	
	protected Mat4 translate = new Mat4();
	protected Mat4 orient = new Mat4();
	protected Mat4 rotate = new Mat4();
	
	public JointNode( String id ) { super( id ); }
	@Override public JointNode actual() { return this; }
	
	public void setJointMatrix( float[] m ) { joint = new Mat4( m ); }
	public void setJointTranslation( float[] t ) { trans = Vec3.add( trans, new Vec3( t[0], t[1], t[2] ) ); }
	public void setJointRotateX( float[] r ) { orient = Mat4.multiply( orient, Quat4.create( new Vec3( r[0], r[1], r[2] ), r[3] * Quat4.PIOVER180 ).toMat4() ); }
	public void setJointRotateY( float[] r ) { orient = Mat4.multiply( orient, Quat4.create( new Vec3( r[0], r[1], r[2] ), r[3] * Quat4.PIOVER180 ).toMat4() ); }
	public void setJointRotateZ( float[] r ) { orient = Mat4.multiply( orient, Quat4.create( new Vec3( r[0], r[1], r[2] ), r[3] * Quat4.PIOVER180 ).toMat4() ); }
	public void setInverseBindMatrix( float[] m ) { inverse = new Mat4( m ); }
	
	public void applyBone( int index ) {
		world = Mat4.multiply( skin, inverse );
		Quat4 q = world.toQuat4();
		glUniform4f( GL.getUniform( "quats["+index+"]" ), q.x, q.y, q.z, q.w );
		glUniform3f( GL.getUniform( "trans["+index+"]" ), world.m[3], world.m[7], world.m[11] );
	}
	
	public void finish() {
		joint = Mat4.multiply( Mat4.translate( new Mat4(), trans ), orient );
		//joint = Mat4.inverse( joint );
		//joint.m[3] += trans.x;
		//joint.m[7] += trans.y;
		//joint.m[11] += trans.z;
		//skin = new Mat4( joint );
		if( parent != null && parent != tree.getRoot() ) {
			//skin = Mat4.multiply( parent.skin, skin );
			joint = Mat4.multiply( parent.joint, joint );
		}
	}
	
	public void update( double dt ) {
		Animation cAnimation = ((Skeleton)tree).getCurrentAnimation();
		
		skin = new Mat4( joint );
		if( cAnimation != null ) {
			Frame cFrame = cAnimation.getFrame( getId() );
			if( cFrame != null ) {
				skin = cFrame.getCurrentTransform();
				//Vec3 tmp = cFrame.getCurrentTranslation();
				//skin.m[3] += tmp.x;
				//skin.m[7] += tmp.y;
				//skin.m[11] += tmp.z;
			}
		}
		
		if( parent != null && parent != tree.getRoot() )
			skin = Mat4.multiply( parent.skin, skin );
		new RPauseDialog();
	}
}
