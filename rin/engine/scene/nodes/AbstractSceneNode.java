package rin.engine.scene.nodes;

import rin.engine.util.TreeNode;

public abstract class AbstractSceneNode extends TreeNode<AbstractSceneNode> {

	private boolean second = false;
	
	public AbstractSceneNode( String id ) { super( id ); }
	
	public abstract void process();
	public abstract void destroy();
	
	public boolean isSecondPass() { return second; }
	public void setSecondPass( boolean val ) { second = val; }
}
