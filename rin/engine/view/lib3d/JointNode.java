package rin.engine.view.lib3d;

import rin.util.math.Mat4;

public class JointNode extends SceneNode<JointNode> {
	
	private Mat4 joint;
	private Mat4 inverse;
	
	public JointNode( String id ) { super( id ); }
	@Override public JointNode actual() { return this; }
	
	public void setBindPoseMatrix( float[] m ) {
		
	}
	
	public void setInverseBindMatrix( float[] m ) {
		
	}
	
	public void update( double dt ) {
		// update the joint matrix of this joint
		System.out.println( "updating joint " + name );
	}
}
