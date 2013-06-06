package rin.engine.scene;

import rin.engine.resource.Resource;
import rin.engine.scene.lib2d.Layer;
import rin.engine.scene.lib2d.Sprite;
import rin.engine.scene.nodes.AbstractSceneNode;
import rin.engine.scene.nodes.Camera;
import rin.util.math.Mat4;

public class GLScene2D extends SceneAdapter {
	
	@Override
	public void init() {
		System.out.println( "GLScene2D#init()" );
		setCamera( new Camera( Mat4.orthographic( -25, 25, -20, 20, 1, -10 ) ) );
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
	
	public void addSprite( int layer, Resource res ) {
		Sprite spr = new Sprite( "test", res );
		graph.find( "LAYER"+layer ).add( spr );
	}
	
}
