package rin.engine.view.gl;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import rin.engine.view.lib3d.RenderNode;
import rin.gl.GL;
import rin.gl.TextureManager;
import rin.gl.lib3d.data.GLBuffer;

public class GLRenderNode extends RenderNode {

	protected int tex = -1;
	
	protected GLBuffer vbuf;
	protected GLBuffer tbuf;
	
	protected GLBuffer ibuf;
	public GLRenderNode( String id ) { super( id ); }
	
	@Override
	public GLRenderNode actual() { return this; }

	public void bindTexture() { TextureManager.enable( tex ); }
	
	@Override
	public boolean build() {
		if( v.length > 0 )
			vbuf = new GLBuffer( GL_ARRAY_BUFFER, v, 3, 0, 0, GL.getAttrib( "vertex" ) );
		
		if( t.length > 0 )
			tbuf = new GLBuffer( GL_ARRAY_BUFFER, t, 2, 0, 0, GL.getAttrib( "texture" ) );
		
		if( in.length > 0 )
			ibuf = new GLBuffer( GL_ELEMENT_ARRAY_BUFFER, in );
		
		if( texture != null )
			tex = TextureManager.load( texture );
		
		return true;
	}
	
	@Override
	public boolean buffer() {
		boolean valid = true;
		if( vbuf != null ) valid &= vbuf.buffer();
		if( tbuf != null ) valid &= tbuf.buffer();
		if( ibuf != null ) valid &= ibuf.buffer();
		return valid;
	}
	
	@Override
	public void render() {
		System.out.println( "rendering with opengl " + name );
	}
}
