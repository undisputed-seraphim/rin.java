package rin.gl.lib3d.interfaces;

public interface Boundable {
	
	public void setBounds( float[] vertices );
	
	public void createBoundingBox();
	
	public void showBoundingBox();
}
