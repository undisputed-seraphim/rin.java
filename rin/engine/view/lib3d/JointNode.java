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
	
	public JointNode( String id ) { super( id ); }
	@Override public JointNode actual() { return this; }
	
	public void setJointMatrix( float[] m ) { joint = new Mat4( m ); }
	public void setJointTranslation( float[] t ) { joint = Mat4.multiply( Mat4.translate( new Mat4(), new Vec3( t[0], t[1], t[2] ) ), joint ); }
	public void setInverseBindMatrix( float[] m ) { inverse = new Mat4( m ); }
	
	public void applyBone( int index ) {
		skin = Mat4.multiply( world, inverse );
		Quat4 q = skin.toQuat4();
		Vec3 t = new Vec3( skin.m[3], skin.m[7], skin.m[11] );
		glUniform4f( GL.getUniform( "quats["+index+"]" ), q.x, q.y, q.z, q.w );
		glUniform3f( GL.getUniform( "trans["+index+"]" ), t.x, t.y, t.z );
	}
	
	public void update( double dt ) {
		if( parent != tree.getRoot() && parent != null )
			world = Mat4.multiply( parent.world, joint );
		else world = joint;
	}
}
