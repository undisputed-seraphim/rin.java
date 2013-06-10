package rin.engine.core.game;

import rin.engine.core.scene.Scene;
import rin.engine.core.view.View;

public interface Game extends Runnable {

	public View getView();
	public void setView( View v );
	
	public Scene getScene();
	public void setScene( Scene s );
	
	public void init();
	public void start();
	public void destroy();
	
	@Override public void run();
	
}
