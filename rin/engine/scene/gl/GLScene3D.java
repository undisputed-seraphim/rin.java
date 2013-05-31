package rin.engine.scene.gl;

import java.util.ArrayList;

import rin.engine.resource.Resource;
import rin.engine.scene.nodes.AbstractSceneNode;
import rin.engine.util.NodeTree;

public class GLScene3D extends GLScene {

	private NodeTree<AbstractSceneNode> graph = new NodeTree<AbstractSceneNode>();
	private ArrayList<AbstractSceneNode> secondPass = new ArrayList<AbstractSceneNode>();

	@Override
	public void process() {
		for( AbstractSceneNode asn : graph ) {
			if( asn.isSecondPass() )
				secondPass.add( asn );
			else asn.process();
		}
		
		for( AbstractSceneNode asn : secondPass )
			asn.process();
		secondPass.clear();
	}

	@Override
	public void destroy() {
		System.out.println( "GLScene3D#destroy()" );
		super.destroy();
		
		for( AbstractSceneNode asn : graph )
			asn.destroy();
		graph.clear();
		secondPass.clear();
	}
	
	public void addModel( Resource resource ) {
		
	}
	
}
