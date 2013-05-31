package rin.engine.game;

import rin.engine.scene.Scene;

public interface Game extends Runnable {
	public void init();
	public void setSize( int width, int height );
	public Scene getScene();
	public void start();
	public void stop();
	public void destroy();
}
