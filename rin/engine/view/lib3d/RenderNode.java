package rin.engine.view.lib3d;

import rin.engine.resource.image.ImageContainer;

public class RenderNode extends SceneNode<RenderNode> {
	
	protected ImageContainer texture;
	
	protected float[] v = new float[0];
	protected float[] n = new float[0];
	protected float[] t = new float[0];
	
	protected int[] in = new int[0];
	
	private boolean ready = false;
	
	public RenderNode( String id ) { super( id ); }
	@Override public RenderNode actual() { return this; }
	
	public void setTexture( ImageContainer ic ) { texture = ic; }
	public void setVertices( float[] vertices ) { v = vertices; }
	public void setNormals( float[] normals ) { n = normals; }
	public void setTexcoords( float[] texcoords ) { t = texcoords; }
	public void setIndices( int[] indices ) { in = indices; }
	
	@Override
	public void update( double dt ) {}

	public void process() {
		if( !ready ) ready = build();
		
		if( ready ) {
			if( v.length > 0 || in.length > 0 )
				if( buffer() )
					if( in.length > 0 )
						render();
			
			for( RenderNode rn : children )
				rn.process();
		}
	}
	
	public boolean build() { return true; }
	public boolean buffer() { return true; }
	public void render() {}
}
