package rin.engine.util;

import java.util.ArrayList;

public abstract class AbstractNode<T extends AbstractNode<T>> extends AbstractNodeContainer<T> {
	protected AbstractNodeContainer<T> parent;
	protected AbstractNodeContainer<T> container;
	public AbstractNodeContainer<T> getParent() { return parent; }
	
	public abstract String getId();
	protected abstract T actual();
	
	private void findContainer() {
		container = parent;
		while( container instanceof AbstractNode )
			container = ((AbstractNode<T>)container).parent;
	}
	
	public void cache( T node ) {
		if( container != null )
			container.cache( node );
	}
	
	public void discard( T node ) {
		if( container != null )
			container.discard( node );
	}
	
	public T add( T node ) {
		findContainer();
		node.parent = this;
		nodes.add( node );
		cache( node );
		return nodes.get( nodes.size() - 1 );
	}
	
	public boolean remove( T node ) {
		boolean res = nodes.remove( node );
		discard( node );
		return res;
	}
}

