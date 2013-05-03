package rin.engine.view;

import rin.engine.resource.image.ImageContainer;

public interface View {

	public void init();
	public void show( int width, int height );
	
	public Object loadTexture( ImageContainer image );
	public RenderData createRenderData( Renderable entity );
	public void render( Renderable entity );
	
}
