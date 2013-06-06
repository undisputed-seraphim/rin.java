package rin.engine.scene.lib2d;

import rin.engine.resource.Resource;
import rin.engine.scene.nodes.RenderedActor;
import rin.engine.view.gl.TextureManager;

public class Sprite extends RenderedActor {

	public Sprite( String id, Resource res ) {
		super( id );
		test( res );
	}
	
	public void test( Resource res ) {
		float w = 5.0f;
		float h = 7.0f;
		
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
		rn.setTextureId( TextureManager.load( res.getPath() ) );
		
		add( rn );
	}
	
}
