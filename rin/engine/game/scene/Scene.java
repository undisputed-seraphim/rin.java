package rin.engine.game.scene;

import rin.engine.view.View;

public class Scene {

	private View view;
	public void setView( View view ) { this.view = view; }
	
	public void update( long dt ) {
		// magically update all objects that need to updated
		System.out.println( "Scene#update " + dt );
	}
	
	public void render() {
		// render this scene using the view
		System.out.println( "Scene#render" );
	}
	
}
