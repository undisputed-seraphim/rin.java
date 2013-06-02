package rin.engine.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractTreeIterator<T extends TreeNode<T>> implements Iterable<T> {
	protected boolean dirty = false;
	protected List<T> stack = new ArrayList<T>();
	
	protected abstract List<T> getList();
	
	private List<T> addToList( T node ) {
		stack.add( node );
		for( T t : node.nodes )
			addToList( t );
		return stack;
	}

	private void updateStack() {
		if( dirty ) {
			stack.clear();
			for( T an : getList() )
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
