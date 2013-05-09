package rin.engine.view;

import rin.engine.resource.image.ImageContainer;

public interface View {

	/**
	 * Initialize anything having to deal with this view. After this call,
	 * the view should be ready for {@link #show(int, int)}.
	 */
	public void init();
	
	/**
	 * Display the graphical representation of this view.
	 * @param width width of graphical display
	 * @param height height of graphical display
	 */
	public void show( int width, int height );
	
	public Object loadTexture( ImageContainer image );
	public RenderData createRenderData( Renderable entity );
	public void render( Renderable entity );
	
}
