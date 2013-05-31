package rin.engine.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class NodeTree<T extends TreeNode<T>> implements Iterable<T> {
	protected boolean dirty = false;
	protected List<T> nodes = new ArrayList<T>();
	private HashMap<String, T> idCache = new HashMap<String, T>();
	private List<T> stack = new ArrayList<T>();
	
	protected void cache( T node ) {
		if( idCache.get( node.getId() ) == null )
			idCache.put( node.getId(), node );
		else throw new RuntimeException( "ID " + node.getId() + " already exists." );
	}
	
	protected void discard( T node ) {
		if( idCache.get( node.getId() ) != null )
			idCache.remove( node.getId() );
		else System.err.println( "Removed node " + node.getId() + " was not cached...?" );
	}
	
	public T add( T node ) {
		node.tree = this;
		nodes.add( node );
		cache( node );
		dirty = true;
		return idCache.get( node.getId() );
	}
	
	public boolean remove( T node ) {
		boolean res = nodes.remove( node );
		discard( node );
		dirty = true;
		return res;
	}
	
	public void clear() {
		for( T t : nodes ) {
			t.clear();
			discard( t );
		}
		nodes.clear();
		idCache.clear();
		stack.clear();
	}
	
	public T find( String id ) { return idCache.get( id ); }

	private List<T> addToList( T node ) {
		stack.add( node );
		for( T t : node.nodes )
			addToList( t );
		return stack;
	}

	private void updateStack() {
		if( dirty ) {
			stack.clear();
			for( T an : nodes )
				addToList( an );
			dirty = false;
		}
	}

	@Override
	public Iterator<T> iterator() {
		updateStack();
		return stack.iterator();
	}
	
}
