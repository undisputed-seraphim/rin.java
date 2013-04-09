package rin.engine.game.entity.animated;

import rin.engine.event.Trackable;
import rin.engine.event.Tracker;
import rin.engine.game.entity.RenderedEntityAdapter;
import rin.engine.meta.RinTracked;
import rin.engine.meta.RinTrackedType;

public class AnimatedEntityAdapter extends RenderedEntityAdapter implements AnimatedEntity, Trackable {
	
	private AnimationData animationData = new AnimationData();
	private Animation currentAnimation = null;
	
	@Override
	public AnimationData getAnimationData() {
		return this.animationData;
	}
	
	@Override
	public Animation getCurrentAnimation() {
		return this.currentAnimation;
	}
	
	@Override
	public boolean hasAnimation( String id ) {
		return this.animationData.getAnimation( id ) != null;
	}
	
	@Override
	@RinTracked( types = { RinTrackedType.UPDATE } )
	public void setCurrentAnimation( String id ) {
		if( !this.currentAnimation.id.equals( id ) ) {
			if( this.hasAnimation( id ) ) {
				this.currentAnimation = this.animationData.getAnimation( id );
				this.tracker.update();
			}
		}
	}
	
	private Tracker tracker = new Tracker( this );
	
	@Override
	public Tracker getTracker() {
		return this.tracker;
	}

}
