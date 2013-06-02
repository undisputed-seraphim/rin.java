package rin.engine.view;

import rin.engine.scene.Scene;

public class ViewAdapter implements View {

	protected Scene scene;
	
	protected int width = View.DEFAULT_WIDTH;
	protected int height = View.DEFAULT_HEIGHT;
	
	@Override public Scene getScene() { return scene; }
	public void setScene( Scene s ) {
		if( scene != null )
			scene.destroy();
		scene = s;
		scene.init();
	}
	
	@Override public int getWidth() { return width; }
	@Override public int getHeight() { return height; }
	@Override public void setSize( int w, int h ) {}
	
	@Override public void init() {}
	@Override public void show() {}
	@Override public void destroy() {}
}
