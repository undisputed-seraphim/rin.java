package rin.engine.game.entity.animated;

import java.util.HashMap;

public class AnimationData {

	private final AnimatedEntity entity;
	
	private HashMap<String, Animation> animationMap = new HashMap<String, Animation>();
	private HashMap<Float, KeyFrame> frames = new HashMap<Float, KeyFrame>();
	private Skin skin = new Skin();
	
	public AnimationData( AnimatedEntity entity ) {
		this.entity = entity;
		this.animationMap.put( "default", new Animation( "default", 0, 0 ) );
	}
	
	protected Animation getAnimation( String id ) {
		//TODO: unknown animation exception
		return this.animationMap.get( id );
	}
	
	public AnimationData addAnimationIndex( String id, int start, int end ) {
		//TODO: Animation ID exists exception
		this.animationMap.put( id, new Animation( id, start, end ) );
		return this;
	}
	
	public KeyFrame addKeyFrameIndex( float id ) {
		//TODO: keyframe id exists exception
		this.frames.put( id, new KeyFrame( id ) );
		return this.frames.get( id );
	}
	
}
