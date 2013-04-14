package rin.sample;

import rin.engine.view.opengl.OpenGLView;

public class MyGame implements rin.engine.game.Game {

	@Override
	public String getName() { return "Rin Tactics"; }
	
	@Override
	public void preload() {
		
	}
	
	@Override
	public OpenGLView getView() {
		return new OpenGLView();
	}
	
	@Override
	public void initGUI() {
		
	}
	
	@Override
	public void load() {
		
	}
	
	@Override
	public void run() {
		
	}
	
	@Override
	public void destroy() {
		
	}
	
}
