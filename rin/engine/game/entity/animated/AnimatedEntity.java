package rin.engine.game.entity.animated;

import rin.engine.game.entity.Entity;

public interface AnimatedEntity extends Entity {
	
	public AnimationData getAnimationData();
	public AnimationState getAnimationState();

}
