package rin.engine.game.entity.animated;

import rin.engine.event.Trackable;
import rin.engine.event.Tracker;
import rin.engine.game.entity.RenderedEntityAdapter;
import rin.engine.meta.RinTracked;
import rin.engine.meta.RinTrackedType;

public class AnimatedEntityAdapter extends RenderedEntityAdapter implements AnimatedEntity, Trackable {
	
	private AnimationData animationData = new AnimationData( this );
	private AnimationState animationState = new AnimationState( this );
	
	@Override
	public AnimationData getAnimationData() {
		return this.animationData;
	}
	
	@Override
	public AnimationState getAnimationState() {
		return this.animationState;
	}
	
	public Animation getCurrentAnimation() {
		return this.animationState.getCurrentAnimation();
	}
	
	@RinTracked( types = { RinTrackedType.UPDATE } )
	public void setCurrentAnimation( String anim ) {
		if( this.animationState.setCurrentAnimation( anim ) )
			this.tracker.update();
	}

	
	private Tracker tracker = new Tracker( this );
	
	@Override
	public Tracker getTracker() {
		return this.tracker;
	}

}
