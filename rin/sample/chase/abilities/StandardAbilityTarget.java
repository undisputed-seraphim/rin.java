package rin.sample.chase.abilities;

import rin.engine.game.ability.AbilityEffect;
import rin.engine.game.ability.AbilityTarget;
import rin.sample.chase.abilities.effects.StatusEffect;

public abstract class StandardAbilityTarget implements AbilityTarget {

	public abstract void damage( int amount );
	public abstract void status( StatusEffect effect );
	
	@Override
	public void apply( AbilityEffect<?> effect ) {
		/* the following will call the process method of the effect, passing this card as a target */
		effect.process( this );
	}
	
}
