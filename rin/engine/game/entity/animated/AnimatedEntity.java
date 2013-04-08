package rin.engine.game.entity.animated;

import rin.engine.game.entity.RenderedEntity;

public interface AnimatedEntity extends RenderedEntity {
	
	public AnimationData getAnimationData();
	public AnimationState getAnimationState();

}
