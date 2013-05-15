package rin.engine.view.gl;

import rin.engine.view.lib3d.RenderNode;

public class GLRenderNode extends RenderNode {

	public GLRenderNode( String id ) { super( id ); }
	
	@Override
	public GLRenderNode actual() { return this; }

	@Override
	public void render() {
		System.out.println( "rendering with opengl " + name );
	}
}
