package rin.engine.game;

import rin.engine.view.View;

public interface Game {

	public String getName();
	public void preload();
	public View getView();
	public void initGUI();
	public void load();
	public void run();
	public void destroy();
	
}
