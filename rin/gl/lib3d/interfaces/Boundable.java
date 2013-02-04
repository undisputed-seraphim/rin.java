package rin.gl.lib3d.interfaces;

import rin.gl.lib3d.shape.BoundingBox;

public interface Boundable {
	
	public boolean isBound();
	public void setBound( boolean val );
	
	public void computeBounds( float[] vertices );
	
	public BoundingBox getBoundingBox();
	public void createBoundingBox();
	public void showBoundingBox();
}
