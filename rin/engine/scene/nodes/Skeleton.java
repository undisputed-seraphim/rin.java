package rin.engine.scene.nodes;

import java.util.HashMap;

import rin.engine.lib.gui.GUIFactory;
import rin.engine.util.CachedNodeTree;

public class Skeleton extends CachedNodeTree<JointNode> {
	private HashMap<String, Animation> animations = new HashMap<String, Animation>();
	private Animation currentAnimation;
	
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
		for( JointNode jn : this )
			jn.update( dt );
	}
}
