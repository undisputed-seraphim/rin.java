package rin.engine.view.lib3d;

import java.util.HashMap;

import rin.engine.system.ident.NodeTree;

public class Skeleton extends NodeTree<JointNode> {
	
	private HashMap<String, Animation> animations = new HashMap<String, Animation>();
	private Animation currentAnimation;
	
	public Skeleton( JointNode j ) { super( j, false ); }
	public Skeleton( JointNode j, boolean cache ) { super( j, cache ); }
	
	public Animation getCurrentAnimation() { return currentAnimation; }
	
	public Animation addAnimation( String id ) {
		animations.put( id, new Animation( id ) );
		if( currentAnimation == null )
			currentAnimation = animations.get( id );
		
		return animations.get( id );
	}
	
	public void update( double dt ) {
		// update animation and
		if( currentAnimation != null )
			currentAnimation.update( dt );
		
		// update this skeletons joints, traversing in the proper order by default
		for( JointNode jn : this )
			jn.update( dt );
	}
}
