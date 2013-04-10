package rin.engine.game.entity.animated;

import java.util.HashMap;
import java.util.TreeMap;

public class AnimationData {
	
	private HashMap<String, Animation> animationMap = new HashMap<String, Animation>();
	private TreeMap<Float, KeyFrame> frames = new TreeMap<Float, KeyFrame>();
	private Skeleton skeleton = new Skeleton();
	
	public Skeleton getSkeleton() { return this.skeleton; }
	
	protected Animation getAnimation( String id ) {
		//TODO: unknown animation exception
		return this.animationMap.get( id );
	}
	
	public AnimationData addAnimationIndex( String id, int start, int end ) {
		//TODO: Animation ID exists exception
		this.animationMap.put( id, new Animation( id, start, end ) );
		return this;
	}
	
	public AnimationData addKeyFrame( KeyFrame frame  ) {
		//TODO: keyframe id exists exception
		this.frames.put( frame.getTime(), frame );
		return this;
	}
	
}
