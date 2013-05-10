package rin.engine.view.scene;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SceneGraph implements Iterable<Node> {
	protected Scene scene;
	protected List<Node> nodes = new CopyOnWriteArrayList<Node>();
	
	public SceneGraph( Scene s ) {
		setScene( s );
	}
	
	protected void setScene( Scene s ) {
		scene = s;
	}
	
	/* how is a node added to this scenegraph? */
	public void add( Node n ) {
		nodes.add( n );
	}
	
	/* how is a node removed from this scenegraph? */
	public void remove( Node n ) {
		nodes.remove( n );
	}

	/* determines if current node should be accepted for rendering or rejected */
	public boolean accept( Node n ) {
		return true;
	}
	
	@Override
	public Iterator<Node> iterator() {
		return new Iterator<Node>() {
			private boolean current;
			private int count = 0;
			private int size = nodes.size();
			
			public Node get( int index ) {
				return nodes.get( index );
			}
			
			@Override
			public boolean hasNext() {
				current = false;
				while( count < size && current == false )
					current = accept( get( count++ ) );
				return current != false;
			}
			
			@Override
			public Node next() {
				return get( count );
			}

			@Override
			public void remove() {
				/* nope */
			}
		};
	}
}
