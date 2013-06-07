package rin.engine.util;

import java.util.HashMap;

public class CachedNodeTree<T extends TreeNode<T>> extends NodeTree<T> {

	private HashMap<String, T> idCache = new HashMap<String, T>();
	
	protected void cache( T node ) {
		if( idCache.get( node.getId() ) == null )
			idCache.put( node.getId(), node );
		else throw new RuntimeException( "ID '" + node.getId() + "' already exists." );
	}
	
	protected void discard( T node ) {
		if( idCache.get( node.getId() ) != null )
			idCache.remove( node.getId() );
		else System.err.println( "Removed node " + node.getId() + " was not cached...?" );
	}
	
	@Override
	public <R extends T> R add( R node ) {
		super.add( node );
		cache( node );
		if( node.nodes.size() > 0 ) {
			for( T t : this ) {
				if( idCache.get( t.getId() ) == null )
					cache( t );
			}
			dirty = true;
		}
		return node;
	}
	
	@Override
	public boolean remove( T node ) {
		boolean res = super.remove( node );
		discard( node );
		return res;
	}
	
	@Override
	public void clear() {
		for( T t : nodes ) {
			t.clear();
			discard( t );
		}
		nodes.clear();
		idCache.clear();
		stack.clear();
	}
	
	@Override
	public T find( String id ) { return idCache.get( id ); }
	
	@Override
	public <R extends T> R find( String id, Class<R> cls ) {
		T res = idCache.get( id );
		if( cls.isInstance( res ) )
			return cls.cast( res );
		return null;
	}
	
}
