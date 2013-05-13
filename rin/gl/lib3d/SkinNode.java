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

public class SkinNode extends Node {

	private ArrayList<JointNode> joints = new ArrayList<JointNode>();
	private int tex = -1;
	
	private float[] v = new float[0];
	private float[] n = new float[0];
	private float[] t = new float[0];
	
	private int[] i = new int[0];
	private GLBuffer ibuf;
	private GLBuffer tbuf;
	private GLBuffer vbuf;
	
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
	
	public void setIndices( short[] s ) {
		i = new int[s.length];
		for( int j = 0; j < s.length; j++ )
			i[j] = s[j];
	}
	
	public void build() {
		vbuf = new GLBuffer( GL_ARRAY_BUFFER, v, 3, 0, 0, GL.getAttrib( "vertex" ) );
		if( t.length > 0 ) tbuf = new GLBuffer( GL_ARRAY_BUFFER, t, 3, 0, 0, GL.getAttrib( "texture" ) );
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
	
	@Override
	public boolean buffer() {
		vbuf.buffer();
		if( tbuf != null ) tbuf.buffer();
		return true;
	}
	
	@Override
	public void update( double dt ) {
		/* joints have already updated their positions, so render the joints */
		glUniformMatrix4( GL.getUniform( "mMatrix"), false, this.getMatrix().gl() );
		if( buffer() ) {
			//System.out.println( "hi" + " " + v.length + " " + i.length );
			TextureManager.enable( tex );
			ibuf.render();
		}
		
		/*System.out.println( "SKINNODE " + name + " controls the following joints: " );
		for( JointNode n : joints ) {
			System.out.println( n.name );
		}*/
	}
}
