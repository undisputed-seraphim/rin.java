package rin.sample.chase.abilities.effects;

import rin.sample.chase.abilities.StandardAbilityEffect;

public class DamageEffect extends StandardAbilityEffect {

	private int magnitude;
	public DamageEffect( int amount ) { magnitude = amount; }

	@Override
	public void process() {
		getTarget().damage( magnitude );
	}

}
