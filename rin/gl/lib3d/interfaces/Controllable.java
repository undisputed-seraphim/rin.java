package rin.gl.lib3d.interfaces;

import rin.gl.event.GLEventListener.*;

/** Grants an object the capability to be controlled via keyboard and/or mouse. */
public interface Controllable extends KeyEventListener, MouseEventListener {
	
	public boolean isKeyboardControlled();
	public boolean isMouseControlled();
	
	public void setKeyboardControlled( boolean val );
	public void setMouseControlled( boolean val );
	
}
