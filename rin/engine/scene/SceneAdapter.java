package rin.engine.scene;

import rin.engine.scene.nodes.AbstractSceneNode;
import rin.engine.scene.nodes.Camera;
import rin.engine.util.CachedNodeTree;

public class SceneAdapter implements Scene {

	protected CachedNodeTree<AbstractSceneNode> graph = new CachedNodeTree<AbstractSceneNode>();
	protected Camera camera;
	
	@Override public Camera getCamera() { return camera; }
	public void setCamera( Camera c ) {
		camera = c;
	}
	
	@Override public void init() {}
	
	@Override
	public void process( double dt ) {
		camera.process( dt );
		
		for( AbstractSceneNode asn : graph )
			asn.process( dt );
	}
	
	@Override public void destroy() {}

}
