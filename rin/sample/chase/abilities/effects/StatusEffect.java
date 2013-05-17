package rin.sample.chase.abilities.effects;

import rin.sample.chase.abilities.StandardAbilityEffect;

public class StatusEffect extends StandardAbilityEffect {

	public static final int HEALTHY = 0;
	public static final int POISON = 1;
	public static final int SLEEP = 2;
	public static final int DRAIN = 3;
	
	private int effect = 0;
	public StatusEffect( int type ) { effect = type; }
	public int getType() { return effect; }
	
	@Override
	public void process() {
		getTarget().status( this );
	}

}
