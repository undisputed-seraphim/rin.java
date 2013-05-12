package rin.engine.game;

import rin.engine.view.View;
import rin.engine.view.scene.Scene;

public abstract class Game implements Runnable {
	private View view;
	private Scene scene;
	
	public void setView( View v ) { view = v; }
	public View getView() { return view; }
	
	public void setScene( Scene s ) { scene = s; }
	public Scene getScene() { return scene; }
	
	public abstract void init();
	public abstract void start();
	
	@Override
	public final void run() {
		setView( new View() );
		setScene( new Scene( getView() ) );
		view.init();
		init();
		start();
		view.destroy();
		destroy();
	}
	
	public abstract void destroy();
}
