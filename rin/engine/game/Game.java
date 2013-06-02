package rin.engine.game;

import rin.engine.view.View;

public interface Game extends Runnable {
	
	public View getView();
	
	public void init();
	public void start();
	public void stop();
	public void destroy();

	@Override public void run();
}
