package rin.engine.scene.nodes;

import rin.engine.util.TreeNode;

public abstract class AbstractSceneNode extends TreeNode<AbstractSceneNode> {
	
	public AbstractSceneNode( String id ) { super( id ); }
	
	public abstract void process( double dt );
	public abstract void destroy();
	
}
