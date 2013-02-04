package rin.gl.lib3d.interfaces;

public interface Renderable {
	
	/** Returns whether or not the Renderable is ready for rendering. */
	public boolean isReady();

	public boolean isUsingUnique();
	public void useUniqueColor( boolean val );
	
	public int getIndexCount();
	
	public boolean isVisible();
	public void setVisible( boolean val );
	
	public int getRenderMode();
	public void setRenderMode( int renderMode );
	
	public boolean isColored();
	public void setColored( boolean val );
	public void setColor( float r, float g, float b, float a );
	public float[] getColor();
	
	/** Creates an opengl Texture resource to associate with this Renderable
	 * @param textureFile absolute filename of desired texture
	 * @return int opengl texture id
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
