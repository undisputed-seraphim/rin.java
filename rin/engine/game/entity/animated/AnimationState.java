package rin.engine.game.entity.animated;

public class AnimationState {

	private final AnimatedEntity entity;
	
	private String currentAnimation = null;
	private int currentFrame = 0;
	private boolean looping = false;
	
	public AnimationState( AnimatedEntity entity ) {
		this.entity = entity;
		this.setCurrentAnimation( "default" );
	}
	
	public Animation getCurrentAnimation() {
		return this.entity.getAnimationData().getAnimation( this.currentAnimation );
	}
	
	public boolean setCurrentAnimation( String anim ) {
		if( this.currentAnimation.equals( anim ) )
			return false;
		
		this.currentAnimation = anim;
		return true;
	}
	
	public int getCurrentFrame() {
		return this.currentFrame;
	}
	
	public boolean isLooping() {
		return this.looping;
	}
	
}
