package rin.engine.util;

import java.util.ArrayList;
import java.util.List;

public class NodeTree<T extends TreeNode<T>> extends AbstractTreeIterator<T> {
	protected List<T> nodes = new ArrayList<T>();

	@Override
	public List<T> getList() { return nodes; }
	
	/*public T add( T node ) {
		node.tree = this;
		nodes.add( node );
		dirty = true;
		return node;
	}*/
	
	public <R extends T> R add( R node ) {
		node.tree = this;
		nodes.add( node );
		dirty = true;
		return node;
	}
	
	public boolean remove( T node ) {
		boolean res = nodes.remove( node );
		dirty = true;
		return res;
	}
	
	public void clear() {
		for( T t : nodes ) {
			t.clear();
		}
		nodes.clear();
		stack.clear();
	}
	
	public T find( String id ) {
		for( T t : nodes )
			if( t.getId().equals( id ) )
				return t;
		return null;
	}
	
	public <R extends T> R find( String id, Class<R> cls ) {
		for( T t : nodes )
			if( t.getId().equals( id ) )
				if( cls.isInstance( t ) )
					return cls.cast( t );
		return null;
	}
	
}
