package rin.sample.chase.abilities;

import rin.engine.game.ability.AbstractAbility;
import rin.engine.game.ability.AbilityFactory;
import rin.sample.chase.abilities.effects.DamageEffect;

public class Abilities {

	public static AbstractAbility FIREBALL = AbilityFactory.createAbility( null, new DamageEffect( 15 ) );
	
}
