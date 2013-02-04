package rin.gl.lib3d.interfaces;

import rin.gl.event.GLEventListener.*;

/** Grants an object the capability to be controlled via keyboard and/or mouse. */
public interface Controllable extends KeyEventListener, MouseEventListener {
	
	public boolean isControlled();
	public void setControlled( boolean val );
	
}
