package rin.engine.scene.lib2d;

import static org.lwjgl.opengl.GL20.glUniform1f;
import rin.engine.scene.nodes.AbstractSceneNode;
import rin.engine.view.gl.GL;

public class Layer extends AbstractSceneNode {

	private float z = 0;
	public Layer( String id, float zindex ) { super( id ); z = zindex; }
	
	@Override
	public boolean accept( AbstractSceneNode node ) {
		return true;
	}
	
	@Override
	public void process( double dt ) {
		glUniform1f( GL.getUniform( "zindex" ), z );
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
}
