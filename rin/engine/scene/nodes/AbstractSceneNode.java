package rin.engine.scene.nodes;

import rin.engine.util.AdvancedTreeNode;

public abstract class AbstractSceneNode extends AdvancedTreeNode<AbstractSceneNode> {
	
	public AbstractSceneNode( String id ) { super( id ); }
	
	public boolean accept( AbstractSceneNode node ) { return true; }
	public abstract void process( double dt );
	public abstract void destroy();
	
}
