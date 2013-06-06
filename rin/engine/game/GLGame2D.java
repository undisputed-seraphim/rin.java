package rin.engine.game;

import rin.engine.scene.GLScene2D;
import rin.engine.view.GLView2D;

public class GLGame2D extends GameAdapter {

	private boolean running = false;
	
	@Override
	public void init() {
		System.out.println( "GLGame2D#init()" );
		setView( new GLView2D() );
	}
	
	@Override public GLView2D getView() { return (GLView2D)view; }
	public GLScene2D getScene() { return getView().getScene(); }
	
	@Override
	public void start() {
		running = true;
		getView().show();
	}
	
	@Override
	public void destroy() {
		System.out.println( "GLGame2D#destroy()" );
		running = false;
		if( view != null ) view.destroy();
	}
	
}
