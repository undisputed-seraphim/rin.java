package rin.gl.lib3d;

import java.util.ArrayList;
import java.util.List;

import rin.engine.system.ident.NodeTree;

public class ModelScene extends Poly {

	protected boolean ready = false;
	private double dt = 0;
	private long start;
	private List<Animation> animations = new ArrayList<Animation>();
	private Animation currentAnimation;
	
	private NodeTree<Node> tree;
	
	public ModelScene( Node r ) {
		r.scene = this;
		tree = new NodeTree<Node>( r, true );
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
		return tree.getRoot();
	}
	
	public void ready() {
		ready = true;
		start = System.currentTimeMillis();
	}
	
	public Node find( String name ) { return tree.find( name ); }
	
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
		
		for( Node n : tree )
			n.update( dt );
		
		for( Node n : tree )
			n.render();
	}
}
