package rin.gl.lib3d;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;

import java.util.ArrayList;

import rin.engine.resource.image.ImageContainer;
import rin.gl.GL;
import rin.gl.TextureManager;
import rin.gl.lib3d.data.GLBuffer;
import rin.util.math.Mat4;

public class SkinNode extends Node {

	private ArrayList<JointNode> joints = new ArrayList<JointNode>();
	public ArrayList<JointNode> getJoints() { return joints; }
	
	private int tex = -1;
	
	private GLBuffer vbuf;
	private float[] v = new float[0];
	
	private GLBuffer nbuf;
	private float[] n = new float[0];
	
	private GLBuffer tbuf;
	private float[] t = new float[0];
	
	private GLBuffer bbuf;
	private float[] b = new float[0];
	
	private GLBuffer wbuf;
	private float[] w = new float[0];
	
	private GLBuffer ibuf;
	private int[] i = new int[0];
	
	private boolean ready;
	
	public SkinNode( String name ) {
		super( name );
	}
	
	public void setVertices( float[] vertices ) {
		v = vertices;
	}
	
	public void setTexcoords( float[] texcoords ) {
		t = texcoords;
	}
	
	public void setBoneIndices( short[] bi ) {
		b = new float[bi.length];
		for( int j = 0; j < bi.length; j++ )
			b[j] = bi[j];
	}
	
	public void setBoneWeights( float[] weights ) {
		w = weights;
	}
	
	public void setIndices( short[] s ) {
		i = new int[s.length];
		for( int j = 0; j < s.length; j++ )
			i[j] = s[j];
	}
	
	public void build() {
		vbuf = new GLBuffer( GL_ARRAY_BUFFER, v, 3, 0, 0, GL.getAttrib( "vertex" ) );
		if( t.length > 0 ) tbuf = new GLBuffer( GL_ARRAY_BUFFER, t, 3, 0, 0, GL.getAttrib( "texture" ) );
		if( b.length > 0 ) {
			bbuf = new GLBuffer( GL_ARRAY_BUFFER, b, 4, 0, 0, GL.getAttrib( "bone" ) );
			wbuf = new GLBuffer( GL_ARRAY_BUFFER, w, 4, 0, 0, GL.getAttrib( "weight" ) );
		}
		ibuf = new GLBuffer( GL_ELEMENT_ARRAY_BUFFER, i );
		ready = true;
	}
	
	public void addJoint( JointNode joint ) {
		joints.add( joint );
	}
	
	public void setTexture( ImageContainer ic ) {
		tex = TextureManager.load( ic.getName(), ic.getWidth(), ic.getHeight(), ic.getFormat().getStride(), ic.getData() );
	}
	
	public void setTexture( int texId ) {
		tex = texId;
	}
	
	public boolean buffer() {
		boolean valid = true;
		valid &= vbuf.buffer();
		if( tbuf != null ) valid &= tbuf.buffer();
		if( bbuf != null ) valid &= bbuf.buffer();
		if( wbuf != null ) valid &= wbuf.buffer();
		return valid;
	}
	
	@Override
	public void update( double dt ) {
		super.update( dt );
		/* joints have already updated their positions, so render the joints */
	}
	
	@Override
	public void render() {
		for( int i = 0; i < joints.size(); i++ ) {
			joints.get( i ).applyBone( i, transform );
		}
		
		glUniformMatrix4( GL.getUniform( "mMatrix"), false, scene.getMatrix().gl() );
		if( buffer() ) {
			//System.out.println( "hi" + " " + v.length + " " + i.length );
			TextureManager.enable( tex );
			ibuf.render();
		}
	}
}
