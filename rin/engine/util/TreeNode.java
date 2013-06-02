package rin.engine.util;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<T extends TreeNode<T>> extends AbstractTreeIterator<T> {
	protected final String name;
	protected T parent;
	protected AbstractTreeIterator<T> tree;
	protected List<T> nodes = new ArrayList<T>();
	
	public TreeNode( String id ) { name = id; }
	public String getId() { return name; }
	
	@Override protected List<T> getList() { return nodes; }
	
	@SuppressWarnings("unchecked")
	public <R extends T> R add( R node ) {
		//TODO: if parent is not null, remove this child from its parent before inserting
		node.parent = (T)this;
		node.tree = tree;
		nodes.add( node );
		if( tree instanceof CachedNodeTree )
			((CachedNodeTree<T>)tree).cache( node );
		
		if( tree != null ) tree.dirty = true;
		dirty = true;
		return node;
	}
	
	public boolean remove( T node ) {
		boolean res = nodes.remove( node );
		if( tree instanceof CachedNodeTree )
			((CachedNodeTree<T>)tree).discard( node );
		
		if( tree != null ) tree.dirty = true;
		dirty = true;
		return res;
	}
	
	public void clear() {
		for( T t : nodes ) {
			t.clear();
			if( tree instanceof CachedNodeTree )
				((CachedNodeTree<T>)tree).discard( t );
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
