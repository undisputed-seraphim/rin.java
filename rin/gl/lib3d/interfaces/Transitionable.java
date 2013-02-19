package rin.gl.lib3d.interfaces;

import rin.gl.event.Transition;

public interface Transitionable {
	public void applyTransition( Transition t );
	public void onTransitionBegin( Transition t );
	public void onTransitionEnd( Transition t );
}
