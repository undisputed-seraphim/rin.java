package rin.engine.view.gl;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;

import java.util.HashMap;

import rin.engine.view.lib3d.JointNode;
import rin.gl.GL;
import rin.gl.lib3d.data.GLBuffer;

public class GLSkinNode extends GLRenderNode {

	protected GLBuffer bbuf;
	protected GLBuffer wbuf;
	
	protected float[] b = new float[0];
	protected float[] w = new float[0];
	
	private HashMap<String, JointNode> joints = new HashMap<String, JointNode>();
	private HashMap<String, Integer> jointIndices = new HashMap<String, Integer>();
	
	public GLSkinNode( String id ) { super( id ); }
	@Override public GLSkinNode actual() { return this; }
	
	public void setBoneIndices( float[] bones ) { b = bones; }
	public void setBoneWeights( float[] weights ) { w = weights; }
	
	public void addJoint( JointNode joint, int index ) {
		if( !joints.containsKey( joint.getId() ) ) {
			joints.put( joint.getId(), joint );
			jointIndices.put( joint.getId(), index );
		}
	}
	
	@Override
	public boolean build() {
		super.build();
		
		if( b.length > 0 )
			bbuf = new GLBuffer( GL_ARRAY_BUFFER, b, 4, 0, 0, GL.getAttrib( "bone" ) );
		
		if( w.length > 0 )
			wbuf = new GLBuffer( GL_ARRAY_BUFFER, w, 4, 0, 0, GL.getAttrib( "weight" ) );
		
		return true;
	}
	
	@Override
	public boolean buffer() {
		boolean valid = super.buffer();
		if( bbuf != null ) valid &= bbuf.buffer();
		if( wbuf != null ) valid &= wbuf.buffer();
		return valid;
	}
	
	@Override
	public void render() {
		for( JointNode jn : joints.values() )
			jn.applyBone( jointIndices.get( jn.getId() ) );
		bindTexture();
		ibuf.render();
	}
}
