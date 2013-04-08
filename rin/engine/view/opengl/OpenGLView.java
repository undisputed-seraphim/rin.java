package rin.engine.view.opengl;

import rin.engine.view.Renderable;
import rin.engine.view.View;
import rin.engine.view.opengl.data.OpenGLRenderData;

public class OpenGLView implements View {

	@Override
	public void init() {}

	@Override
	public void show( int width, int height ) {}
	
	@Override
	public OpenGLRenderData createRenderData( Renderable entity ) {
		return null;
	}

	@Override
	public void render( Renderable entity ) {}

}
