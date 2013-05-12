package rin.gl.lib3d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ModelScene extends Poly implements Iterable<Node> {

	private Node root;
	protected boolean ready = false;
	private long dt = 0L;
	private List<Node> stack = new ArrayList<Node>();
	
	public ModelScene( Node r ) {
		r.scene = this;
		root = r;
	}
	
	public Node getRoot() {
		return root;
	}
	
	public void ready() {
		ready = true;
		dt = System.nanoTime();
		update();
	}
	
	public void update() {
		if( ready )
			getNodeList();
	}
	
	public Node find( String name ) {
		for( Node n : this )
			if( n.getName().equalsIgnoreCase( name ) )
				return n;
		return null;
	}
	
	public void getNodeList() {
		stack = new ArrayList<Node>();
		stack.addAll( addToList( root ) );
	}
	
	private List<Node> addToList( Node n ) {
		stack.add( n );
		
		for( Node nc : n.children )
			addToList( nc );
		
		return stack;
	}
	
	@Override
	public Iterator<Node> iterator() {
		return new Iterator<Node>() {
			private int count = 0;
			private int size = stack.size();
			
			@Override
			public boolean hasNext() {
				return count < size;
			}
			
			@Override
			public Node next() {
				return stack.get( count++ );
			}

			@Override
			public void remove() {
				/* nope */
			}
		};
	}
	
	private void updateDt() {
		dt = System.nanoTime() - dt;
	}
	
	@Override
	public void render( boolean unique ) {
		updateDt();
		for( Node n : this )
			n.update( dt );
	}
}
