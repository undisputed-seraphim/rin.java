package rin.sample.chase.abilities.effects;

import rin.engine.game.ability.AbilityEffect;
import rin.sample.chase.abilities.StandardAbilityTarget;

public class DamageEffect implements AbilityEffect<StandardAbilityTarget> {

	private int magnitude;
	public DamageEffect( int amount ) { magnitude = amount; }
	
	@Override
	public void process( StandardAbilityTarget target ) {
		target.damage( magnitude );
	}

}
