package rin.engine.system.ident;

import java.util.ArrayList;

public abstract class AbstractNode<T extends AbstractNode<T>> {

	protected T parent;
	protected String name;
	protected NodeTree<T> tree;
	protected ArrayList<T> children = new ArrayList<T>();

	public AbstractNode( String id ) { name = id; }
	
	protected abstract T actual();
	
	public T getParent() { return parent; }
	public String getId() { return name; }
	public NodeTree<T> getTree() { return tree; }
	public ArrayList<T> getChildren() { return children; }
	
	public T add( T node ) {
		node.tree = tree;
		node.parent = actual();
		children.add( node );
		
		if( tree != null ) {
			tree.dirty = true;
			tree.cache( node );
		}
		return getRecent();
	}
	
	public void remove( T node ) {
		children.remove( node );
		if( tree != null ) {
			tree.dirty = true;
			tree.discard( node );
		}
	}
	
	public T find( String id ) {
		if( name.equals( id ) )
			return actual();
		
		for( T t : children )
			if( t.getId().equals( id ) )
				return t;
		
		return null;
	}
	
	public T getRecent() {
		return children.get( children.size() - 1 );
	}
}
