package rin.engine.scene;

import rin.engine.scene.nodes.AbstractSceneNode;
import rin.engine.scene.nodes.Camera;
import rin.engine.util.AdvancedNodeTree;

public class SceneAdapter implements Scene {

	protected AdvancedNodeTree<AbstractSceneNode> graph = new AdvancedNodeTree<AbstractSceneNode>() {
		@Override public boolean accept( AbstractSceneNode node ) {
			return node.accept( node );
		}
	};
	
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
