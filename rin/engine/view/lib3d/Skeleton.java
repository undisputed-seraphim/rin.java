package rin.engine.view.lib3d;

import java.util.HashMap;

import rin.engine.lib.gui.GUIFactory;
import rin.engine.lib.gui.GUIManager;
import rin.engine.system.ident.NodeTree;

public class Skeleton extends NodeTree<JointNode> {
	
	private HashMap<String, Animation> animations = new HashMap<String, Animation>();
	private Animation currentAnimation;
	
	public Skeleton( JointNode j ) { super( j, false ); }
	public Skeleton( JointNode j, boolean cache ) {
		super( j, cache );
	}
	
	public Animation getCurrentAnimation() { return currentAnimation; }
	public Animation setCurrentAnimation( String id ) {
		currentAnimation = animations.get( id );
		currentAnimation.restart();
		return currentAnimation;
	}
	
	public Animation addAnimation( String id ) {
		animations.put( id, new Animation( id ) );
		if( currentAnimation == null )
			currentAnimation = animations.get( id );
		
		return animations.get( id );
	}
	
	public void showDebugWindow() {
		GUIFactory.createWindow( "animations" ).setSize( 500, 500 );
		for( Animation a : animations.values() ) {
			GUIFactory.getWindow( "animations" ).add( GUIFactory.createCheckBox( a.getName(), a.getName() ).onChange( new GUIFactory.CheckBoxEvent() {
				
				@Override
				public void run() {
					setCurrentAnimation( target.getId() );
				}
				
			}) );
			if( a.getName().equals( currentAnimation.getName() ) )
				GUIFactory.getCheckBox( a.getName() ).check();
		}
		GUIFactory.getWindow( "animations" ).show();
	}
	
	public void update( double dt ) {
		// update animation and
		if( currentAnimation != null )
			currentAnimation.update( dt );
		
		// update this skeletons joints, traversing in the proper order by default
		for( JointNode jn : this ) {
			jn.update( dt );
		}
	}
}
