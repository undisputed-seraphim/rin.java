package rin.engine.scene.lib2d;

import rin.engine.resource.Resource;
import rin.engine.scene.nodes.RenderedActor;
import rin.engine.view.gl.TextureManager;

public class Sprite extends RenderedActor {

	protected SpriteMap _map;
	private int w;
	private int h;
	
	protected int tex = -1;
	
	protected boolean built = false;
	protected boolean ready = false;
	
	public Sprite( String id, Resource res ) {
		super( id );
		
	}
	
	public Sprite( String id, SpriteMap map ) {
		super( id );
		_map = map;
		
		tex = TextureManager.load( _map.getImage().getPath() );
		build();
	}
	
	public void build() {
		float w = _map.getSpriteWidth();
		float h = _map.getSpriteHeight();
		
		float[] v = new float[] {
				0.0f, 0.0f,
				w, 0.0f,
				w, h,
				0.0f, h
		};
		
		float[] t = new float[] {
				0.0f, 1.0f,
				1.0f, 1.0f,
				1.0f, 0.0f,
				0.0f, 0.0f
		};
		
		int[] i = new int[] {
				0, 1, 2,
				0, 2, 3
		};
		
		RenderNode2D rn = new RenderNode2D( "test" );
		rn.setVertices( v );
		rn.setTexcoords( t );
		rn.setIndices( i );
		rn.setTextureId( tex );
		
		add( rn );
	}
	
}
