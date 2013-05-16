package rin.sample.chase;

import static rin.sample.chase.abilities.effects.StatusEffect.*;

import java.util.HashMap;

import rin.engine.game.ability.Ability;
import rin.engine.game.ability.AbilityUser;
import rin.sample.chase.abilities.StandardAbilityTarget;
import rin.sample.chase.abilities.effects.StatusEffect;

public class Card extends StandardAbilityTarget implements AbilityUser {

	private HashMap<Integer, Ability> abilities = new HashMap<Integer, Ability>();
	private int count = 0;
	
	@Override
	public int addAbility( Ability ability ) {
		abilities.put( count++, ability );
		return count - 1;
	}

	@Override
	public void useAbility( int id ) {
		abilities.get( id ).execute();
	}

	@Override
	public void damage( int amount ) {
		/* apply damage to this card */
	}
	
	@Override
	public void status( StatusEffect effect ) {
		switch( effect.getType() ) {
		case HEALTHY: /* do something here */ break;
		case POISON: /* do something here */ break;
		case DRAIN: /* do something here */ break;
		}
	}
}
