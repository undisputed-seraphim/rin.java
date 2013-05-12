package rin.engine.view.scene;

import rin.engine.view.gl.GLRenderData;

public class RenderNode extends Node {
	private GLRenderData data;
	public RenderNode( Mesh m ) {
		super( m );
		data = new GLRenderData( m );
	}
	
	@Override
	public void update( long dt ) {
		super.update( dt );
		render();
	}
	
	public void render() {
	}
}
