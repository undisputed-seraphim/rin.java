package rin.system.interfaces;

import rin.engine.Engine;

public abstract class State {

	public void onEnter() {}
	
	public abstract void inside();
	
	public void onExit() {}
	
	public final void push() {
		Engine.getStateController().add( this );
	}
	
	public final void pop() {
		
	}
	
}
