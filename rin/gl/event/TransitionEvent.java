package rin.gl.event;

public class TransitionEvent implements Runnable {
	public Transition<?> transition;
	public TransitionEvent setTarget( Transition<?> t ) { this.transition = t; return this; }
	
	@Override public void run() {}
}
