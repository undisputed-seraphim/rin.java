package rin.sample.chase.abilities;

import rin.engine.game.ability.AbstractAbilityEffect;

public abstract class StandardAbilityEffect extends AbstractAbilityEffect {

	@Override
	public final StandardAbilityTarget getTarget() {
		return (StandardAbilityTarget)target;
	}

	@Override
	public abstract void process();

}
