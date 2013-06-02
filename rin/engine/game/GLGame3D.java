package rin.engine.game;

import rin.engine.view.GLView3D;

public class GLGame3D extends GameAdapter {

	private boolean running = false;
	
	@Override
	public void init() {
		System.out.println( "GLGame3D#init()" );
		setView( new GLView3D() );
	}
	
	@Override public GLView3D getView() { return (GLView3D)view; }
	
	@Override
	public void start() {
		running = true;
		getView().show();
	}
	
	@Override
	public void destroy() {
		System.out.println( "GLGame3D#destroy()" );
		running = false;
		if( view != null ) view.destroy();
	}
	
}
