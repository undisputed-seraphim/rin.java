package rin.engine.lib.gl;

import rin.engine.view.View;

public class GLView implements View {
	private GLCanvas canvas;
	private int w = 500;
	private int h = 500;
	
	@Override
	public void init() { canvas = new GLCanvas(); }

	@Override
	public void setSize( int width, int height ) {
		canvas.resize( width, height );
		w = width;
		h = height;
	}
	
	@Override
	public void show() {
		setSize( w, h );
		canvas.show();
	}
	
	@Override
	public void update() { canvas.update(); }
	
	@Override
	public boolean isClosed() { return canvas.isClosed(); }
	
	@Override
	public void setTitle( String title ) { canvas.setTitle( title ); }

	@Override
	public void destroy() {}
}
