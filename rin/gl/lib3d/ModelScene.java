package rin.gl.lib3d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ModelScene extends Poly implements Iterable<Node> {

	private Node root;
	protected boolean ready = false;
	private double dt = 0;
	private long start;
	private List<Node> stack = new ArrayList<Node>();
	private List<Animation> animations = new ArrayList<Animation>();
	private Animation currentAnimation;
	
	public ModelScene( Node r ) {
		r.scene = this;
		root = r;
	}
	
	public Animation getCurrentAnimation() {
		return currentAnimation;
	}
	
	public Animation addAnimation( String name ) {
		animations.add( new Animation( name ) );
		if( currentAnimation == null )
			currentAnimation = animations.get( animations.size() - 1 );
		return animations.get( animations.size() - 1 );
	}
	
	public Node getRoot() {
		return root;
	}
	
	public void ready() {
		ready = true;
		start = System.currentTimeMillis();
		update();
	}
	
	public void update() {
		if( ready )
			getNodeList();
	}
	
	public Node find( String name ) {
		if( !ready ) getNodeList();
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
		long ms = System.currentTimeMillis();
		dt = (ms - start) * 0.001;
		start = ms;
	}
	
	@Override
	public void render( boolean unique ) {
		updateDt();
		if( currentAnimation != null )
			currentAnimation.update( dt );
		
		for( Node n : this )
			n.update( dt );
		
		for( Node n : this )
			n.render();
	}
}
