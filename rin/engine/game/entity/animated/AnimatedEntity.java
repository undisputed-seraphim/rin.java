package rin.engine.game.entity.animated;

import rin.engine.game.entity.RenderedEntity;

public interface AnimatedEntity extends RenderedEntity {
	
	public AnimationData getAnimationData();
	
	public Animation getCurrentAnimation();
	public boolean hasAnimation( String id );
	public void setCurrentAnimation( String id );

}
