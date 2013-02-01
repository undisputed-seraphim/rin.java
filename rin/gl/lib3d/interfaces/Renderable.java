package rin.gl.lib3d.interfaces;

public interface Renderable {
	
	public boolean isReady();
	public int getRenderMode();
	
	public void setVertices( float[] vertices );
	public void setNormals( float[] normals );
	public void setTexcoords( float[] texcoords );
	
	/**
	 * Initializes all vertex and texture data. After this call, the object should
	 *  be prepared for rendering.
	 */
	public void init();
	
	public boolean buffer();
	public void render();
}
