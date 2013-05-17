package rin.sample.chase.abilities;

import rin.engine.game.ability.AbstractAbilityEffect;
import rin.engine.game.ability.AbilityTarget;
import rin.sample.chase.abilities.effects.StatusEffect;

public abstract class StandardAbilityTarget implements AbilityTarget {

	public abstract void damage( int amount );
	public abstract void status( StatusEffect effect );
	
	@Override
	public void apply( AbstractAbilityEffect effect ) {
		effect.setTarget( this ).process();
	}
	
}
