package rin.engine.scene.lib2d;

import static org.lwjgl.opengl.GL20.glUniform1i;
import rin.engine.view.gl.GL;

public class AnimatedSprite extends Sprite {

	private int mapx = 1;
	private int mapy = 1;
	
	private int sw = 1;
	private int sh = 1;
	
	private int current = 0;
	private int end = 0;
	
	private double mod = 1.5;
	private double animDt = 0.0;
	
	private String currentSprite = "";
	
	public AnimatedSprite( String id, SpriteMap sm ) {
		super( id, sm );
		sw = sm.getSpriteWidth();
		sh = sm.getSpriteHeight();
		currentSprite = "right_idle";
		end = sm.getSpriteInfo( "right_idle" ).getFrameCount();
		mapx = sm.getSpriteInfo( "right_idle" ).getOffsetX( 0 );
		mapy = sm.getSpriteInfo( "right_idle" ).getOffsetY( 0 );
	}
	
	public void updateDt( double dt ) {
		animDt += dt * mod;
		if( animDt > end )
			animDt = animDt - end;
		if( (int)animDt != current ) {
			current = (int)animDt;
			mapx = _map.getSpriteInfo( currentSprite ).getOffsetX( current );
			mapy = _map.getSpriteInfo( currentSprite ).getOffsetY( current );
		}
	}
	
	private void applySprite() {
		glUniform1i( GL.getUniform( "mapw" ), _map.getMapWidth() );
		glUniform1i( GL.getUniform( "maph" ), _map.getMapHeight() );
		
		glUniform1i( GL.getUniform( "sw" ), sw );
		glUniform1i( GL.getUniform( "sh" ), sh );
		
		glUniform1i( GL.getUniform( "mapx" ), mapx );
		glUniform1i( GL.getUniform( "mapy" ), mapy );
	}
	
	public void process( double dt ) {
		super.process( dt );
		updateDt( dt );
		applySprite();
	}
}
