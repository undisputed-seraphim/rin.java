package rin.sample.chase;

import static rin.sample.chase.abilities.effects.StatusEffect.*;

import java.util.HashMap;

import rin.engine.game.ability.AbstractAbility;
import rin.engine.game.ability.AbilityUser;
import rin.sample.chase.abilities.StandardAbilityTarget;
import rin.sample.chase.abilities.effects.StatusEffect;

public class Card extends StandardAbilityTarget implements AbilityUser {

	private String name;
	private HashMap<Integer, AbstractAbility> abilities = new HashMap<Integer, AbstractAbility>();
	private int count = 0;
	
	public Card( String id ) { name = id; }
	
	@Override
	public int addAbility( AbstractAbility ability ) {
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
