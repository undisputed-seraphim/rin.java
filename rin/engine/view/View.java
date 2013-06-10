package rin.engine.view;

import rin.engine.scene.Scene;

public interface View {
	
	public static final int DEFAULT_WIDTH = 900;
	public static final int DEFAULT_HEIGHT = 500;
	
	public Scene getScene();
	
	public int getWidth();
	public int getHeight();
	public void setSize( int width, int height );
	
	public void init();
	public void show();
	public void destroy();

}
