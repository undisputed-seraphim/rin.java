package rin.engine.util;

import java.util.ArrayList;

public class AdvancedNodeTree<T extends AdvancedTreeNode<T>> extends AdvancedTreeIterator<T> {
	
	protected ArrayList<T> nodes = new ArrayList<T>();
	
	public AdvancedNodeTree() {}
	
	public ArrayList<T> getList() { return nodes; }
	public boolean accept( T node ) { return true; }
	
	private <R extends T> void updateInfo( R node ) {
		node.tree = this;
		
		for( T t : node.nodes )
			updateInfo( t );
	}
	
	public <R extends T> R add( R node ) {
		if( node.parent != null )
			node.parent.remove( node );
		else if( node.tree != null )
			node.tree.remove( node );
		node.parent = null;
		updateInfo( node );
		
		nodes.add( node );
		return node;
	}
	
	public <R extends T> boolean remove( R node ) {
		return nodes.remove( node );
	}
	
	public void clear() {
		for( T t : nodes )
			t.clear();
		nodes.clear();
	}
	
	public T find( String id ) {
		for( T t : all() )
			if( t.name.equals( id ) )
				return t;
		return null;
	}
	
	public <R extends T> R find( String id, Class<R> cls ) {
		for( T t : nodes )
			if( t.name.equals( id ) )
				return cls.cast( t );
		return null;
	}
	
}
