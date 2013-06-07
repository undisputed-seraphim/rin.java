package rin.engine.scene;

import rin.engine.scene.lib2d.AnimatedSprite;
import rin.engine.scene.lib2d.Layer;
import rin.engine.scene.lib2d.Sprite;
import rin.engine.scene.lib2d.SpriteMap;
import rin.engine.scene.nodes.AbstractSceneNode;
import rin.engine.scene.nodes.Camera;
import rin.util.math.Mat4;

public class GLScene2D extends SceneAdapter {
	
	@Override
	public void init() {
		System.out.println( "GLScene2D#init()" );
		setCamera( new Camera( Mat4.orthographic( -450, 450, -250, 250, 1, -10 ) ) );
		camera.setPosition( 0.0f, 0.0f, 2.0f );
		camera.setKeyboardControlled( true );
		
		graph.add( new Layer( "LAYER1", 0.0f ) );
		graph.add( new Layer( "LAYER0", 0.0f ) );
		
	}
	
	@Override
	public void destroy() {
		System.out.println( "GLScene2D#destroy()" );
		
		for( AbstractSceneNode asn : graph )
			asn.destroy();
		graph.clear();
	}
	
	public Sprite addSprite( int layer, SpriteMap sm ) {
		Sprite spr = new AnimatedSprite( "test", sm );
		graph.find( "LAYER"+layer ).add( spr );
		return spr;
	}
	
}
