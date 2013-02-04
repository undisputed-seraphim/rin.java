package rin.gl.lib3d.interfaces;

public interface Renderable {
	
	/** Returns whether or not the Renderable is ready for rendering. */
	public boolean isReady();
	public void useUniqueColor( boolean val );
	
	public int getIndexCount();
	
	public int getRenderMode();
	public void setRenderMode( int renderMode );
	
	/** Creates an opengl Texture resource to associate with this Renderable
	 * @param textureFile absolute filename of desired texture
	 */
	public void addTexture( String textureFile );
	
	/** Binds the texture that was added for this Renderable, if needed. */
	public void bindTexture();
	
	/** Construct a Renderable with the given vertices, normals, and texture coordinates.
	 * @param vertices array of vertex coordinates
	 * @param normals array of normal coordinates
	 * @param texcoords array of texture coordinates
	 */
	public void build( float[] vertices, float[] normals, float[] texcoords );
	
	public boolean buffer();
	public void render();
}
