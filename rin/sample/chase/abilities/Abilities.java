package rin.sample.chase.abilities;

import rin.engine.game.ability.Ability;
import rin.engine.game.ability.AbilityFactory;
import rin.sample.chase.abilities.effects.DamageEffect;

public class Abilities {

	public static Ability FIREBALL = AbilityFactory.createAbility( null, new DamageEffect( 15 ) );
	
}
