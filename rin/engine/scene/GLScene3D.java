package rin.engine.scene;

import rin.engine.resource.FormatManager;
import rin.engine.resource.Resource;
import rin.engine.resource.model.ModelContainer;
import rin.engine.scene.nodes.AbstractSceneNode;
import rin.engine.scene.nodes.Camera;
import rin.engine.view.View;
import rin.util.math.Mat4;

public class GLScene3D extends SceneAdapter {

	@Override
	public void init() {
		System.out.println( "GLScene3D#init()" );
		setCamera( new Camera( Mat4.perspective( 45, View.DEFAULT_WIDTH / View.DEFAULT_HEIGHT, 0.1f, 15.0f ) ) );
		camera.setPosition( 0.0f, -1.0f, -11.0f );
		camera.setKeyboardControlled( true );
	}
	
	@Override
	public void destroy() {
		System.out.println( "GLScene3D#destroy()" );
		
		for( AbstractSceneNode asn : graph )
			asn.destroy();
		graph.clear();
	}
	
	public void addModel( Resource resource ) {
		ModelContainer mc = FormatManager.decodeModel( resource );
		if( mc.getGL() != null )
			graph.add( mc.getGL() );
	}
	
}
