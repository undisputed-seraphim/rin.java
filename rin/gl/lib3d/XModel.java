package rin.gl.lib3d;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import java.util.ArrayList;

import rin.gl.GL;
import rin.gl.TextureManager;
import rin.gl.lib3d.data.GLBuffer;

public class XModel extends Poly {
	
	private ArrayList<GLBuffer> indices = new ArrayList<GLBuffer>();
	private ArrayList<Integer> textures = new ArrayList<Integer>();

	public void addPoly( int[] i, int texture ) {
		indices.add( new GLBuffer( GL_ELEMENT_ARRAY_BUFFER, i ) );
		System.out.println( i.length );
		textures.add( texture );
	}
	
	private boolean ready = false;
	private GLBuffer vbuf;
	private GLBuffer nbuf;
	private GLBuffer tbuf;
	
	public void build( float[] v, float[] n, float[] t ) {
		vbuf = new GLBuffer( GL_ARRAY_BUFFER, v, 3, 0, 0, GL.getAttrib( "vertex" ) );
		nbuf = new GLBuffer( GL_ARRAY_BUFFER, n, 3, 0, 0, GL.getAttrib( "normal" ) );
		tbuf = new GLBuffer( GL_ARRAY_BUFFER, t, 2, 0, 0, GL.getAttrib( "texture" ) );
		ready = true;
	}
	
	@Override
	public boolean buffer() {
		vbuf.buffer();
		nbuf.buffer();
		tbuf.buffer();
		return true;
	}
	
	@Override
	public void render( boolean unique ) {
		if( ready ) {
			//while( true ) {
				glUniformMatrix4( GL.getUniform( "mMatrix"), false, this.getMatrix().gl() );
				if( buffer() ) {
					for( int i = 0; i < indices.size(); i++ ) {
						glUniform1i( GL.getUniform( "useColor" ), GL_FALSE );
						TextureManager.enable( textures.get( i ) );
						indices.get( i ).render();
					}
				}
			//}
		}
	}
}
