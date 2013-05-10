package rin.engine.game;

import rin.engine.view.View;

public abstract class Game<T extends View> implements Runnable {
	private T view;
	
	public void setView( T v ) { view = v; }
	public T getView() { return view; }
	
	public abstract String getName();
	public abstract T getViewInstance();
	public abstract void init();
	public abstract void start();
	
	@Override
	public final void run() {
		setView( getViewInstance() );
		view.init();
		init();
		view.show();
		view.setTitle( getName() );
		start();
		destroy();
	}
	
	public abstract void destroy();
}
