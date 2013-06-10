package rin.engine.util;

import java.util.ArrayList;

public class AdvancedTreeNode<T extends AdvancedTreeNode<T>> extends UniquePointer {
	
	protected String name = "";
	protected AdvancedNodeTree<T> tree;
	protected AdvancedTreeNode<T> parent;
	protected ArrayList<T> nodes = new ArrayList<T>();
	
	public AdvancedTreeNode() { super(); }
	public AdvancedTreeNode( String id ) { super(); name = id; }
	
	public String getId() { return name; }
	public AdvancedNodeTree<?> getTree() { return tree; }
	public AdvancedTreeNode<?> getParent() { return parent; }
	
	private void updateInfo( T node ) {
		node.tree = tree;
		for( T t : node.nodes )
			updateInfo( t );
	}
	
	public <R extends T> R add( R node ) {
		if( node == this )
			throw new RuntimeException( "May not add an AdvancedTreeNode to itself." );
		
		if( node.parent != null )
			node.parent.remove( node );
		else if( node.tree != null )
			node.tree.remove( node );
		node.parent = this;
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
		for( T t : nodes )
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
	
	@Override
	public boolean equals( Object node ) {
		if( node instanceof AdvancedTreeNode ) {
			AdvancedTreeNode<?> obj = (AdvancedTreeNode<?>)node;
			return obj.id == id;
		}
		return false;
	}
}
