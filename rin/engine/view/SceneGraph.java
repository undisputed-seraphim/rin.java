package rin.engine.view;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SceneGraph implements Iterable<Actor> {
	protected Scene scene;
	protected List<Actor> actors = new CopyOnWriteArrayList<Actor>();
	
	public SceneGraph( Scene s ) {
		setScene( s );
	}
	
	protected void setScene( Scene s ) {
		scene = s;
	}
	
	/* how is a node added to this scenegraph? */
	public void add( Actor a ) {
		actors.add( a );
	}
	
	/* how is a node removed from this scenegraph? */
	public void remove( Actor a ) {
		actors.remove( a );
	}

	/* determines if current node should be accepted for rendering or rejected */
	public boolean accept( Actor a ) {
		return false;
	}
	
	@Override
	public Iterator<Actor> iterator() {
		return new Iterator<Actor>() {
			private boolean current;
			private int count = 0;
			private int size = actors.size();
			
			public Actor get( int index ) {
				return actors.get( index );
			}
			
			@Override
			public boolean hasNext() {
				current = false;
				while( count < size && current == false )
					current = accept( get( count++ ) );
				return current != false;
			}
			
			@Override
			public Actor next() {
				return get( count );
			}

			@Override
			public void remove() {
				/* nope */
			}
		};
	}
}
