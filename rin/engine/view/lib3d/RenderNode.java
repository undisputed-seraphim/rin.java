package rin.engine.view.lib3d;

public class RenderNode extends SceneNode<RenderNode> {
	
	protected float[] v = new float[0];
	protected float[] n = new float[0];
	protected float[] t = new float[0];
	
	protected int[] in = new int[0];
	
	public RenderNode( String id ) { super( id ); }
	@Override public RenderNode actual() { return this; }
	
	public void setVertices( float[] vertices ) { v = vertices; }
	public void setNormals( float[] normals ) { n = normals; }
	public void setTexcoords( float[] texcoords ) { t = texcoords; }
	public void setIndices( int[] indices ) { in = indices; }
	
	@Override
	public void update( double dt ) {}
	public void render() { System.out.println( "root render node" ); }
}
