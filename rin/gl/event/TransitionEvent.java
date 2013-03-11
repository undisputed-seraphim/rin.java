package rin.gl.event;

import rin.gl.lib3d.Actor;

public class TransitionEvent implements Runnable {
	public Transition<?> transition;
	public Actor getActor() { return this.transition.getActor(); }
	public TransitionEvent setTarget( Transition<?> t ) { this.transition = t; return this; }

	@Override public void run() {}
}
