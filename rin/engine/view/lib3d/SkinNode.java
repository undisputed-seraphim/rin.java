package rin.engine.view.lib3d;

import java.util.ArrayList;

public class SkinNode extends RenderNode {
	
	protected float[] b = new float[0];
	protected float[] w = new float[0];
	private ArrayList<JointNode> joints = new ArrayList<JointNode>();
	
	public SkinNode( String id ) { super( id ); }
	@Override public SkinNode actual() { return this; }
	
	public void setBoneIndices( float[] bones ) { b = bones; }
	public void setBoneWeights( float[] weights ) { w = weights; }
	
	public void addJoint( JointNode jn ) { joints.add( jn ); }
	
	@Override
	public void update( double dt ) {
		
	}
	
	@Override
	public void render() {}
}
