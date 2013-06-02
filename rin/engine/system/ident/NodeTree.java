package rin.engine.system.ident;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class NodeTree<T extends AbstractNode<T>> implements Iterable<T> {
	protected T root;
	protected boolean cached = false;
	protected boolean dirty = true;
	
	private ArrayList<T> stack = new ArrayList<T>();
	private HashMap<String, T> idCache = new HashMap<String, T>();
	
	public NodeTree( T r ) { this( r, false ); }
	public NodeTree( T r, boolean cache ) {
		cached = cache;
		r.tree = this;
		root = r;
		if( cache ) cache( root );
	}
	
	public T getRoot() { return root; }
	
	public <R extends T> R add( R node ) { return root.add( node ); }
	public void remove( T node ) { root.remove( node ); }
	
	protected void cache( T node ) {
		if( cached )
			if( idCache.get( node.getId() ) == null )
				idCache.put( node.getId(), node );
	}
	
	protected void discard( T node ) {
		if( cached )
			if( idCache.get( node.getId() ) != null )
				idCache.remove( node.getId() );
	}
	
	public T find( String id ) {
		if( cached ) return idCache.get( id );
		
		for( T t : this )
			if( t.getId().equals( id ) )
				return t;
		
		return null;
	}
	
	private void updateStack() {
		if( dirty ) {
			stack.clear();
			addToList( root );
			dirty = false;
		}
	}
	
	private List<T> addToList( T node ) {
		stack.add( node );
		for( T t : node.children )
			addToList( t );
		return stack;
	}
	
	@Override
	public Iterator<T> iterator() {
		updateStack();
		return stack.iterator();
	}
}
