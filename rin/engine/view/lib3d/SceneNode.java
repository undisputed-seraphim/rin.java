package rin.engine.view.lib3d;

import rin.engine.system.ident.AbstractNode;

public abstract class SceneNode<T extends AbstractNode<T>> extends AbstractNode<T> {

	public SceneNode( String id ) { super( id ); }
	
	public abstract void update( double dt );
	@Override public abstract T actual();
}
