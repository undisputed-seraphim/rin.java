package rin.engine.view;

public interface View {

	public void init();
	public void show( int width, int height );
	public RenderData createRenderData( Renderable entity );
	public void render( Renderable entity );
	
}
