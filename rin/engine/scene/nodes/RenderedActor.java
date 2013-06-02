package rin.engine.scene.nodes;

import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import rin.engine.view.gl.GL;

public class RenderedActor extends Actor {

	public RenderedActor( String id ) { super( id ); }
	
	public void process( double dt ) {
		super.process( dt );
		render();
	}
	
	public void render() {
		glUniformMatrix4( GL.getUniform( "mMatrix" ), false, getTransform().gl() );
	}
	
}
