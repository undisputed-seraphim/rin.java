package rin.engine.core.render;

public interface Renderer {

	public void init();
	public void destroy();
	
	public void render( Renderable r );
	
}
