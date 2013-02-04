package rin.gl.lib3d.interfaces;

import rin.gl.event.GLEventListener.*;

public interface Pickable extends PickEventListener {
	
	public boolean isPicking();
	public void setPicking( boolean val );
	
	public float[] getUniqueColor();
	
}
