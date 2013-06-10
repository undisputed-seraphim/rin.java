package rin.engine.util;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public abstract class AdvancedTreeIterator<T extends AdvancedTreeNode<T>> implements Iterable<T> {
	
	private class TreeIterator implements Iterator<T> {

		private Stack<T> stak = new Stack<T>();
		private boolean all = false;
		private T current = null;
		
		private TreeIterator init( boolean a ) {
			all = a;
			stak.clear();
			current = null;
			if( getList().size() > 0 )
				for( T t : getList() )
					if( accept( t ) || all )
						stak.push( t );
			return this;
		}
		
		@Override
		public boolean hasNext() { return !stak.empty(); }

		@Override
		public T next() {
			current = stak.pop();
			for( T t : current.nodes )
				if( accept( t ) || all )
					stak.push( t );
			return current;
		}

		@Override
		public void remove() {}
		
	}
	
	private final TreeIterator iter = new TreeIterator();
	private final Iterable<T> able = new Iterable<T>() {

		@Override
		public Iterator<T> iterator() {
			return iter.init( true );
		}
		
	};
	
	protected abstract List<T> getList();
	protected abstract boolean accept( T node );

	@Override
	public Iterator<T> iterator() { return iter.init( false ); }
	public Iterable<T> all() { return able; }

}
