package rin.engine.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class NodeContainerTree<T extends AbstractNode<T>> extends AbstractNodeContainer<T> implements Iterable<T> {
	protected boolean dirty = false;
	private List<T> stack = new ArrayList<T>();
	
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
