package rin.engine.scene.nodes;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import rin.engine.resource.image.ImageContainer;
import rin.engine.view.gl.GL;
import rin.engine.view.gl.TextureManager;
import rin.gl.lib3d.data.GLBuffer;

public class RenderNode extends AbstractSceneNode {

	protected ImageContainer texture;
	protected int tex = -1;
	
	protected float[] v = new float[0];
	protected float[] n = new float[0];
	protected float[] t = new float[0];
	protected GLBuffer vbuf;
	protected GLBuffer nbuf;
	protected GLBuffer tbuf;
	
	protected int[] in = new int[0];
	protected GLBuffer ibuf;

	private boolean ready = false;
	private boolean built = false;
	
	public RenderNode( String id ) { super( id ); }
	
	public void setTexture( ImageContainer ic ) { texture = ic; }
	public void setTextureId( int id ) { tex = id; }
	
	public void setVertices( float[] vertices ) { v = vertices; }
	public void setNormals( float[] normals ) { n = normals; }
	public void setTexcoords( float[] texcoords ) { t = texcoords; }
	public void setIndices( int[] indices ) { in = indices; }
	
	public void bindTexture() { TextureManager.enable( tex ); }
	
	public boolean build() {
		if( v.length > 0 )
			vbuf = new GLBuffer( GL_ARRAY_BUFFER, v, 3, 0, 0, GL.getAttribute( "vertex" ) );
		
		if( n.length > 0 )
			nbuf = new GLBuffer( GL_ARRAY_BUFFER, n, 3, 0, 0, GL.getAttribute( "normal" ) );
		
		if( t.length > 0 )
			tbuf = new GLBuffer( GL_ARRAY_BUFFER, t, 2, 0, 0, GL.getAttribute( "texture" ) );
		
		if( in.length > 0 )
			ibuf = new GLBuffer( GL_ELEMENT_ARRAY_BUFFER, in );
		
		if( texture != null )
			tex = TextureManager.load( texture );
		
		built = true;
		return true;
	}
	
	@Override
	public boolean accept( AbstractSceneNode node ) {
		return true;
	}
	
	@Override
	public void process( double dt ) {
		if( !built ) ready = build();
		render();
	}
	
	public boolean buffer() {
		boolean valid = true;
		if( vbuf != null ) valid &= vbuf.buffer();
		if( nbuf != null ) valid &= nbuf.buffer();
		if( tbuf != null ) valid &= tbuf.buffer();
		return valid;
	}
	
	public void render() {
		if( ready ) {
			if( v.length > 0 || in.length > 0 )
				if( buffer() )
					if( in.length > 0 ) {
						bindTexture();
						ibuf.render();
					}
		}
	}
	
	@Override
	public void destroy() {
		if( vbuf != null ) vbuf.destroy();
		if( nbuf != null ) nbuf.destroy();
		if( tbuf != null ) tbuf.destroy();
		if( tex != -1 ) TextureManager.unload( tex );
	}
	
}
