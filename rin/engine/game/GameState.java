package rin.engine.game;

public abstract class GameState {
	private boolean pushed = false;
	
	public void push() {
		while( !pushed )
			run();
	}
	
	public abstract void run();
	
	public void pop() {
		pushed = true;
	}
}
