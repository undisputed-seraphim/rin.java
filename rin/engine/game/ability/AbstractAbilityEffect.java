package rin.engine.game.ability;

public abstract class AbstractAbilityEffect {

	protected AbilityTarget target;
	public AbstractAbilityEffect setTarget( AbilityTarget abilityTarget ) {
		target = abilityTarget;
		return this;
	}
	
	public abstract AbilityTarget getTarget();
	public abstract void process();
	
}
