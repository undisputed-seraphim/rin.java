package rin.gl.lib3d.interfaces;

public interface Boundable {
	
	public boolean isBound();
	public void setBound( boolean val );
	public void computeBounds( float[] vertices );
	
	public void createBoundingBox();
	public void showBoundingBox();
}
