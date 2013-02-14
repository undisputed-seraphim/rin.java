package rin.gl.lib3d.interfaces;

import rin.gl.event.GLEventListener.AnimationEventListener;

public interface Animatable extends AnimationEventListener {

	public boolean isAnimatable();
	public void setAnimatable( boolean val );
	
}
