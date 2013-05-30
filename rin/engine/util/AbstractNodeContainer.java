package rin.engine.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AbstractNodeContainer<T extends AbstractNode<T>> {
	protected List<T> nodes = new ArrayList<T>();
	private HashMap<String, T> idCache = new HashMap<String, T>();
	
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
		node.parent = this;
		nodes.add( node );
		cache( node );
		return idCache.get( node.getId() );
	}
	
	public boolean remove( T node ) {
		boolean res = false;
		if( node.parent == this ) {
			res = nodes.remove( node );
			discard( node );
		}
		else res = idCache.get( node.getId() ).remove( node );
		return res;
	}
}
